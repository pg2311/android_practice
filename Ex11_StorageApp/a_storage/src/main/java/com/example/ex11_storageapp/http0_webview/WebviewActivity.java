package com.example.ex11_storageapp.http0_webview;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex11_storageapp.R;


public class WebviewActivity extends AppCompatActivity {
    private static final String TAG = "WebviewActivity_SCSA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1. webview만들어서 webview예제 진행
        setContentView(R.layout.activity_http_webview);

        WebView webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        // 페이지 로딩 콜백되는 함수들이 지정되어 있음.
        // 로딩의 시작과 끝을 알 수 있다. shouldOverrideUrlLoading이 있다.
//        webview.setWebViewClient(new WebViewClient());

        // 페이지 내에세 일어나는 일의 콜백. 로딩상태 리턴하는 onProgressChanged().가 있다.
        // 새창, 파일첨부등 호출시 콜백됨.
//        webview.setWebChromeClient(new WebChromeClient());

//        webview.loadUrl("https://m.naver.com");
        findViewById(R.id.btn_go).setOnClickListener( v -> {
            String inputUrl = ((EditText)findViewById(R.id.url)).getText().toString();
            webview.loadUrl(inputUrl);
        });

    }
}

