package com.mz.ttswebapiproject.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mz.ttswebapiproject.R;

/**
 * @Author 作者：mazhuang
 *
 * @Date 创建时间： 2020/12/8 10:41
 *
 * @Description 文件描述: https://www.jianshu.com/p/da4a806e599b
 * https://www.cnblogs.com/badaoliumangqizhi/p/14023058.html
 *
 * https://github.com/452896915/jieba-android
 *
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ttsBtn = findViewById(R.id.test_tts_btn);
        Button websocketBtn = findViewById(R.id.test_websocket_btn);
        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TTSActivity.class);
                startActivity(intent);
            }
        });
        websocketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebSocketActivity.class);
                startActivity(intent);
            }
        });
    }
}
