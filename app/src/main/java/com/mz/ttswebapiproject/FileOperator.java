package com.mz.ttswebapiproject;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:51
 * @Description 文件描述：
 */
public class FileOperator {
    public  Map<Integer,File> cacheFileMap = new HashMap<>();
    /**
     * 将文件存入到内存中
     * @param resultBytes
     * @return
     */
    public File saveFileCache(int index,byte[] resultBytes){
        File tempMp3 = null;
        try {
            tempMp3 = File.createTempFile("cacheTTS", ".mp3");
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(resultBytes);
            fos.close();
            cacheFileMap.put(index,tempMp3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempMp3;
    }
    /**
     * 将文件存入到外存中
     * @param resultBytes
     * @return
     * @throws IOException
     */
    public void saveFileIntoLocal(Context context,int index,byte[] resultBytes) {
        String filePath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        File targetFile = new File(filePath+"/localTTS"+index+".mp3");
        if(targetFile.exists()){
            cacheFileMap.put(index,targetFile);
            return;
//            targetFile.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            fos.write(resultBytes);
            fos.close();
            cacheFileMap.put(index,targetFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public File loadFileFromMap(int index){
        return cacheFileMap.get(index);
    }
    public void removeExistFile(int index){
        File existFile = cacheFileMap.get(index);
        cacheFileMap.remove(index);
        if(existFile.exists()){
            existFile.delete();
        }
    }
}
