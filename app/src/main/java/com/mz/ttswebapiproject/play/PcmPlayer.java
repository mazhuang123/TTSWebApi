//package com.mz.ttswebapiproject.play;
//
//import android.content.Context;
//import android.media.AudioFormat;
//import android.media.AudioManager;
//import android.media.AudioManager.OnAudioFocusChangeListener;
//import android.media.AudioTrack;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//
//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.msc.util.FuncAdapter;
//import com.iflytek.cloud.msc.util.log.DebugLog;
//
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * PcmData Player
// *
// * @author yjzhao
// */
//public class PcmPlayer {
//    private static final String TAG = "PcmPlayer";
//
//    public static final int INIT = 0;
//    public static final int BUFFERING = 1;
//    public static final int PLAYING = 2;
//    public static final int PAUSED = 3;
//    public static final int STOPED = 4;
//
//    private static final int MSG_ERROR = 0;
//    private static final int MSG_PAUSE = 1;
//    private static final int MSG_RESUME = 2;
//    private static final int MSG_PERCENT = 3;
//    private static final int MSG_STOPED = 4;
//
//    private static final int MIN_SLEEP = 5;
//    private AudioTrack mAudioTrack = null;
//    private PcmBuffer mBuffer = null;
//    private Context mContext = null;
//    private PcmThread mThread = null;
//    private PcmPlayerListener mListener = null;
//    private volatile int mPlaytate = INIT;
//
//    private boolean mAudioFocus = true;// 内部支持播放器抢占策略
//    //声音类型为AudioManager.STREAM_MUSIC
//    private int mStreamType = 3;
//    private int mBufferSize;
//    // 是否会话过程中暂停后台音乐播放
//    private boolean mRequestFocus = false;
//    private boolean mChangeListenerFlag = false;// 用来区分是被抢占还是手动
//    private Object mAudioLock = new Object();
//    private Object mSyncObj = this;
//
//    private final int BYTES_OF_PER_SAMPLE = 2;
//    private final int FADE_TIME = 500;
//    private final int FADE_PERIOD = 50;
//    private int mPerPlaySize = FADE_PERIOD * 32;
//
//    private final float MAX_VOL = 1.0f;
//    private final float MIN_VOL = 0.0f;
//    private final float PER = (MAX_VOL - MIN_VOL) / (FADE_TIME / FADE_PERIOD);
//    private int mFadingSize = mPerPlaySize * (FADE_TIME / FADE_PERIOD);
//
//    private float mCurVol = MIN_VOL;
//    private float mTargetVol = MAX_VOL;
//    private float mCurFadingPeriod = PER;
//    private boolean mFading = false;
//
//    private boolean mBufferingFadingEnable = false;
//    private boolean mFadingEnable = false;
//    ReentrantLock mEndLock = new ReentrantLock();
//    Condition mEndCondition = mEndLock.newCondition();
//
//    public PcmPlayer(Context context, int streamType, boolean requestFocus, final boolean fadingEnable,
//                     final boolean bufferingFadingEnable) {
//        mContext = context;
//        mStreamType = streamType;
//        mRequestFocus = requestFocus;
//
//        mFadingEnable = fadingEnable;
//        mBufferingFadingEnable = bufferingFadingEnable;
//    }
//
//    /**
//     * 播放过程中实时改变steamtype。
//     *
//     * @param streamType
//     */
//    public void setStreamType(int streamType) {
//        DebugLog.LogD(TAG, "setmStreamType || streamType = " + streamType);
//        this.mStreamType = streamType;
//    }
//
//    /**
//     * 设置是否支持播放器抢占
//     *
//     * @param audioFocus
//     */
//    public void setAudioFocus(boolean audioFocus) {
//        DebugLog.LogD(TAG, "setAudioFocus " + audioFocus);
//        mAudioFocus = audioFocus;
//    }
//
//    /**
//     * 播放器抢占、获取监听。
//     */
//    OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
//        public void onAudioFocusChange(int focusChange) {
//            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
//                    || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
//                    || focusChange == AudioManager.AUDIOFOCUS_LOSS) {
//                DebugLog.LogD(TAG, "pause start");
//                // 播放器被抢占，暂停时回调出去
//                if (pause()) {
//                    DebugLog.LogD(TAG, "pause success");
//                    // 是被抢占
//                    mChangeListenerFlag = true;
//                    if (null != mListener) {
//                        mListener.onPaused();
//                    }
//                }
//                ;
//            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
//                DebugLog.LogD(TAG, "resume start");
//                // 被抢占后重新获得播放器，继续播放回调出去
//                if (mChangeListenerFlag) {
//                    mChangeListenerFlag = false;
//                    if (resume()) {
//                        DebugLog.LogD(TAG, "resume success");
//                        if (null != mListener) {
//                            mListener.onResume();
//                        }
//                    }
//                    ;
//                }
//            }
//        }
//    };
//
//    public PcmPlayer(Context context) {
//        mContext = context;
//    }
//
//    public int getState() {
//        return mPlaytate;
//    }
//
//    private void setState(int state) {
//        mPlaytate = state;
//    }
//
//    private boolean setState(final int srcState, final int dstState) {
//        boolean ret = false;
//
//        synchronized (mSyncObj) {
//            if (srcState == mPlaytate) {
//                mPlaytate = dstState;
//                ret = true;
//            }//end of if is srcState
//        }//end of synchronized
//
//        return ret;
//    }
//
//    /**
//     * 创建播放器。
//     *
//     * @throws Exception
//     */
//    private void createAudio() throws Exception {
//        DebugLog.LogD(TAG, "createAudio start");
//        int rate = mBuffer.getRate();
//        mBufferSize = AudioTrack.getMinBufferSize(rate,
//                AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                AudioFormat.ENCODING_PCM_16BIT);
//
//        mPerPlaySize = rate / 1000 * BYTES_OF_PER_SAMPLE * FADE_PERIOD;
//        mFadingSize = mPerPlaySize * (FADE_TIME / FADE_PERIOD);
//
//        if (null != mAudioTrack) {
//            release();
//        }
//        DebugLog.LogD(TAG, "createAudio || mStreamType = " + mStreamType + ", buffer size: " + mBufferSize);
//        // 创建
//        mAudioTrack = new AudioTrack(mStreamType, rate,
//                AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                AudioFormat.ENCODING_PCM_16BIT, mBufferSize * 2,
//                AudioTrack.MODE_STREAM);
//        mBuffer.setAudioTrackBuffSize(mBufferSize * 2);
//        if (mBufferSize == AudioTrack.ERROR_BAD_VALUE
//                || mBufferSize == AudioTrack.ERROR) {
//            throw new Exception();
//        }
//
////		AudioManager audioManager = (AudioManager) (mContext.getSystemService(Context.AUDIO_SERVICE));
////		audioManager.setMode(AudioManager.MODE_NORMAL);
////		audioManager.setStreamVolume(mStreamType,audioManager.getStreamVolume(mStreamType),0);
//        DebugLog.LogD(TAG, "createAudio end");
//    }
//
//    /**
//     * 释放播放器。
//     */
//    public void release() {
//        synchronized (mAudioLock) {
//            if (null != mAudioTrack) {
//                if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
//                    mAudioTrack.stop();
//                }
//                mAudioTrack.release();
//                mAudioTrack = null;
//            }
//            DebugLog.LogD(TAG, "mAudioTrack released");
//        }
//    }
//
//    /**
//     * AudioTrack检查
//     *
//     * @throws Exception
//     */
//    private void prepAudioPlayer() throws Exception {
//        PcmThread pcmThread = mThread;
//
//        if (null == mAudioTrack
//                || (null != pcmThread && pcmThread.getSreamType() != mStreamType)) {
//            DebugLog.LogD(TAG, "prepAudioPlayer || audiotrack is null or stream type is change.");
//            // 创建AudioTrack
//            createAudio();
//
//            if (null != pcmThread) {
//                pcmThread.setStreamType(mStreamType);
//            }
//        }
//    }
//
//
//    private class PcmThread extends Thread {
//        private int mCurrentStreamType = mStreamType;
//
//        public int getSreamType() {
//            return mCurrentStreamType;
//        }
//
//        public void setStreamType(final int streamType) {
//            mCurrentStreamType = streamType;
//        }
//
//        @Override
//        public void run() {
//            try {
//                DebugLog.LogD(TAG, "start player");
//                // 开始播放，暂停后台音乐播放
//                DebugLog.LogD(TAG, "mAudioFocus= " + mAudioFocus);
//                if (mAudioFocus) {
//                    FuncAdapter.Lock(mContext, mRequestFocus, afChangeListener);
//                } else {
//                    FuncAdapter.Lock(mContext, mRequestFocus, null);
//                }
//                mBuffer.beginRead();
//                // 防止外部已调用停止接口
//                synchronized (mSyncObj) {
//                    if (mPlaytate != STOPED && mPlaytate != PAUSED)
//                        mPlaytate = PLAYING;
//                }
//
//                startFadeIn();
//                while (true) {
//                    prepAudioPlayer();
//                    if (mPlaytate == PLAYING || mPlaytate == BUFFERING || mFading) {
//
//                        //判断 缓冲内容是否可读
//                        if (mBuffer.playAble()) {
//                            if (setState(BUFFERING, PLAYING)) {
//                                Message.obtain(mUihandler, MSG_RESUME).sendToTarget();
//
//                                DebugLog.LogD("BUFFERING to PLAYING  fading ");
//                                startFadeIn();
//                            }
//
//                            int percent = mBuffer.getPlayPercent();
//                            PcmBuffer.AudioInfo info = mBuffer.getPalyAudioInfo();
//
//                            if (info != null) {
//                                mCurEndPos = info.mEndIndex;
//                                Log.i(TAG, "onSpeakProgress++++：" + "起始位置beginPos：" + info.mBegIndex + "，结束位置：" + info.mEndIndex);
//                                Message.obtain(mUihandler, MSG_PERCENT, percent, info.mBegIndex).sendToTarget();
//                            }
//
//                            if (mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
//                                mAudioTrack.play();
//                            }
//
//                            if (mBufferingFadingEnable) {
//                                if (!mBuffer.isBufferingFinished()
//                                        && !mBuffer.hasMoreBuffer(mFadingSize)
//                                        && !(Math.abs(mTargetVol - MIN_VOL) < PER)) {
//                                    DebugLog.LogD("no more size  fading ");
//                                    startFadeOut();
//                                } else if (PLAYING == mPlaytate
//                                        && (mBuffer.isBufferingFinished() || mBuffer.hasMoreBuffer(mFadingSize))
//                                        && !(Math.abs(mTargetVol - MAX_VOL) < PER)) {
//                                    DebugLog.LogD("has buffer  fading ");
//                                    startFadeIn();
//                                }
//                            }//end of if buffering fading enable
//
//                            if (mFading) {
//                                fading();
//                            }
//
//                            mBuffer.writeTrack(mAudioTrack, mPerPlaySize);
//                        } else if (mBuffer.isOver()) {
//
//                            DebugLog.LogD("play stoped");
//                            int pos = mAudioTrack.getPlaybackHeadPosition();
//                            int total = (int) (mBuffer.getTotalSize() / 2);
////							long time = System.currentTimeMillis();
//                            if (total > pos) {
//                                if (mEndLock.tryLock()) {
//                                    //等待播放结束
//                                    mAudioTrack.setNotificationMarkerPosition(total);
//                                    DebugLog.LogI("PcmPlayer setNotificationMarkerPosition");
//                                    mAudioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
//                                        @Override
//                                        public void onMarkerReached(AudioTrack audioTrack) {
//                                            DebugLog.LogI("PcmPlayer onMarkerReached");
//                                            mEndLock.lock();
//                                            try {
//                                                mEndCondition.signalAll();
//                                            } catch (Exception e) {
//
//                                            } finally {
//                                                mEndLock.unlock();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onPeriodicNotification(AudioTrack audioTrack) {
//
//                                        }
//                                    });
//
//                                    try {
//                                        mEndCondition.await(1000, TimeUnit.MILLISECONDS);
//                                    } catch (InterruptedException ie) {
//                                        DebugLog.LogI("pcmplayer interrupted");
//                                        ie.printStackTrace();
//                                    } finally {
//                                        mEndLock.unlock();
//                                    }
//                                }
//                            }
//                            synchronized (mSyncObj) {
//                                DebugLog.LogI("pcmplayer isover stop:" + mPlaytate);
//                                if (mPlaytate != STOPED) {
//                                    mPlaytate = STOPED;
//                                    Message.obtain(mUihandler, MSG_STOPED).sendToTarget();
//                                }
//                                mFading = false;
////								DebugLog.LogI("audio track delay = " + (System.currentTimeMillis()-time));
//                            }
//                            break;
//                        } else if (mFading) {
//                            mFading = false;
//                        } else {
//                            //如果状态在播放状态，说明播放进度不足100% 异常  停止播放
//                            if (mPlaytate == PLAYING) {
//                                Message.obtain(mUihandler, MSG_ERROR,
//                                        new SpeechError(ErrorCode.ERROR_NET_EXCEPTION)).sendToTarget();
//                                break;
//                            }
//                            if (setState(PLAYING, BUFFERING)) {
//                                DebugLog.LogD("play onpaused!");
//                                Message.obtain(mUihandler, MSG_PAUSE).sendToTarget();
//                            }
//                            sleep(MIN_SLEEP);
//                        }
//                    } else if (mPlaytate == PAUSED) {
//                        if (AudioTrack.PLAYSTATE_PAUSED != mAudioTrack.getPlayState()) {
//                            mAudioTrack.pause();
//                            DebugLog.LogD("pause done");
//                            Message.obtain(mUihandler, MSG_PAUSE).sendToTarget();
//                            if (mFading) {
//                                setSilence();
//                            }
//                        }
//                        sleep(MIN_SLEEP);
//                    } else if (STOPED == mPlaytate) {
//                        setSilence();
//                        break;
//                    }
//                }//end of while not stoped
//
//                if (null != mAudioTrack) {
//                    mAudioTrack.stop();
//                }
//            } catch (Exception e) {
//                DebugLog.LogE(e);
//                Message.obtain(mUihandler, MSG_ERROR, new SpeechError(ErrorCode.ERROR_PLAY_MEDIA)).sendToTarget();
//            } finally {
//                synchronized (mSyncObj) {
//                    mPlaytate = STOPED;
//                }
//                if (mAudioTrack != null) {
//                    mAudioTrack.release();
//                    mAudioTrack = null;
//                }
//
//                //完成播放，恢复后台音乐播放
//                if (mAudioFocus) {
//                    FuncAdapter.UnLock(mContext, mRequestFocus, afChangeListener);
//                } else {
//                    FuncAdapter.UnLock(mContext, mRequestFocus, null);
//                }
//                mThread = null;
//
//                DebugLog.LogD(TAG, "player stopped");
//            }
//        }
//    }
//
//    ;
//
//    private int mCurEndPos = 0;
//
//    private Handler mUihandler = new Handler(Looper.getMainLooper()) {
//
//        public void handleMessage(Message msg) {
//            if (null == mListener) {
//                return;
//            }
//            switch (msg.what) {
//                case MSG_ERROR:
//                    mListener.onError((SpeechError) msg.obj);
//                    mListener = null;
//                    break;
//                case MSG_PAUSE:
//                    mListener.onPaused();
//                    break;
//                case MSG_RESUME:
//                    mListener.onResume();
//                    break;
//                case MSG_PERCENT:
//                    mListener.onPercent(msg.arg1, msg.arg2, mCurEndPos);
//                    break;
//                case MSG_STOPED:
//                    mListener.onStoped();
//                    mListener = null;
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    public boolean pause() {
//        if (mPlaytate == STOPED || mPlaytate == PAUSED) {
//            return false;
//        }
//
//        DebugLog.LogD("pause start fade out");
//        startFadeOut();
//        mPlaytate = PAUSED;
//        return true;
//    }
//
//
//    public boolean play(PcmBuffer buffer, PcmPlayerListener listener) {
//        DebugLog.LogD(TAG, "play mPlaytate= " + mPlaytate + ",mAudioFocus= " + mAudioFocus);
//
//        boolean ret = true;
//        synchronized (mSyncObj) {
//            if (mPlaytate != STOPED &&
//                    mPlaytate != INIT && mPlaytate != PAUSED && null != mThread) {
//                ret = false;
//            } else {
//                mBuffer = buffer;
//                mListener = listener;
//                mThread = new PcmThread();
//                mThread.start();
//            }
//        }//end of synchronized
//
//        return ret;
//    }
//
//
//    public boolean rePlay(PcmBuffer buffer, PcmPlayerListener listener) {
//        setState(INIT);
//        return play(buffer, listener);
//    }
//
//    public boolean resume() {
//        final boolean ret = setState(PAUSED, PLAYING);
//        //继续播放时重新主动获取焦点
//        FuncAdapter.Lock(mContext, mRequestFocus, afChangeListener);
//        if (ret) {
//            DebugLog.LogD("resume start fade in");
//            Message.obtain(mUihandler, MSG_RESUME).sendToTarget();
//            startFadeIn();
//        }
//
//        return ret;
//    }
//
//    public void stop() {
//        if (STOPED != mPlaytate) {
//            DebugLog.LogD("stop start fade out");
//            startFadeOut();
//        }
//
//        synchronized (mSyncObj) {
//            mPlaytate = STOPED;
//        }
//    }
//
//    public void startFadeIn() {
//        if (mFadingEnable) {
//            synchronized (mSyncObj) {
//                DebugLog.LogD("start fade in");
//                mFading = true;
//                mTargetVol = MAX_VOL;
//                mCurFadingPeriod = PER;
//            }//end of sync
//        }
//    }
//
//    public void startFadeOut() {
//        if (mFadingEnable) {
//            synchronized (mSyncObj) {
//                DebugLog.LogD("start fade out");
//                mFading = true;
//                mTargetVol = MIN_VOL;
//                mCurFadingPeriod = -PER;
//            }//end of sync
//        }
//    }
//
//    public void fading() {
//        if (mFadingEnable) {
//            synchronized (mSyncObj) {
//                if (Math.abs(mTargetVol - mCurVol) < PER) {
//                    mCurVol = mTargetVol;
//                    mFading = false;
//                    DebugLog.LogD("fading finish");
//                } else {
//                    mCurVol += mCurFadingPeriod;
//                }
//            }//end of sync
//
//            mAudioTrack.setStereoVolume(mCurVol, mCurVol);
//        } else {
//            mFading = false;
//        }
//    }
//
//    public void setSilence() {
//        DebugLog.LogD("fading set silence");
//        synchronized (mSyncObj) {
//            if (Math.abs(MIN_VOL - mTargetVol) < PER) {
//                mCurVol = MIN_VOL;
//                mFading = false;
//            }
//        }//end of sync
//
//        mAudioTrack.setStereoVolume(mCurVol, mCurVol);
//    }
//
//    public abstract interface PcmPlayerListener {
//        /**
//         * 错误信息，网络连接或服务繁忙时触发
//         */
//        void onError(SpeechError error);
//
//        /**
//         * Audio not ready,has to wait
//         */
//        void onPaused();
//
//        /**
//         * Audio resumed
//         */
//        void onResume();
//
//        /**
//         * Audio play percent
//         */
//        void onPercent(int percent, int beginPos, int endPos);
//
//        /**
//         * Audio play over
//         */
//        void onStoped();
//
//    }
//}
