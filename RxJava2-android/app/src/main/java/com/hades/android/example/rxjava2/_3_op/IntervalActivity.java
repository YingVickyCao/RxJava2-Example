package com.hades.android.example.rxjava2._3_op;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hades.android.example.rxjava2.R;

public class IntervalActivity extends AppCompatActivity {
    IntervalExample example;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_interval);

        findViewById(R.id.start).setOnClickListener(v -> start());
        findViewById(R.id.end).setOnClickListener(v -> end());
        example = new IntervalExample();
    }

    private void start() {
        example.startInterval();
    }

    private void end() {
        example.stopInterval();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        end();
    }
}