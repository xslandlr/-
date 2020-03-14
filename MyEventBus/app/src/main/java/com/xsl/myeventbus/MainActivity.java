package com.xsl.myeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xsl.myeventbus.core.MyEventBus;
import com.xsl.myeventbus.core.MySubscribe;
import com.xsl.myeventbus.core.MyThreadMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyEventBus.getDefault().register(this);

        tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @MySubscribe(threadMode = MyThreadMode.MAIN)
    public void get(MessageEvent event){
        Log.e("xsl", event.getStr() );
        tv.setText(event.getStr());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyEventBus.getDefault().unregister(this);
    }
}
