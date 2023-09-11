package com.russozaripov.uploadfiletofewchunks.service;

import com.russozaripov.uploadfiletofewchunks.service.fileMerger.FileMerger;
import com.russozaripov.uploadfiletofewchunks.uploadingThread.UploadThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class FileUploadingService {
    public String UploadTheVideo(String InputFileURL){
    String outputFileName = "music";
        String extensionFile = InputFileURL.substring(InputFileURL.length() - 4);
    int threadCount = 4;
        try {

            URL url = new URL(InputFileURL);
            HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();// открываем url соединение

            long fileSize = urlConnection.getContentLength(); // get file size
            urlConnection.disconnect();// close connection

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            List<String> chunksFilesNames = new ArrayList<>();
            long chunkSize = fileSize / threadCount; // получаем размер одной части файла для каждого потока
            for (int i = 0; i < threadCount; i ++){
                long start = i * chunkSize; // откуда поток начинает загрузку
                long ends = i == threadCount - 1 ? fileSize - 1 : (i + 1) * chunkSize - 1;
                String fileName = "D:\\" + i + outputFileName;
                // если поток является последним(i == threadCount - 1) то мы загружаем сотавшуюся часть файла за исключением одного байта, так как отстет начинается с 0 (fileSize - 1)
                // если поток не является последним то загружаем следющим образом: номер потока * кусок файла и за исключением одного байта
                executorService.execute(new UploadThread(InputFileURL, fileName, start, ends, i));
                chunksFilesNames.add(fileName);
            }
            executorService.shutdown();// пул потоков больше не принимаем tasks
            while (!executorService.isTerminated()){
                // ждем пока все потоки закончат работу
                Thread.sleep(100);
            }


            FileMerger.fileMerge(chunksFilesNames.toArray(new String[0]), extensionFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Uploading ends";
    }
}
