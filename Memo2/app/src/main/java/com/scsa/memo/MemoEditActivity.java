package com.scsa.memo;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.graphics.shapes.Utils;

import com.google.android.material.appbar.MaterialToolbar;
import com.scsa.memo.databinding.ActivityMemoEditBinding;

public class MemoEditActivity extends AppCompatActivity {

    private static final String TAG = "MemoEditActivity";
    private MemoManager memoManager;
    private ActivityMemoEditBinding binding;
    MaterialToolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init
        binding = ActivityMemoEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        memoManager = MemoManager.getInstance();
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("툴바");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get position & modify activity
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        Log.i(TAG, "position: " + position);

        EditText editTextTitle = binding.memoEditEditTextTitle;
        EditText editTextBody = binding.memoEditEditTextBody;
        Button btnSave = binding.memoEditBtnSave;
        Button btnDelete = binding.memoEditBtnDelete;
        Button btnCancel = binding.memoEditBtnCancel;
        TextView regDateTitle = binding.memoEditTextRegDateTitle;
        TextView regDate = binding.memoEditTextRegDate;

        if (position >= 0) {
            MemoDto selected = memoManager.get(position);
            editTextTitle.setText(selected.getTitle());
            editTextTitle.setEnabled(false);
            editTextBody.setText(selected.getBody());

            regDateTitle.setVisibility(View.VISIBLE);
            regDate.setVisibility(View.VISIBLE);
            regDate.setText(Util.getFormattedDate(selected.getRegDate()));

            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener((v) -> {
                memoManager.remove(position);
                finish();
            });

            btnSave.setText("수정");
            btnSave.setOnClickListener((v) -> {
                String title = String.valueOf(editTextTitle.getText());
                String body = String.valueOf(editTextBody.getText());
                MemoDto dto = new MemoDto(title, body);

                memoManager.update(position, dto);
                finish();
            });
        } else {
            btnSave.setText("추가");
            btnSave.setOnClickListener((v) -> {
                String title = String.valueOf(editTextTitle.getText());
                String body = String.valueOf(editTextBody.getText());
                MemoDto dto = new MemoDto(title, body);

                memoManager.add(dto);
                finish();
            });
        }

        btnCancel.setOnClickListener((v) -> {
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
