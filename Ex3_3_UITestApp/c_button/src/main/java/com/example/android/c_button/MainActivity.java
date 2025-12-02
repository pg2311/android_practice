package com.example.android.c_button;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        CheckBox one = findViewById(R.id.checkBox);

        //TODO. checkbox가 체그되었는지, 체크해제되었는지의 여부를 Toast메시지로 출력해본다
        //hint :  토스트메시지 : Toast.makeText(MainActivity.this, "체크됨",  Toast.LENGTH_SHORT).show();
        one.setOnCheckedChangeListener((view, isChecked) -> {
           //완성하세요
            if (isChecked) {
                Toast.makeText(MainActivity.this, "체크됨", Toast.LENGTH_LONG).show();
            }
        });

        //텍스트 태그를
        Chip chip = findViewById(R.id.chip1);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chip 삭제 동작
                // Chip의 부모 뷰에서 제거
                ((ViewGroup) chip.getParent()).removeView(chip);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}