package com.xsl.changeskin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.xsl.skincore2.SkinManager;
import com.xsl.skincore2.utils.SkinPreference;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeskin(View view){
        File file = Environment.getExternalStorageDirectory();
        File file1 = new File(file.getPath()+"/skin.apk");
        if(file1.exists()){
            SkinManager.getInstance().loadSkin(file.getPath()+"/skin.apk");
        }
    }

    public void reset(View view){
        SkinManager.getInstance().loadSkin("");
    }
}
