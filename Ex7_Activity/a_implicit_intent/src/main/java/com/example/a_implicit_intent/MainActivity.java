package com.example.a_implicit_intent;
//import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
//
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    Button btCall, btGallary, btExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btCall = (Button)findViewById(R.id.btCall);
        btGallary = (Button)findViewById(R.id.btGallary);
        btExit = (Button)findViewById(R.id.btExit);

        btCall.setOnClickListener(view ->{
            Intent  callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/010"));
//                callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택의 맨 위에 있는 Activity가 이미 존재할 경우, 해당 Activity를 재사용하고, 그 위의 Activity를 모두 제거합니다.
            startActivity(callIntent);
        });

        btGallary.setOnClickListener(view ->{
            Intent gallaryIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("content://media/internal/images/media"));
            gallaryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(gallaryIntent);
        });

        btExit.setOnClickListener(view ->{
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}