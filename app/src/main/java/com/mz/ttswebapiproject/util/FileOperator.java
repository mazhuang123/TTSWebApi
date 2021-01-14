package com.mz.ttswebapiproject.util;


import com.mz.ttswebapiproject.manager.TTSDataKeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:51
 * @Description 文件描述：
 */
public class FileOperator {
    public  Map<Integer,File> cacheFileMap = new HashMap<>();
    private File sumFile;
    private static FileOperator instance;
    private  FileOperator(){
        sumFile = new File(TTSDataKeeper.getInstance().getAudioPath());
        if(!sumFile.exists()){
            sumFile.mkdir();
        }
    }
    public static FileOperator getInstance(){
        if(instance == null){
            synchronized (FileOperator.class){
                if(instance == null){
                    instance = new FileOperator();
                }
            }
        }
        return instance;
    }

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
    public void saveFileIntoLocal(int index, ArrayList<byte[]> resultBytes) {
        int audioDataLen = 0;
        if(resultBytes == null || resultBytes.size()==0){
            return;
        }
        for(byte[] dataArray : resultBytes){
            audioDataLen+=dataArray.length;
        }
        File targetFile = cacheFileMap.get(index);
        if(targetFile!= null && targetFile.exists()){
            cacheFileMap.put(index,targetFile);
            return;
        }
        String suffix = ".wav";
        targetFile = new File(sumFile.getAbsolutePath()+"/localTTS"+index+suffix);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            byte[] headerByte = writeHeader(audioDataLen);
            fos.write(headerByte);
            for(byte[] data : resultBytes){
                fos.write(data);
            }
            fos.close();
            cacheFileMap.put(index,targetFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public File loadFileFromMap(int index){
        File file = cacheFileMap.get(index);
        return file;
    }
    public void removeExistFile(int index){
        File existFile = cacheFileMap.get(index);
        cacheFileMap.remove(index);
        if(existFile.exists()){
            existFile.delete();
        }
    }
    public void clearFile(){
        if(sumFile.exists() && sumFile.isDirectory()){
            File[] files = sumFile.listFiles();
            if(files!=null && files.length >0){
                for(File subFile : files){
                    subFile.delete();
                }
            }
        }
    }
    private byte[] writeHeader(long dataLength){
        long sampleRate = 16000;
        int channels = 1;
        long pcmDataLen = dataLength;
        long wavDataLen = pcmDataLen + 44;
        long bitsPerChannel = 16;
        long byteRate = sampleRate * channels * (bitsPerChannel >> 3);

        byte[] header = new byte[44];

        // RIFF/WAVE header
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte)(wavDataLen & 0xff);
        header[5] = (byte)((wavDataLen >> 8) & 0xff);
        header[6] = (byte)((wavDataLen >> 16) & 0xff);
        header[7] = (byte)((wavDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        // 'fmt' chunk
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        // 4 bytes, size of 'fmt' chunk
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        // format
        header[20] = 1; // framesPerPacket, pcm格式为1
        header[21] = 0;
        // channels
        header[22] = (byte)channels;
        header[23] = 0;
        // sampleRate
        header[24] = (byte)(sampleRate & 0xff);
        header[25] = (byte)((sampleRate >> 8) & 0xff);
        header[26] = (byte)((sampleRate >> 16) & 0xff);
        header[27] = (byte)((sampleRate >> 24) & 0xff);
        // byteRate
        header[28] = (byte)(byteRate & 0xff);
        header[29] = (byte)((byteRate >> 8) & 0xff);
        header[30] = (byte)((byteRate >> 16) & 0xff);
        header[31] = (byte)((byteRate >> 24) & 0xff);
        // block align
        header[32] = (byte) (channels * (bitsPerChannel >> 3)); // bytesPerFrame
        header[33] = 0;
        // bits per sample
        header[34] = 16;
        header[35] = 0;
        // data
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte)(pcmDataLen & 0xff);
        header[41] = (byte)((pcmDataLen >> 8) & 0xff);
        header[42] = (byte)((pcmDataLen >> 16) & 0xff);
        header[43] = (byte)((pcmDataLen >> 24) & 0xff);
        return header;

    }
}
