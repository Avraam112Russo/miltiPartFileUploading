package com.russozaripov.uploadfiletofewchunks.service.fileMerger;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

@Slf4j
public class FileMerger {
    public static void fileMerge(String[] chunkFiles, String fileExtension){
        String outPutFileName = "D:\\allPartsMerge.mp3";
        try (RandomAccessFile allPartsMergeFile = new RandomAccessFile(outPutFileName, "rw")){
            for (String chunkName : chunkFiles){
                try(RandomAccessFile file = new RandomAccessFile(chunkName, "r")) {
                    int byteReader;
                    byte [] buffer = new byte[4096];
                    log.info("file merger is start:" + chunkName);
                    while ((byteReader = file.read(buffer)) != -1){
                        allPartsMergeFile.write(buffer, 0, byteReader);
                        // buffer это те байты которые надо записать
                        // 0 это индекс элемента в массиве buffer с которого надо начать запись
                        // bytereader это количество элементов в массиве
                    }
                    file.close(); // нужно обязательно закрыть randomAccess что бы затем удалить временные файлы
                    File filedelete = new File(chunkName);
                    if (filedelete.delete()){
                    log.info("file delete" + chunkName);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
