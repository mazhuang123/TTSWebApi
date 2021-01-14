//package com.mz.ttswebapiproject.play;
//
//import com.iflytek.cloud.msc.util.log.DebugLog;
//import com.iflytek.cloud.resource.Resource;
//
///**
// * <h3>错误信息类</h3>
// * <p>
// * 当业务会话出现错误时，通过onError函数，返回类对象。
// * </p>
// * <p>
// * 应用层可通过此类获取错误描述信息，以及具体的错误码。应用层只使用
// * 回调返回的现成对象，非必要情况下，不必在应用层实例化本类对象。
// * </p>
// *
// * @author <script>gShowAuthor();</script>
// * @see ErrorCode
// * @since version:1073
// */
//public class SpeechError extends Exception {
//
//    private static final long serialVersionUID = 4434424251478985596L;
//
//    /**
//     * 没有检查到网络
//     */
//    protected static final int TIP_NO_NETWORK = 1;
//    /**
//     * 获取结果超时
//     */
//    protected static final int TIP_RESULT_TIMEOUT = 2;
//    /**
//     * 网络连接发生异常
//     */
//    protected static final int TIP_ERROR_NET_EXPECTION = 3;
//    /**
//     * 未设置有效权限
//     */
//    protected static final int TIP_ERROR_INSUFFICIENT_PERMISSIONS = 4;
//    /**
//     * 无法解析的结果
//     */
//    protected static final int TIP_ERROR_INVALID_RESULT = 5;
//    /**
//     * 无法连接服务,请检查网络
//     */
//    protected static final int TIP_ERROR_SERVER_CONNECT = 6;
//    /**
//     * 无效的参数
//     */
//    protected static final int TIP_ERROR_INVALID_PARAM = 7;
//    /**
//     * 发生未知错误
//     */
//    protected static final int TIP_ERROR_CLIENT = 8;
//    /**
//     * 启动录音失败
//     */
//    protected static final int TIP_ERROR_AUDIO_RECORD = 9;
//    /**
//     * 没有匹配的结果
//     */
//    protected static final int TIP_ERROR_NO_MATCH = 10;
//    /**
//     * 您好像没有说话哦
//     */
//    protected static final int TIP_ERROR_SPEECH_TIMEOUT = 11;
//    /**
//     * 无法支持的编码格式
//     */
//    protected static final int TIP_ERROR_INVALID_ENCODING = 12;
//    /**
//     * 无有效的文本信息
//     */
//    protected static final int TIP_ERROR_EMPTY_UTTERANCE = 13;
//    /**
//     * 无法读写文件
//     */
//    protected static final int TIP_ERROR_FILE_ACCESS = 14;
//    /**
//     * 播放音频错误
//     */
//    protected static final int TIP_ERROR_PLAY_MEDIA = 15;
//    /**
//     * 内存不足
//     */
//    protected static final int TIP_ERROR_MEMORY_WRANING = 16;
//    /**
//     * 文本过长
//     */
//    protected static final int TIP_ERROR_TEXT_OVERFLOW = 17;
//    /**
//     * 用户校验失败
//     */
//    protected static final int TIP_ERROR_LOGIN = 18;
//    /**
//     * 网络繁忙，请稍候
//     */
//    protected static final int TIP_ERROR_IN_USE = 19;
//    /**
//     * 上传数据格式不正确
//     */
//    protected static final int TIP_ERROR_INVALID_DATA = 20;
//    /**
//     * 未找到有效的语法文件
//     */
//    protected static final int TIP_ERROR_INVALID_GRAMMAR = 21;
//    /**
//     * 无法找到本地资源
//     */
//    protected static final int TIP_ERROR_INVALID_LOCAL_RESOURCE = 22;
//    /**
//     * 无效的用户名
//     */
//    protected static final int TIP_ERROR_LOGIN_INVALID_USER = 23;
//    /**
//     * 密码错误
//     */
//    protected static final int TIP_ERROR_LOGIN_INVALID_PWD = 24;
//    /**
//     * 未经授权的语音应用
//     */
//    protected static final int TIP_ERROR_PERMISSION_DENIED = 25;
//    /**
//     * 系统未安装浏览器，请安装后再查看
//     */
//    protected static final int TIP_ERROR_BROWSER_NOT_INSTALLED = 26;
//    /**
//     * 引擎繁忙，被打断
//     */
//    protected static final int TIP_ERROR_INTERRUPT = 27;
//    /**
//     * 引擎初始化失败
//     */
//    protected static final int TIP_ERROR_ENGINE_INIT_FAIL = 28;
//    /**
//     * 引擎未安裝
//     */
//    protected static final int TIP_ENGINE_NOT_INSTALLED = 29;
//    /**
//     * 本地引擎发生错误
//     */
//    protected static final int TIP_LOCAL_ENGINE_ERROR = 30;
//    /**
//     * 脚本运行错误
//     */
//    protected static final int TIP_SCRIPT_ERROR = 31;
//    /**
//     * 无效的授权
//     */
//    protected static final int TIP_INVALID_AUTHORIZATION = 32;
//    /**
//     * 您好像没有传图片哦
//     */
//    protected static final int TIP_ERROR_NO_PICTURE = 33;
//    /**
//     * 您的图片中未检测到内容
//     */
//    protected static final int TIP_ERROR_NO_CONTENT = 34;
//    /**
//     * 无语音或音量太小
//     */
//    protected static final int TIP_ERROR_SILENT_OR_LOW_VOLUME = 35;
//    /**
//     * 信噪比低或有效语音过短
//     */
//    protected static final int TIP_ERROR_NOISY_OR_SHORT_AUDIO = 36;
//    /**
//     * 非试卷数据
//     */
//    protected static final int TIP_ERROR_NOT_PAPER_DATA = 37;
//    /**
//     * 试卷内容有误
//     */
//    protected static final int TIP_ERROR_WRONG_PAPER_CONTENT = 38;
//    /**
//     * 录音格式有误
//     */
//    protected static final int TIP_ERROR_WRONG_AUDIO_FORMAT = 39;
//    /**
//     * 评测数据异常
//     */
//    protected static final int TIP_ERROR_OTHER_DATA_EXCEPTION = 40;
//    /**
//     * 试卷格式有误
//     */
//    protected static final int TIP_ERROR_WRONG_PAPER_FORMAT = 41;
//    /**
//     * 存在未登录词
//     */
//    protected static final int TIP_ERROR_EXIST_UNLISTED_WORD = 42;
//    /**
//     * 无人脸
//     */
//    protected static final int TIP_ERROR_NOT_FACE_IMAGE = 43;
//    /**
//     * 人脸向左偏
//     */
//    protected static final int TIP_ERROR_FACE_IMAGE_FULL_LEFT = 44;
//    /**
//     * 人脸向右偏
//     */
//    protected static final int TIP_ERROR_FACE_IMAGE_FULL_RIGHT = 45;
//    /**
//     * 人脸顺时针旋转
//     */
//    protected static final int TIP_ERROR_IMAGE_CLOCKWISE_WHIRL = 46;
//    /**
//     * 逆时针旋转
//     */
//    protected static final int TIP_ERROR_IMAGE_COUNTET_CLOCKWISE_WHIRL = 47;
//    /**
//     * 图片尺寸错误
//     */
//    protected static final int TIP_ERROR_VALID_IMAGE_SIZE = 48;
//    /**
//     * 光照异常
//     */
//    protected static final int TIP_ERROR_ILLUMINATION = 49;
//    /**
//     * 人脸被遮挡
//     */
//    protected static final int TIP_ERROR_FACE_OCCULTATION = 50;
//    /**
//     * 非法模型数据
//     */
//    protected static final int TIP_ERROR_FACE_INVALID_MODEL = 51;
//    /**
//     * 输入数据类型非法
//     */
//    protected static final int TIP_ERROR_FUSION_INVALID_INPUT_TYPE = 52;
//    /**
//     * 输入的数据不完整
//     */
//    protected static final int TIP_ERROR_FUSION_NO_ENOUGH_DATA = 53;
//    /**
//     * 输入的数据过多
//     */
//    protected static final int TIP_ERROR_FUSION_ENOUGH_DATA = 54;
//    /**
//     * 内核异常
//     */
//    public static final int TIP_ERROR_IVP_GENERAL = 55;
//    /**
//     * rgn超过最大支持次数9
//     */
//    public static final int TIP_ERROR_IVP_EXTRA_RGN_SOPPORT = 56;
//    /**
//     * 音频波形幅度太大，超出系统范围，发生截幅
//     */
//    public static final int TIP_ERROR_IVP_TRUNCATED = 57;
//    /**
//     * 太多噪音
//     */
//    public static final int TIP_ERROR_IVP_MUCH_NOISE = 58;
//    /**
//     * 声音太小
//     */
//    public static final int TIP_ERROR_IVP_TOO_LOW = 59;
//    /**
//     * 没检测到音频
//     */
//    public static final int TIP_ERROR_IVP_ZERO_AUDIO = 60;
//    /**
//     * 音频太短
//     */
//    public static final int TIP_ERROR_IVP_UTTER_TOO_SHORT = 61;
//    /**
//     * 音频内容与给定文本不一致
//     */
//    public static final int TIP_ERROR_IVP_TEXT_NOT_MATCH = 62;
//    /**
//     * 音频长度达不到自由说的要求
//     */
//    public static final int TIP_ERROR_IVP_NO_ENOUGH_AUDIO = 63;
//    /**
//     * 模型数据不存在
//     */
//    public static final int TIP_ERROR_MODEL_NOT_FOUND = 64;
//    /**
//     * 模型数据正在生成
//     */
//    public static final int TIP_ERROR_MODEL_IS_CREATING = 65;
//    /**
//     * 模型或记录已存在
//     */
//    public static final int TIP_ERROR_ALREADY_EXIST = 66;
//    /**
//     * 组不存在，未创建
//     */
//    public static final int TIP_ERROR_NO_GROUP = 67;
//    /**
//     * 空组
//     */
//    public static final int TIP_ERROR_GROUP_EMPTY = 68;
//    /**
//     * 组内没有该用户
//     */
//    public static final int TIP_ERROR_NO_USER = 69;
//    /**
//     * 用户数量超过组上限或者组创建数量超过上限
//     */
//    public static final int TIP_ERROR_OVERFLOW_IN_GROUP = 70;
//
//    public static final int TIP_ERROR_AUTHID_NOT_AVAILABLE = 71;
//
//
//    // error code,for check reason
//    private int mErrorCode = 0;
//    // error description
//    private String mDescription = "";
//
//    public SpeechError(Exception e) {
//        super();
//        mErrorCode = ErrorCode.ERROR_UNKNOWN;
//        mDescription = e.toString();
//    }
//
//    public SpeechError(Throwable cause, int errorCode) {
//        this(errorCode);
//        this.initCause(cause);
//    }
//
//    public SpeechError(int errorCode, String engineType) {
//        this(errorCode);
//        if (SpeechConstant.ENG_WFR.equals(engineType)) {
//            if (ErrorCode.MSP_ERROR_NO_DATA == errorCode) {
//                mDescription = Resource.getErrorDescription(SpeechError.TIP_ERROR_NO_PICTURE);
//            } else if (ErrorCode.MSP_ERROR_NO_MORE_DATA == errorCode) {
//                mDescription = Resource.getErrorDescription(SpeechError.TIP_ERROR_NO_CONTENT);
//            }
//        }
//    }
//
//    /**
//     * 初始化SpeechError对象
//     * 映射errorCode到errorDescription上.
//     *
//     * @param errorCode 错误码
//     */
//    public SpeechError(int errorCode) {
//        super();
//        mErrorCode = errorCode;
//        int errorType = TIP_ERROR_NET_EXPECTION;
//        /*
//         * 1开头错误码为MSC或服务器返回错误,需要排查msc.log和结构化日志.
//         */
//        if (errorCode < ErrorCode.ERROR_NO_NETWORK) {
//            if (mErrorCode == ErrorCode.MSP_ERROR_NO_DATA) {
//                /** 没有说话 */
//                errorType = TIP_ERROR_SPEECH_TIMEOUT;
//            } else if (ErrorCode.MSP_ERROR_INVALID_PARA == mErrorCode
//                    || ErrorCode.MSP_ERROR_INVALID_PARA_VALUE == mErrorCode
//                    || ErrorCode.MSP_ERROR_INVALID_CONFIG == mErrorCode) {
//                /** 无效的参数*/
//                DebugLog.LogD("sdk errorcode", mErrorCode + "");
//                errorType = TIP_ERROR_INVALID_PARAM;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_NO_LICENSE) {
//                /** 无效的授权 */
//                errorType = TIP_INVALID_AUTHORIZATION;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_NOT_INIT) {
//                /** 未初始化 */
//                errorType = TIP_ERROR_ENGINE_INIT_FAIL;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_NET_GENERAL
//                    && mErrorCode < ErrorCode.MSP_ERROR_MSG_GENERAL) {
//                /** 网络连接异常*/
//                errorType = TIP_ERROR_NET_EXPECTION;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_NO_ENOUGH_BUFFER
//                    || mErrorCode == ErrorCode.MSP_ERROR_OUT_OF_MEMORY) {
//                /** 内存溢出*/
//                errorType = TIP_ERROR_MEMORY_WRANING;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_OVERFLOW) {
//                /** 文本溢出(注：MSC v5.1039之前版本该错误意为"网络繁忙10129错误") */
//                errorType = TIP_ERROR_TEXT_OVERFLOW;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_NOT_FOUND) {
//                /** 未找到 */
//                errorType = TIP_ERROR_MODEL_NOT_FOUND;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
//                /** 已存在 */
//                errorType = TIP_ERROR_ALREADY_EXIST;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_DB_GENERAL
//                    && mErrorCode <= ErrorCode.MSP_ERROR_DB_INVALID_APPID) {
//                /** 登录校验错误*/
//                errorType = TIP_ERROR_LOGIN;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_LOGIN_SUCCESS
//                    && mErrorCode < ErrorCode.MSP_ERROR_LOGIN_SYSTEM_ERROR) {
//                /** 无效的用户名*/
//                if (mErrorCode == ErrorCode.MSP_ERROR_LOGIN_INVALID_USER)
//                    errorType = TIP_ERROR_LOGIN_INVALID_USER;
//                /** 无效的密码*/
//                else if (mErrorCode == ErrorCode.MSP_ERROR_LOGIN_INVALID_PWD)
//                    errorType = TIP_ERROR_LOGIN_INVALID_PWD;
//                /** 登录错误*/
//                else
//                    errorType = TIP_ERROR_LOGIN;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_CREATE_HANDLE) {
//                /** 前一次会话未结束，又开启一次新的会话*/
//                errorType = TIP_ERROR_IN_USE;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_INVALID_DATA) {
//                /** 无效的数据*/
//                errorType = TIP_ERROR_INVALID_DATA;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_REC_GRAMMAR_ERROR) {
//                /** 识别语法文件错误 */
//                errorType = TIP_ERROR_INVALID_GRAMMAR;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_RES_GENERAL
//                    && mErrorCode < ErrorCode.MSP_ERROR_TTS_GENERAL) {
//                /** 资源丢失，加载内容错误*/
//                errorType = TIP_ERROR_INVALID_LOCAL_RESOURCE;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_AUTH_NO_LICENSE
//                    && mErrorCode <= ErrorCode.MSP_ERROR_AUTH_ERROR_END) {
//                /** 授权校验错误（网络每日授权次数限制，本地合成校验次数限制）*/
//                errorType = TIP_ERROR_PERMISSION_DENIED;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_LUA_BASE
//                    && mErrorCode <= ErrorCode.MSP_ERROR_LUA_INVALID_PARAM) {
//                /** 脚本运行错误 */
//                errorType = TIP_SCRIPT_ERROR;
//            } else if (mErrorCode >= ErrorCode.MSP_ERROR_LMOD_BASE
//                    && mErrorCode <= ErrorCode.MSP_ERROR_LMOD_ALREADY_LOADED) {
//                /** 脚本运行错误 */
//                errorType = TIP_SCRIPT_ERROR;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_SILENCE == mErrorCode) {
//                /** 无语音或音量太小 */
//                errorType = TIP_ERROR_SILENT_OR_LOW_VOLUME;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_SNRATIO == mErrorCode) {
//                /** 信噪比低或有效语音过短 */
//                errorType = TIP_ERROR_NOISY_OR_SHORT_AUDIO;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_PAPERDATA == mErrorCode) {
//                /** 非试卷数据 */
//                errorType = TIP_ERROR_NOT_PAPER_DATA;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_PAPERCONTENTS == mErrorCode) {
//                /** 试卷内容有误 */
//                errorType = TIP_ERROR_WRONG_PAPER_CONTENT;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_NOTMONO == mErrorCode) {
//                /** 录音格式有误 */
//                errorType = TIP_ERROR_WRONG_AUDIO_FORMAT;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_OTHERS == mErrorCode) {
//                /** 其他评测数据异常，包括错读、漏读、恶意录入、试卷内容等错误 */
//                errorType = TIP_ERROR_OTHER_DATA_EXCEPTION;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_PAPERFMT == mErrorCode) {
//                /** 试卷格式有误 */
//                errorType = TIP_ERROR_WRONG_PAPER_FORMAT;
//            } else if (ErrorCode.MSP_ERROR_ASE_EXCEP_ULISTWORD == mErrorCode) {
//                /** 存在未登录词*/
//                errorType = TIP_ERROR_EXIST_UNLISTED_WORD;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_IVW_MODEL_TRAINING) {
//                /** 模型资源正在生成中*/
//                errorType = TIP_ERROR_MODEL_IS_CREATING;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_IVW_MODEL_NO_FOUND) {
//                /** 无新模型资源 */
//                errorType = TIP_ERROR_MODEL_NOT_FOUND;
//            } else if (mErrorCode == ErrorCode.MSP_ERROR_IVW_BUSY) {
//                /** 查询服务繁忙*/
//                errorType = TIP_ERROR_IN_USE;
//            }
//        }
//        /*
//         * 除1开头的错误码之外，2开头错误码为SDK返回错误码，errorType在SDK内部已设置.
//         * 需要排查LOGCAT日志.
//         */
//        else if (mErrorCode < 30000) {
//            /** 没有检查到网络*/
//            if (mErrorCode == ErrorCode.ERROR_NO_NETWORK) {
//                errorType = TIP_NO_NETWORK;
//            }
//            /** 网络连接超时*/
//            else if (mErrorCode == ErrorCode.ERROR_NETWORK_TIMEOUT) {
//                errorType = TIP_RESULT_TIMEOUT;
//            }
//            /** 网络异常*/
//            else if (mErrorCode == ErrorCode.ERROR_NET_EXCEPTION) {
//                errorType = TIP_ERROR_NET_EXPECTION;
//            }
//            /** 服务端返回无效的结果*/
//            else if (mErrorCode == ErrorCode.ERROR_INVALID_RESULT) {
//                errorType = TIP_ERROR_INVALID_RESULT;
//            }
//            /** 无匹配的结果*/
//            else if (mErrorCode == ErrorCode.ERROR_NO_MATCH) {
//                errorType = TIP_ERROR_NO_MATCH;
//            }
//            /** 录音启动失败*/
//            else if (mErrorCode == ErrorCode.ERROR_AUDIO_RECORD) {
//                errorType = TIP_ERROR_AUDIO_RECORD;
//            }
//            /** 无有效的音频输入*/
//            else if (mErrorCode == ErrorCode.ERROR_NO_SPEECH) {
//                errorType = TIP_ERROR_INVALID_ENCODING;
//            }
//            /** 录音超时*/
//            else if (mErrorCode == ErrorCode.ERROR_SPEECH_TIMEOUT) {
//                errorType = TIP_ERROR_SPEECH_TIMEOUT;
//            }
//            /** 无效的文本*/
//            else if (mErrorCode == ErrorCode.ERROR_EMPTY_UTTERANCE) {
//                errorType = TIP_ERROR_EMPTY_UTTERANCE;
//            }
//            /** 文件读写失败*/
//            else if (mErrorCode == ErrorCode.ERROR_FILE_ACCESS) {
//                errorType = TIP_ERROR_FILE_ACCESS;
//            }
//            /** 无效的参数*/
//            else if (mErrorCode == ErrorCode.ERROR_INVALID_PARAM) {
//                errorType = TIP_ERROR_INVALID_PARAM;
//            }
//            /** 初始化失败*/
//            else if (mErrorCode == ErrorCode.ERROR_ENGINE_INIT_FAIL) {
//                errorType = TIP_ERROR_ENGINE_INIT_FAIL;
//            }
//            /** 引擎未安装 */
//            else if (mErrorCode == ErrorCode.ERROR_ENGINE_NOT_SUPPORTED
//                    || mErrorCode == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
//                errorType = TIP_ENGINE_NOT_INSTALLED;
//            } else if (mErrorCode == ErrorCode.ERROR_MFV_RESVER_NOMATCH) {
//                errorType = TIP_ERROR_AUTHID_NOT_AVAILABLE;
//            } else {
//                /** 本地引擎通用错误 */
//                errorType = TIP_LOCAL_ENGINE_ERROR;
//            }
//        }
//
//        /** 人脸+声纹混合验证错误码*/
//        switch (mErrorCode) {
//            case ErrorCode.MSP_ERROR_IVP_GENERAL:
//                /** 声纹内核异常*/
//                errorType = TIP_ERROR_IVP_GENERAL;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_EXTRA_RGN_SOPPORT:
//                /** rgn超过最大支持次数9*/
//                errorType = TIP_ERROR_IVP_EXTRA_RGN_SOPPORT;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_TRUNCATED:
//                /** 音频波形幅度太大，超出系统范围，发生截幅*/
//                errorType = TIP_ERROR_IVP_TRUNCATED;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_MUCH_NOISE:
//                /** 太多噪音*/
//                errorType = TIP_ERROR_IVP_MUCH_NOISE;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_TOO_LOW:
//                /** 声音太小*/
//                errorType = TIP_ERROR_IVP_TOO_LOW;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_ZERO_AUDIO:
//                /** 没检测到音频*/
//                errorType = TIP_ERROR_IVP_ZERO_AUDIO;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_UTTER_TOO_SHORT:
//                /** 音频太短*/
//                errorType = TIP_ERROR_IVP_UTTER_TOO_SHORT;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_TEXT_NOT_MATCH:
//                /** 音频内容与给定文本不一致*/
//                errorType = TIP_ERROR_IVP_TEXT_NOT_MATCH;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_NO_ENOUGH_AUDIO:
//                /** 音频长达不到自由说的要求*/
//                errorType = TIP_ERROR_IVP_NO_ENOUGH_AUDIO;
//                break;
//            case ErrorCode.MSP_ERROR_IVP_MODEL_NOT_FOUND_IN_HBASE:
//            case ErrorCode.MSP_ERROR_FACE_MODEL_NOT_FOUND_IN_HBASE:
//                /** 模型数据在hbase中找不到 **/
//                errorType = TIP_ERROR_MODEL_NOT_FOUND;
//                break;
//            case ErrorCode.MSP_MODEL_NEED_UPDATE:
//                /** 模型正在生成 **/
//                errorType = TIP_ERROR_MODEL_IS_CREATING;
//                break;
//            case ErrorCode.MSP_ERROR_IFR_NOT_FACE_IMAGE:
//                /** 未检测到人脸 */
//                errorType = TIP_ERROR_NOT_FACE_IMAGE;
//                break;
//            case ErrorCode.MSP_ERROR_FACE_IMAGE_FULL_LEFT:
//                /** 人脸偏向左 */
//                errorType = TIP_ERROR_FACE_IMAGE_FULL_LEFT;
//                break;
//            case ErrorCode.MSP_ERROR_FACE_IMAGE_FULL_RIGHT:
//                /** 人脸偏向右 */
//                errorType = TIP_ERROR_FACE_IMAGE_FULL_RIGHT;
//                break;
//            case ErrorCode.MSP_ERROR_IMAGE_CLOCKWISE_WHIRL:
//                /** 顺时针旋转 */
//                errorType = TIP_ERROR_IMAGE_CLOCKWISE_WHIRL;
//                break;
//            case ErrorCode.MSP_ERROR_IMAGE_COUNTET_CLOCKWISE_WHIRL:
//                /** 逆时针旋转 */
//                errorType = TIP_ERROR_IMAGE_COUNTET_CLOCKWISE_WHIRL;
//                break;
//            case ErrorCode.MSP_ERROR_VALID_IMAGE_SIZE:
//                /** 尺寸错误*/
//                errorType = TIP_ERROR_VALID_IMAGE_SIZE;
//                break;
//            case ErrorCode.MSP_ERROR_ILLUMINATION:
//                /** 光照异常 */
//                errorType = TIP_ERROR_ILLUMINATION;
//                break;
//            case ErrorCode.MSP_ERROR_FACE_OCCULTATION:
//                /** 人脸被遮挡 */
//                errorType = TIP_ERROR_FACE_OCCULTATION;
//                break;
//            case ErrorCode.MSP_ERROR_FACE_INVALID_MODEL:
//                /** 非法模型数据 */
//                errorType = TIP_ERROR_FACE_INVALID_MODEL;
//                break;
//            case ErrorCode.MSP_ERROR_FUSION_INVALID_INPUT_TYPE:
//                /** 输入数据类型非法 */
//                errorType = TIP_ERROR_FUSION_INVALID_INPUT_TYPE;
//                break;
//            case ErrorCode.MSP_ERROR_FUSION_NO_ENOUGH_DATA:
//                /** 输入的数据不完整 */
//                errorType = TIP_ERROR_FUSION_NO_ENOUGH_DATA;
//                break;
//            case ErrorCode.MSP_ERROR_FUSION_ENOUGH_DATA:
//                /** 输入的数据过多 */
//                errorType = TIP_ERROR_FUSION_ENOUGH_DATA;
//                break;
//            case ErrorCode.MSP_ERROR_NO_GROUP:
//                /** 组不存在，未创建  */
//                errorType = TIP_ERROR_NO_GROUP;
//                break;
//            case ErrorCode.MSP_ERROR_GROUP_EMPTY:
//                /** 空组  */
//                errorType = TIP_ERROR_GROUP_EMPTY;
//                break;
//            case ErrorCode.MSP_ERROR_NO_USER:
//                /** 组内没有该用户 */
//                errorType = TIP_ERROR_NO_USER;
//                break;
//            case ErrorCode.MSP_ERROR_OVERFLOW_IN_GROUP:
//                /** 用户数量超过组上限或者组创建数量超过上限 */
//                errorType = TIP_ERROR_OVERFLOW_IN_GROUP;
//                break;
//
//            default:
//                break;
//        }
//
//        mDescription = Resource.getErrorDescription(errorType);
//
//    }
//
//    /**
//     * <h4>转为字符串</h4>
//     * <p>
//     * 把当前的信息转为字符串类，与{@link #getPlainDescription(boolean)}
//     * 参数为true时的效果一致。
//     * </p>
//     *
//     * @return 错误描述字符串值
//     * @see #getErrorCode()
//     * @see #getPlainDescription(boolean)
//     */
//    public String toString() {
//        return getPlainDescription(true);
//    }
//
//    /**
//     * <h4>获取错误码</h4>
//     * <p>
//     * 获取错误对应用的具体错误码，[10000, 20000)的错误码为底层共享库和
//     * 服务器返回的错误码，[20000, +∞)为 jar 层返回的错误。具体错误码值，请
//     * 参考{@link ErrorCode}。
//     * </p>
//     *
//     * @return 错误码值
//     * @see ErrorCode
//     */
//    public int getErrorCode() {
//        return mErrorCode;
//    }
//
//    /**
//     * <h4>获取错误描述</h4>
//     * <p>
//     * 不包含错误码的描述信息。描述信息有助于开发者和用户处理引起错误的
//     * 原因。
//     * </p>
//     *
//     * @return 错误描述信息
//     * @see #getErrorCode()
//     * @see #getPlainDescription(boolean)
//     */
//    public String getErrorDescription() {
//        return mDescription;
//    }
//
//    /**
//     * <h4>获取html错误描述</h4>
//     * <p>
//     * 获取html格式的错误描述。
//     * </p>
//     *
//     * @param containCode 是否包含错误码
//     * @return html错误描述
//     * @hide
//     * @see #getPlainDescription(boolean)
//     * @see #getErrorCode()
//     */
//    public String getHtmlDescription(boolean containCode) {
//        // 设置字体颜色
////		String ret = "<font  size=\"" + ResourceUtils.getErrorTextSize()[0];
////		ret += "\" color=\"" + ResourceUtils.getErrorTextColor()[0] + "\">";
//        String ret = mDescription + "...";
//        if (containCode) {
////			ret += "</font><br><font  size=\"" + ResourceUtils.getErrorTextSize()[1];
////			ret += "\" color=\"" + ResourceUtils.getErrorTextColor()[1] + "\">";
////			ret += "(" + Resource.getErrorTag(Resource.TAG_ERROR_CODE) + ":";
////			ret += mErrorCode + ")</font><br>";
//            ret += "<br>" + "(";
//            ret += Resource.getErrorTag(Resource.TAG_ERROR_CODE) + ":";
//            ret += mErrorCode + ")";
////			+ ResourceUtils.TAG_INTERVAL_LINE;
//        }
//        return ret;
//    }
//
//    /**
//     * <h4>获取错误描述</h4>
//     * <p>
//     * 包含错误码的描述信息。描述信息有助于开发者和用户处理引起错误的
//     * 原因。
//     * </p>
//     *
//     * @return 包含错误码的描述信息
//     * @see #getErrorCode()
//     * @see #getErrorDescription()
//     */
//    public String getPlainDescription(boolean containCode) {
//        String ret = mDescription;
//
//        if (containCode) {
//            ret += ".";
//            ret += "(" + Resource.getErrorTag(Resource.TAG_ERROR_CODE) + ":";
//            ret += mErrorCode + ")";
//        }
//        return ret;
//    }
//}
