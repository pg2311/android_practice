package com.scsa.paint;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Button colorRed = findViewById(R.id.colorRed);
        Button colorBlack = findViewById(R.id.colorBlack);
        Button colorBlue = findViewById(R.id.colorBlue);
        Button clearAll = findViewById(R.id.clearAll);
        SeekBar seekBar = findViewById(R.id.brushWidthSeekBar);
        TextView textView = findViewById(R.id.brushWidthText);

        PaintDrawView paintDrawView = new PaintDrawView(this);

        colorRed.setOnClickListener((v) -> {paintDrawView.setBrushColor("RED");});
        colorBlack.setOnClickListener((v) -> {paintDrawView.setBrushColor("BLACK");});
        colorBlue.setOnClickListener((v) -> {paintDrawView.setBrushColor("Blue");});
        clearAll.setOnClickListener((v)->{paintDrawView.clearAll();});
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paintDrawView.setBrushWidth(progress);
                textView.setText("width: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}