package com.xsl.skincore2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {
    private SkinAttribute skinAttribute;

    public SkinLayoutFactory() {
        skinAttribute = new SkinAttribute();
    }

    // 安卓的控件都在这三个包下面
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private static final Class[] mConstructorSignature = new Class[]{
            Context.class,AttributeSet.class };

    private static final HashMap<String, Constructor<? extends View>> mConstructorMap =
            new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = onCreateViewFromTag(name,context,attrs);
        // 自定义view
        if(null == view){
            view = createView(name,context,attrs);
        }
        // 删选需要换肤的属性
        skinAttribute.load(view,attrs);
        return view;
    }

    private View onCreateViewFromTag(String name, Context context, AttributeSet attrs) {
        // 包含自定义控件
        if(-1 != name.indexOf(".")){
            return null;
        }
        View view = null;
        for(int i = 0; i < mClassPrefixList.length; i++){
            view = createView(mClassPrefixList[i]+name,context,attrs);
            if(view != null){
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if(constructor == null){
            try{
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                // 获取构造方法
                constructor = aClass.getConstructor(mConstructorSignature);
                mConstructorMap.put(name,constructor);
                // 我自己加了这句
                return constructor.newInstance(context,attrs);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                return constructor.newInstance(context,attrs);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        // 更换皮肤
        skinAttribute.applySkin();
    }
}
