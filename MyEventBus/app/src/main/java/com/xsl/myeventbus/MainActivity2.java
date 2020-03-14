package com.xsl.myeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.xsl.myeventbus.core.MyEventBus;

import org.greenrobot.eventbus.EventBus;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void send(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyEventBus.getDefault().post(new MessageEvent("我有一个小毛驴，我一直都不骑！"));
            }
        }).start();
    }
}
