package com.xsl.ndhandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                MyLooper.prepare();
                mHandler = new MyHandler() {
                    @Override
                    public void handleMessage(@NonNull MyMessage msg) {
                        if (msg.what == 0x111) {
                            Log.e("thread1", msg.obj.toString());
                        }
                    }
                };
                MyLooper.loop();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                MyMessage message = new MyMessage();
                message.what = 0x111;
                message.obj = "哎呀妈呀，都特么开始手写Handler了，牛逼牛逼！";
                mHandler.sendMessage(message);
                try {
                    Thread.sleep(2000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });
        thread1.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        thread2.start();
    }
}
