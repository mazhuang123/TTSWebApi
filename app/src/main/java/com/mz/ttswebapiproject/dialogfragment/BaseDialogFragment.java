package com.mz.ttswebapiproject.dialogfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 16:56
 * @Description 文件描述：
 */
public class BaseDialogFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes((WindowManager.LayoutParams) params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
