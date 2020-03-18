package com.xsl.skincore2;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.xsl.skincore2.utils.SkinPreference;
import com.xsl.skincore2.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable {
    private static SkinManager instance;
    private Application application;

    private SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResources.init(application);
        // Application注册了Activity的生命周期回调后，每个Activity的生命周期方法都会
        // 对应回调到 SkinActivityLifeCycle 中
        application.registerActivityLifecycleCallbacks(new SkinActivityLifeCycle());

        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public static void init(Application application){
        synchronized (SkinManager.class) {
            if(null == instance){
                instance = new SkinManager(application);
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    public void loadSkin(String path) {
        // 还原
        if (TextUtils.isEmpty(path)) {
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();
        } else {
            try{
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",String.class);
                addAssetPath.invoke(assetManager,path);
                // 当前app的Resources
                Resources appResources = this.application.getResources();

                Resources skinResources = new Resources(assetManager,appResources.getDisplayMetrics(),appResources.getConfiguration());

                SkinPreference.getInstance().setSkin(path);
                PackageManager packageManager = this.application.getPackageManager();
                PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                String packageName = packageArchiveInfo.packageName;

                SkinResources.getInstance().applySkin(skinResources,packageName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers();
    }
}
