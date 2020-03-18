package com.xsl.skincore2;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SkinActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity,SkinLayoutFactory> factoryHashMap = new HashMap<>();

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        try{
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.setBoolean(inflater,false);
        }catch (Exception e){
            e.printStackTrace();
        }
        SkinLayoutFactory factory = new SkinLayoutFactory();
        inflater.setFactory2(factory);
        // 注册观察者
        SkinManager.getInstance().addObserver(factory);
        factoryHashMap.put(activity,factory);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutFactory remove = factoryHashMap.remove(activity);
        SkinManager.getInstance().deleteObserver(remove);
    }
}
