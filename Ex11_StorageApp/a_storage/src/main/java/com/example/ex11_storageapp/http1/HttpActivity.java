package com.example.ex11_storageapp.http1;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex11_storageapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpActivity extends AppCompatActivity {
    private static final String TAG = "HttpActivity_SCSA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_http);

        TextView textView = findViewById(R.id.textview);
        textView.setMovementMethod(new ScrollingMovementMethod()); //textview scroll


        String url = "https://jsonplaceholder.typicode.com/posts/1";
//        String url = "https://jsonplaceholder.typicode.com/posts";

        new Thread(() -> {
            Log.d(TAG, "run: start");
            HttpURLConnection connection;

            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                //연결할때 Time out 설정
                connection.setConnectTimeout(5000);
                //불러올때 Time out 설정
                connection.setReadTimeout(5000);

                //String 읽기.
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder message = new StringBuilder();
                String read = "";
                while ( (read = reader.readLine()) != null){
                    Log.d(TAG, "run: read : "+ read);
                    message.append(read);
                }
                // json -> Object
                Post post = new Gson().fromJson(message.toString(), new TypeToken<Post>(){}.getType() );
//                    List<Post> post = new Gson().fromJson(message.toString(), new TypeToken<ArrayList<Post>>(){}.getType() );
                Log.d(TAG, "run: post : "+post);

                runOnUiThread(() -> {
                    textView.setText(post.toString());
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

