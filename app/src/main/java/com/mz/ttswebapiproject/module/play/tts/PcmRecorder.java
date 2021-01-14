//package com.mz.ttswebapiproject.play;
//
//import java.io.IOException;
//
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//
//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.msc.util.log.DebugLog;
//
///**
// * PCM Recoder
// *
// * @author iflytek
// *
// */
//public class PcmRecorder extends Thread{
//	/**
//	 * 用于录音数据回调的接口
//	 *
//	 * @author iflytek
//	 *
//	 */
//	public interface PcmRecordListener {
//		/**
//		 * record callback
//		 *
//		 * @param dataBuffer audio data pcm format
//		 * @throws IOException
//		 */
//		void onRecordBuffer(byte[] dataBuffer, int offset, int length);
//		void onError(SpeechError e);
//		void onRecordStarted(boolean success);
//		void onRecordReleased();
//	}
//
//	private final short DEFAULT_BIT_SAMPLES = 16;
//	private static final int RECORD_BUFFER_TIMES_FOR_FRAME = 4;
//	private static final short DEFAULT_CHANNELS = 1;
//
//
//	private byte[] mDataBuffer = null;
//	private AudioRecord mRecorder = null;
//	private PcmRecordListener mOutListener = null;
//	//停止录音的对外回调
//	private PcmRecordListener mStopListener = null;
//	// 是否退出
//	private volatile boolean exit = false;
//
//	// 录音数据检查
//	private double checkDataSum = 0L;
//	private double checkStandDev = 0L;
//
//	// 默认录音采样率，16K
//	public static final int RATE16K = 16000;
//	public static final int READ_INTERVAL40MS = 40;
//	private int mRate = RATE16K;
//	// 录音回调频度，外部传入间隔
//	private int mInterval = READ_INTERVAL40MS;
//	// 读取录音频度，线程中使用
//	private int mReadInterval = READ_INTERVAL40MS;
//
//	private int mAudioSource;
//	/**
//	 *
//	 * @throws Exception
//	 */
//	public PcmRecorder(int sampleRate,int timeInterval) {
//		this(sampleRate, timeInterval, MediaRecorder.AudioSource.MIC);
//	}
//	public PcmRecorder(int sampleRate,int timeInterval,int audioSource) {
//		mAudioSource=audioSource;
//		mRate = sampleRate;
//		mInterval = timeInterval;
//		// 录音读取频度范围控制在40-100之间
//		if(mInterval < READ_INTERVAL40MS || mInterval > 100)
//			mInterval = READ_INTERVAL40MS;
//		// 读取频度不得大于50，否则会造成退出延迟问题
//		mReadInterval = 10;
//	}
//
//	/**
//	 *
//	 * @param channels 音频通道
//	 * @param sampleRate 采样率
//	 * @param timeInterval 间隔时间
//	 * @return
//	 * @throws SpeechError
//	 */
//	protected void initRecord(short channels, int sampleRate,int timeInterval)
//			throws SpeechError {
//		if (mRecorder != null) {
//			DebugLog.LogD("[initRecord] recoder release first");
//			release();
//		}
//
//		short bitSamples = DEFAULT_BIT_SAMPLES;
//		int framePeriod = sampleRate * timeInterval / 1000;
//
//		int recordBufferSize = framePeriod * RECORD_BUFFER_TIMES_FOR_FRAME * bitSamples * channels / 8;
//
//		int channelConfig = (channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO
//				: AudioFormat.CHANNEL_CONFIGURATION_STEREO);
//		int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//
//		int min = AudioRecord.getMinBufferSize(sampleRate, channelConfig,
//				audioFormat);
//		if (recordBufferSize < min) {
//			recordBufferSize = min;
//		}
//
//		mRecorder = new AudioRecord(mAudioSource,
//				sampleRate, channelConfig, audioFormat, recordBufferSize);
//
//		mDataBuffer = new byte[framePeriod * channels * bitSamples / 8];
//
//
//		DebugLog.LogD("\nSampleRate:" + sampleRate + "\nChannel:"
//				+ channelConfig + "\nFormat:" + audioFormat
//				+ "\nFramePeriod:" + framePeriod + "\nBufferSize:"
//				+ recordBufferSize + "\nMinBufferSize:" + min
//				+ "\nActualBufferSize:" + mDataBuffer.length + "\n");
//
//		if (mRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
//			DebugLog.LogD("create AudioRecord error");
//			throw new SpeechError(ErrorCode.ERROR_AUDIO_RECORD);
//		}
//	}
//
//	/**
//	 * @throws SpeechError
//	 * @throws IOException
//	 *
//	 */
//	private int readRecordData() throws SpeechError {
//		int count = 0;
//		if (mRecorder == null || mOutListener == null)
//			return 0;
//		count = mRecorder.read(mDataBuffer, 0, mDataBuffer.length);
//		if (count > 0 && mOutListener != null) {
//			//			DebugLog.Log("Record read data = " + mDataBuffer.length + " real=" + count);
//			mOutListener.onRecordBuffer(mDataBuffer,0,count);
//		}else if( 0 > count ){
//			DebugLog.LogE( "Record read data error: "+count );
//			throw( new SpeechError(ErrorCode.ERROR_AUDIO_RECORD) );
//		}
//
//		return count;
//	}
//
//	/**
//	 * 通过计算每帧音频的标准差来检查音频
//	 * @param pcmData
//	 * @param length
//	 * @return
//	 */
//	private double checkAudio(byte[] pcmData, int length) {
//		if (null == pcmData || length <= 0) {
//			return 0;
//		}
//		// 获取音频数据平均值
//		double dataAvg = 0l;
//		double dataSum = 0l;
//		for(byte data : pcmData)
//			dataSum += data;
//		dataAvg = dataSum/pcmData.length;
//		// 计算数据与平均值的离差平方和
//		double frameSum = 0l;
//		for(byte data : pcmData) {
//			frameSum += Math.pow(data - dataAvg, 2);
//		}
//		// 返回每帧数据标准差
//		return Math.sqrt(frameSum/(pcmData.length-1));
//	}
//
//	public void stopRecord(boolean forceStop)
//	{
//		exit = true;
//		if(null == mStopListener)
//			mStopListener = mOutListener;
//		mOutListener = null;
//
//		if(forceStop)
//		{
//			synchronized(this)
//			{
//				try {
//					DebugLog.LogD("stopRecord...release");
//					if (mRecorder != null)
//					{
//						if(AudioRecord.RECORDSTATE_RECORDING == mRecorder.getRecordingState()
//								&& AudioRecord.STATE_INITIALIZED == mRecorder.getState())
//						{
//							DebugLog.LogD("stopRecord releaseRecording ing...");
//							mRecorder.release();
//							DebugLog.LogD("stopRecord releaseRecording end...");
//							mRecorder = null;
//						}
//
//						if(null != mStopListener)
//						{
//							mStopListener.onRecordReleased();
//							mStopListener = null;
//						}
//					}
//				} catch (Exception e) {
//					DebugLog.LogE(e.toString());
//				}
//			}
//		}
//		DebugLog.LogD("stop record");
//	}
//
//	/**
//	 * @throws SpeechError
//	 *
//	 */
//	public void startRecording(PcmRecordListener listener) throws SpeechError
//	{
//		mOutListener = listener;
//		setPriority(MAX_PRIORITY);
//		start();
//	}
//	@Override
//	public void run() {
//		try {
//			// 初始化重试10次
//			int initCount = 0;
//			while(!exit)
//			{
//				try {
//					initRecord(DEFAULT_CHANNELS, mRate,mInterval);
//					//成功则打断while
//					break;
//				} catch (Exception e1) {
//					initCount++;
//					if(initCount < 10)
//						sleep(READ_INTERVAL40MS);
//					else
//						throw new SpeechError(ErrorCode.ERROR_AUDIO_RECORD);
//				}
//			}
//			// 录音机开启重试10次
//			initCount = 0 ;
//			while(!exit)
//			{
//				try {
//					//long time = SystemClock.elapsedRealtime();
//					mRecorder.startRecording();
//					//DebugLog.LogD("timecost :"+(SystemClock.elapsedRealtime() - time));
//					//如果Mic被占用提示初始化失败.
//					if (mRecorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING)
//					{
//						DebugLog.LogE("recorder state is not recoding");
//						throw new SpeechError(ErrorCode.ERROR_AUDIO_RECORD);
//					}
//					//成功则打断while
//					break;
//				} catch (Exception e1) {
//					initCount++;
//					if(initCount < 10)
//						sleep(READ_INTERVAL40MS);
//					else{
//						DebugLog.LogE("recoder start failed");
//						throw new SpeechError(ErrorCode.ERROR_AUDIO_RECORD);
//					}
//
//				}
//			}
//
//			// 录音机开启成功
//			if(null != mOutListener)
//				mOutListener.onRecordStarted(true);
//
//			final int CHECK_TIME = 1000;
//			// 录音机计时器及数据检测标示
//			long timeStep = System.currentTimeMillis();
//			boolean checkAudio = true;
//			// 读取录音机数据
//			while(!exit)
//			{
//				int count = readRecordData();
//
//				if (checkAudio) {
//					checkDataSum += count;
//					checkStandDev += checkAudio(mDataBuffer, mDataBuffer.length);
//
//					// 检查数据
//					if ( System.currentTimeMillis()-timeStep >= CHECK_TIME) {
//						// 检测1s内数据是否有效，无效则退出录音机
//						checkAudio = false;
//						if (checkDataSum == 0 || checkStandDev == 0) {
//							DebugLog.LogE("cannot get record permission, get invalid audio data.");
//							throw new SpeechError(ErrorCode.ERROR_AUDIO_RECORD);
//						}
//					}
//				}
//
//				if( mDataBuffer.length > count ){
//					DebugLog.LogI( "current record read size is less than buffer size: "+count );
//					sleep(mReadInterval);//音频采用回调方式取消每次停40ms
//				}
//			}
//		} catch (Exception e) {
//			DebugLog.LogE(e);
//			if(mOutListener != null)
//				mOutListener.onError(new SpeechError(ErrorCode.ERROR_AUDIO_RECORD));
//		}
//		// 释放录音机
//		release();
//	}
//
//	@Override
//	protected void finalize() throws Throwable {
//		DebugLog.LogD("[finalize] release recoder");
//		release();
//		super.finalize();
//	}
//	/**
//	 *
//	 */
//	private void release() {
//		synchronized(this)
//		{
//			try {
//				if (mRecorder != null) {
//					DebugLog.LogD("release record begin");
//					mRecorder.release();
//					mRecorder = null;
//					if(null != mStopListener)
//					{
//						mStopListener.onRecordReleased();
//						mStopListener = null;
//					}
//					DebugLog.LogD("release record over");
//				}
//			} catch (Exception e) {
//				DebugLog.LogE(e.toString());
//			}
//		}
//	}
//}
