package com.russozaripov.uploadfiletofewchunks.uploadingThread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class UploadThread implements Runnable{
    private final String fileUrl;
    private final String outputFileName;
    private final long startRange;
    private final long endsRange;
    private final int threadId;

    @Override
    public void run(){
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();// устанавливаем соединение
            urlConnection.setRequestProperty("Range", "bytes=" + startRange + "-" + endsRange);
            // устанавливаем http header (заголовок) в данном случае заголовок Range
            // Range это стандартный http заголовок в котором надо указать диапозон байтов для скачивания и сервер в ответ пришлет только этот диапозон

            InputStream inputStream = urlConnection.getInputStream();// открываем поток данных
            RandomAccessFile outputFileStream = new RandomAccessFile(outputFileName, "rw");// создаем файл куда будет загружена отдельная часть
            int reader;
            byte[] buffer = new byte[4096]; // создаем буфер
                log.info(String.format("Thread number: %s is start work", Thread.currentThread().getId()));
            while ((reader = inputStream.read(buffer)) != -1){
                outputFileStream.write(buffer, 0, reader);// buffer хранит то что нужно записать // 0 - from . reader - to
            }
            inputStream.close();
            outputFileStream.close();
            urlConnection.disconnect();
            log.info(String.format("Thread №: %s ends work", threadId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
