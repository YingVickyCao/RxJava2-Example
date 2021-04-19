package com.hades.android.example.rxjava2._3_op;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hades.android.example.rxjava2.R;

public class ZipActivity extends AppCompatActivity {
    ZipExample example;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_zip);

        findViewById(R.id.zip).setOnClickListener(v -> zip());
        example = new ZipExample();
    }

    private void zip() {
        example.test();
    }
}