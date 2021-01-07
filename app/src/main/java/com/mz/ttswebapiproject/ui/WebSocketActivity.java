package com.mz.ttswebapiproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mz.ttswebapiproject.R;
import com.mz.ttswebapiproject.util.LogUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/6 21:40
 * @Description 文件描述：
 */
public class WebSocketActivity extends AppCompatActivity {
    private String url = "websocket://10.25.170.122:9000/lingxiyun/cloud/tts/v1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.websocket_layout);
        Button connectBtn = findViewById(R.id.connect_btn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWebSocket();
            }
        });
    }

        public void connectWebSocket() {
            try {
                Map<String, String> map = new HashMap<>();
                map.put("origin", url);
                WebSocketClient webSocketClient = new WebSocketClient(URI.create(url),new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake handShakeServer) {
                        LogUtil.e("onOpen");
                    }

                    @Override
                    public void onMessage(String message) {
                        LogUtil.e("onMessage");
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        LogUtil.e("onClose");
                    }

                    @Override
                    public void onError(Exception ex) {
                        LogUtil.e("onError："+ex.toString());
                    }
                };
                // WebSocket连接wss链接
                // This part is needed in case you are going to use self-signed
                // certificates
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                    }

                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }
                } };
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                SSLSocketFactory factory = sc.getSocketFactory();
                webSocketClient.setSocket(factory.createSocket());
                webSocketClient.connect();
            } catch (Exception e) {
                LogUtil.e("onError："+e.toString());
            }
        }
}
