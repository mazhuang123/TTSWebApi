//package com.mz.ttswebapiproject.play;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import android.content.Context;
//import android.media.AudioTrack;
//import android.os.MemoryFile;
//
//import com.iflytek.cloud.msc.util.FileUtil;
//import com.iflytek.cloud.msc.util.log.DebugLog;
//
//public class PcmBuffer {
//    private final int DEF_BYTE = 2;
//    private final int DEF_CHANNEL = 1;
//    private final int DEF_RATE = 16000;
//    private final int DEF_MIN_BUF_SEC = 1 * 60;
//
//    private final int BLANK_BLOCK_MS = 500;
//
//    private final int DEF_MIN_BUF_SIZE = DEF_BYTE * DEF_CHANNEL * DEF_RATE * DEF_MIN_BUF_SEC;
//    private int mMaxFileSize = DEF_MIN_BUF_SIZE;
//
//    private ArrayList<AudioInfo> mAudioInfo = null;
//
//    private Context mContext = null;
//
//    private int mRate = PcmRecorder.RATE16K;
//
//    private volatile long mPercent = 0;
//
//    private MemoryFile memFile = null;
//    public volatile long mTotalSize = 0;
//    private volatile int mReadOffset = 0;
//
//    private AudioInfo mTempAudio = null;
//    private String mFilepath = "";
//    /**
//     * 本地文件的保存路径
//     */
//    private String mLocal_save_path = null;
//
//    private byte[] mAudioBuf = null;
//    private int mBufOffset = 0;
//    private int mBufLen = 0;
//    private int mProcScale = 100;
//    private final float MAX_PLAYABLE_PERCANT = 0.95f;
//
//    public static final int DEF_PROC_SCALE = 100;
//    private boolean mEndWithNull = true;
//    /**
//     * audioTrack的缓冲区的大小
//     * 合成音频的大小小于缓冲区大小时，蓝牙耳机的播放会存在问题，需要填补音频数据以保证正常播放
//     */
//    private int audioTrackBuffSize = 0;
//
//    /**
//     *
//     */
//    public PcmBuffer(Context context, int rate, final int minAudioSecs, String local_saved_path, final int procScale) {
//        mContext = context;
//        mPercent = 0;
//        mAudioInfo = new ArrayList<AudioInfo>();
//        mTotalSize = 0;
//        mRate = rate;
//        mLocal_save_path = local_saved_path;
//        mProcScale = procScale;
//
//        mMaxFileSize = mRate * DEF_BYTE * DEF_CHANNEL * minAudioSecs + DEF_MIN_BUF_SIZE;
//        DebugLog.LogD("min audio seconds: " + minAudioSecs + ", max audio buf size: " + mMaxFileSize);
//    }
//
//    /**
//     * 记录audioTrack缓冲区大小
//     *
//     * @param size
//     */
//    public void setAudioTrackBuffSize(int size) {
//        audioTrackBuffSize = size;
//    }
//
//    public void setPercent(int percent) {
//        if (percent >= 0 && percent <= mProcScale)
//            mPercent = percent;
//    }
//
//    public void writeBuffer(ConcurrentLinkedQueue<byte[]> data) throws IOException {
//        if (data == null)
//            return;
//        Iterator<byte[]> it = data.iterator();
//        while (it.hasNext()) {
//            byte[] t = it.next();
//            writeToFile(t);
//        }
//    }
//
//    public int getRate() {
//        return mRate;
//    }
//
//    public long getTotalSize() {
//        return mTotalSize;
//    }
//
//    /**
//     * @param datas    数据
//     * @param percent  百分比
//     * @param beginPos 开始索引
//     * @param endPos   结束索引
//     * @return ready to play
//     * @throws IOException
//     */
//    public void writeStream(ArrayList<byte[]> datas, int percent, int beginPos, int endPos)
//            throws IOException {
//
//        DebugLog.LogI("buffer percent = " + percent + ", beg=" + beginPos + ", end=" + endPos);
//        AudioInfo info = new AudioInfo(mTotalSize, mTotalSize, beginPos, endPos);
//        for (int i = 0; i < datas.size(); i++) {
//            writeToFile(datas.get(i));
//        }
//        info.mEndOffset = mTotalSize;
//        mPercent = percent;
//
//        synchronized (mAudioInfo) {
//            mAudioInfo.add(info);
//        }
//
//        DebugLog.LogI("allSize = " + mTotalSize + " maxSize=" + mMaxFileSize);
//    }
//
//    /**
//     * 保存到本地文件中
//     */
//    public boolean renameToLocal(String format) {
//        DebugLog.LogD("save to local: format = " + format + " totalSize = " + mTotalSize + " maxSize=" + mMaxFileSize);
//        if (FileUtil.saveFile(memFile, mTotalSize, mLocal_save_path)) {
//            return FileUtil.formatPcm(format, mLocal_save_path, getRate());
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * @param bufMillSec 最小缓冲时间，单位：毫秒
//     * @return
//     */
//    public boolean readyToPlay(int bufMillSec) {
//        if (mPercent > MAX_PLAYABLE_PERCANT * mProcScale)
//            return true;
//        //采样率16000,采样位数16bit的音频 1ms：32个字节
//        long allMillSec = mTotalSize / 32;
//        if (allMillSec >= bufMillSec && 0 < mTotalSize)
//            return true;
//        return false;
//    }
//
//    /**
//     * 写入内存文件
//     *
//     * @param data
//     * @throws IOException
//     */
//    private void writeToFile(byte[] data) throws IOException {
//        if (data == null || data.length == 0)
//            return;
//        try {
//            if (memFile == null) {
//                mFilepath = genFileName();
//                memFile = new MemoryFile(mFilepath, mMaxFileSize);
//                memFile.allowPurging(false);
//            }
//            memFile.writeBytes(data, 0, (int) mTotalSize, data.length);
//            mTotalSize += data.length;
//        } finally {
//        }
//    }
//
//    private String genFileName() {
//        String fileName = FileUtil.getUserPath(mContext);
//        fileName += System.currentTimeMillis() + "tts.pcm";
//        return fileName;
//    }
//
//    public int getMemFileLenth() {
//        if (null != memFile) {
//            return memFile.length();
//        }
//        return 0;
//    }
//
//    /**
//     * set to play state
//     *
//     * @throws IOException
//     */
//    public void beginRead() throws IOException {
//        mReadOffset = 0;
//        // get first audio block
//        mTempAudio = null;
//        if (mAudioInfo.size() > 0)
//            mTempAudio = mAudioInfo.get(0);
//    }
//
//    public int getPlayPercent() {
//        if (mTotalSize <= 0)
//            return 0;
////		DebugLog.LogD("mReadOffset="+mReadOffset+"---mBufLen="+mBufLen+"---mBufOffset"
////			+mBufOffset+"-mPercent="+mPercent+"-mTotalSize="+mTotalSize);
//        int ret = (int) (((mReadOffset - (mBufLen - mBufOffset)) * mPercent) / mTotalSize);
//
//        DebugLog.LogE("getPlayPercent, mReadOffset=" + mReadOffset + ", mBufLen=" + mBufLen + ",mBufOffset" + mBufOffset + ",mPercent" + mPercent + ",mTotalSize" + mTotalSize);
//
//        return ret;
//    }
//
//    public AudioInfo getPalyAudioInfo() {
//        if (mTempAudio != null) {
//            final long playOffset = mReadOffset - (mBufLen - mBufOffset);
//            if (playOffset >= mTempAudio.mBegOffset && playOffset <= mTempAudio.mEndOffset)
//                return mTempAudio;
//
//            synchronized (mAudioInfo) {
//                Iterator<AudioInfo> itor = mAudioInfo.iterator();
//                while (itor.hasNext()) {
//                    mTempAudio = itor.next();
//                    if (playOffset >= mTempAudio.mBegOffset && playOffset <= mTempAudio.mEndOffset)
//                        return mTempAudio;
//                }
//            }
//        }
//        return null;
//    }
//
//    public boolean isOver() {
//        return (mProcScale == mPercent) && (mReadOffset >= mTotalSize) && (mBufOffset >= mBufLen);
//    }
//
////    public boolean isError() {
////        return (mProcScale < mPercent) && (mReadOffset < mTotalSize) && (mBufOffset < mBufLen)&&;
////    }
//
//    public boolean playAble() {
//        return mReadOffset < mTotalSize
//                || mBufOffset < mBufLen;
//    }
//
//    public boolean hasMoreBuffer(final int bufSize) {
//        return bufSize <= mTotalSize - mReadOffset + mBufLen - mBufOffset;
//    }
//
//    public boolean isBufferingFinished() {
//        return mProcScale == mPercent;
//    }
//
//    public void setEndWithNull(final boolean endWithNull) {
//        mEndWithNull = endWithNull;
//    }
//
//    public boolean getEndWithNull() {
//        return mEndWithNull;
//    }
//
//    public void writeTrack(AudioTrack track, int BLOCK_SIZE) throws IOException {
////		DebugLog.LogD( "mBuffer.writeTrack enter:" +BLOCK_SIZE );
//        if (this.mBufOffset >= this.mBufLen) {
//            this.readAudio(BLOCK_SIZE);
//        }
//
//        int size = BLOCK_SIZE;
//        if (2 * BLOCK_SIZE > mBufLen - mBufOffset) {
//            size = mBufLen - mBufOffset;
//        }
//        track.write(mAudioBuf, mBufOffset, size);
//        this.mBufOffset += size;
//        if (isOver() && getEndWithNull()) {
//            writeTrackBlankBlock(track, BLOCK_SIZE);
//        }
////		DebugLog.LogD( "mBuffer.writeTrack leave" );
////		DebugLog.LogD( "mBuffer.writeTrack dataSize="+size+", left="+(mBufLen-mBufOffset) );
//    }
//
//    /**
//     * 追加尾部音频
//     *
//     * @param track
//     * @param BLOCK_SIZE
//     */
//    public void writeTrackBlankBlock(AudioTrack track, int BLOCK_SIZE) {
//        //在蓝牙耳机上，音频太短(小于Audio Track的缓冲区)，会不能正常播放
//        if (mTotalSize < audioTrackBuffSize) {
//            int size = (int) (audioTrackBuffSize - mTotalSize);
//            DebugLog.LogI("mBuffer.writeTrack writeTrackBlankBlock size: " + size);
//            byte[] data = new byte[size];
//            track.write(data, 0, size);
//        }
//    }
//
//    public void deleteFile() {
//        DebugLog.LogD("deleteFile");
//        try {
//            if (memFile != null) {
//                memFile.close();
//                memFile = null;
//            }
//        } catch (Exception e) {
//            DebugLog.LogE(e);
//        }
//    }
//
//    @Override
//    protected void finalize() throws Throwable {
//        deleteFile();
//        super.finalize();
//    }
//
//    public class AudioInfo {
//        long mBegOffset;    // audio data info
//        long mEndOffset;
//        int mBegIndex;    // text info
//        int mEndIndex;
//
//        public AudioInfo(long begOffset, long endOffset, int begIndex, int endIndex) {
//            mBegOffset = begOffset;
//            mEndOffset = endOffset;
//            mBegIndex = begIndex;
//            mEndIndex = endIndex;
//        }
//    }
//
//    private void readAudio(final int minSize) throws IOException {
////		DebugLog.LogD( "readAudio enter, minSize="+minSize );
//        if (null == this.mAudioBuf) {
//            this.mAudioBuf = new byte[10 * minSize];
//        }
//
//        int dataSize = this.mAudioBuf.length;
//        final int bufLen = (int) (mTotalSize - mReadOffset);
//        int readLen = dataSize;
//        if (bufLen < dataSize) {
//            dataSize = bufLen;
//            readLen = bufLen;
//        }
//
//        memFile.readBytes(mAudioBuf, mReadOffset, 0, readLen);
//        mReadOffset += readLen;
//        this.mBufOffset = 0;
//        this.mBufLen = dataSize;
//        DebugLog.LogE("readAudio leave, mBufLen=dataSize=" + dataSize + ", bufLen=" + bufLen);
//        DebugLog.LogE("===mReadOffset=" + mReadOffset + ", bufLen=" + bufLen);
//    }
//}