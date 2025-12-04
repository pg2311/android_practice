package com.example.b_dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class DatePickerActivity extends AppCompatActivity {
    private TextView mDateDisplay;
    private Button mPickDate;

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);

        mDateDisplay = findViewById(R.id.dateDisplay);
        mPickDate = findViewById(R.id.pickDate);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDatePickerDialog().show();
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();

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

    private void updateDisplay() {
        mDateDisplay.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-").append(mDay).append("-")
                .append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

}