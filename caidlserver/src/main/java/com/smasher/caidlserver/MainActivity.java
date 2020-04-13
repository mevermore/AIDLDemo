package com.smasher.caidlserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceS();
    }

    private void startServiceS() {
        Intent intent = new Intent();
        intent.setClass(this, AIDLService.class);
        startService(intent);
    }
}
