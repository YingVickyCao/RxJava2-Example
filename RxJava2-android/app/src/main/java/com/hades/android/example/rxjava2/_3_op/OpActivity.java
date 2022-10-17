package com.hades.android.example.rxjava2._3_op;

import android.app.Activity;
import android.os.Bundle;

import com.hades.android.example.rxjava2.R;
import com.hades.android.example.rxjava2._3_op.ZipExample;

public class OpActivity extends Activity {
    ZipExample example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);
        findViewById(R.id.zip).setOnClickListener(v -> zip());
        example = new ZipExample();

        findViewById(R.id.delay).setOnClickListener(v -> DelayExample.test());

    }

    private void zip() {
        example.test();
    }
}