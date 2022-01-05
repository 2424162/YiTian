package com.android.yitianspider;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Handler().postDelayed(() -> {
//            SpiderNet spiderNet = new SpiderNet();
//            spiderNet.upLoadImg();
//        }, 1);
//
//
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyThread thread = new MyThread();
                thread.getFileName("/storage/emulated/0/Pictures/origin/photo/");
                thread.NewFixedThreadPool();
            }
        }, 1);
    }


}