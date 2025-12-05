package com.scsa.memo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.graphics.shapes.Utils;

import com.google.android.material.appbar.MaterialToolbar;
import com.scsa.memo.databinding.ActivityMemoEditBinding;

import java.util.Calendar;

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

        if (position >= 0) {
            setEditActivity(position);
        } else {
            setAddActivity();
        }

        Button btnCancel = binding.memoEditBtnCancel;
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

    private void setEditActivity(int position) {
        MemoDto selected = memoManager.get(position);

        // set visibilities
        LinearLayout layout = binding.memoEditLayoutRegTime;
        layout.setVisibility(View.VISIBLE);

        TextView regDateTitle = binding.memoEditTextRegDateTitle;
        regDateTitle.setVisibility(View.VISIBLE);

        EditText regDate = binding.memoEditEditTextRegTime;
        regDate.setVisibility(View.VISIBLE);

        Button btnDelete = binding.memoEditBtnDelete;
        btnDelete.setVisibility(View.VISIBLE);

        // set values
        EditText editTextTitle = binding.memoEditEditTextTitle;
        editTextTitle.setText(selected.getTitle());
        editTextTitle.setEnabled(false);

        EditText editTextBody = binding.memoEditEditTextBody;
        editTextBody.setText(selected.getBody());

        Button btnSave = binding.memoEditBtnSave;
        btnSave.setText("수정");

        regDate.setText(Util.getFormattedTime(selected.getRegDate()));

        // set handlers
        Button btnSetAlarm = binding.memoEditBtnSetAlarm;

        btnSetAlarm.setOnClickListener((v) -> {
            // TODO: set alarm


        });

        btnDelete.setOnClickListener((v) -> {
            memoManager.remove(position);
            finish();
        });

        btnSave.setOnClickListener((v) -> {
            String title = String.valueOf(editTextTitle.getText());
            String body = String.valueOf(editTextBody.getText());
            MemoDto dto = new MemoDto(title, body);

            memoManager.update(position, dto);
            finish();
        });
    }

    private void setAddActivity() {
        EditText editTextTitle = binding.memoEditEditTextTitle;
        EditText editTextBody = binding.memoEditEditTextBody;
        Button btnSave = binding.memoEditBtnSave;

        btnSave.setText("추가");
        btnSave.setOnClickListener((v) -> {
            String title = String.valueOf(editTextTitle.getText());
            String body = String.valueOf(editTextBody.getText());
            MemoDto dto = new MemoDto(title, body);

            memoManager.add(dto);
            finish();
        });
    }

    private Dialog createDatePickerDialog() {
        var dialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                mDay);

        var start = Calendar.getInstance();
        start.add(Calendar.DATE, -5);
        var end = Calendar.getInstance();
        end.add(Calendar.DATE, +5);

        dialog.getDatePicker().setMinDate(start.getTimeInMillis());
        dialog.getDatePicker().setMaxDate(end.getTimeInMillis());

        return dialog;
    }
}
