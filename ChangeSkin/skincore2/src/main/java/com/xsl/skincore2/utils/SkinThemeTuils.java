package com.xsl.skincore2.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class SkinThemeTuils {
    public static int[] getResId(Context context,int[] attrs){
        int[] ints = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for(int i = 0; i < typedArray.length(); i++){
            ints[i] = typedArray.getResourceId(i,0);
        }
        typedArray.recycle();
        return ints;
    }
}
