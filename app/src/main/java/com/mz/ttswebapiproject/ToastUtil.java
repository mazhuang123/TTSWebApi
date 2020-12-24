package com.mz.ttswebapiproject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 *
 */
public class ToastUtil {
    // 限制网络异常提示的间隔，4秒内只允许提示1次
    private static long LastTimeShowError = 0; // 上次调用错误弹窗的时间
    private static long IntervShowError = 4000;// 两次弹窗的最小间隔时间
    private static Handler mHandler = null;
    public static void showErrorNet(Context context) {
        if(context==null || isActivity(context)){
            return;
        }
        // 9秒只允许提示一次网络异常
        long currTime = System.currentTimeMillis();
        if (currTime - LastTimeShowError < IntervShowError)// 如果上次显示的时间跟现在的时间没过需要间隔的时间，不显示
            return;
        LastTimeShowError = currTime;
        showToast(context, "网络错误");
    }

    public static void showErrorData(Context context) {
        if(context==null || isActivity(context)){
            return;
        }
        showToast(context, "网络错误");
    }

    private static boolean isActivity(Context context) {
        if(context instanceof Activity){
            Activity activity = (Activity) context;
            if(activity.isFinishing()){
                return true;
            }
        }
        return false;
    }

    public static void showToast(Context mContext, final String text) {
        final Context context = mContext;
        final String msg = text;
        if (msg == null || msg.length() <= 0) {
            return;
        }
        if(context==null || isActivity(context)){
            return;
        }
        makeMainThreadHandler(mContext).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void showToast(Context context, int resId) {
        if(context==null || isActivity(context)){
            return;
        }
        if (Looper.myLooper()!=Looper.getMainLooper()){
            Looper.prepare();
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        else{
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
        }

    }
    private static Handler makeMainThreadHandler(Context context) {
        if (context == null) {
            context = MyApplication.getInstance().getApplicationContext();
        }
        if (mHandler == null) {
            mHandler = new Handler(context.getMainLooper());
        }

        return mHandler;
    }
}
