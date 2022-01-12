package com.android.yitianspider;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Queue;

public class MyThread {
    private static String TAG = "test1";
    private Queue imageFiles = null;
    private int fileSzie ;

    public void getFileName(String fileDir) {
        List pathList = new ArrayList() {};
        File file = new File(fileDir);
        File[] subFile = file.listFiles();
        fileSzie = subFile.length;
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                pathList.add(fileDir + filename);
                Log.d(TAG, fileDir + filename);
            }
        }
        imageFiles = new LinkedList(pathList);
    }

    public void NewFixedThreadPool() {
        SpiderNet spiderNet = new SpiderNet();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < fileSzie; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String name = imageFiles.poll().toString();
                    Log.d(TAG, "上传图片:" + name);
                    spiderNet.upLoadImg(name);
                }
            });
        }
    }
}
