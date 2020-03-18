package com.xsl.skincore2;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;


import com.xsl.skincore2.utils.SkinResources;
import com.xsl.skincore2.utils.SkinThemeTuils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {

    private static final List<String> mAttributes = new ArrayList<>();
    private List<SkinView> skinViewList = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableBottom");
    }

    public void load(View view, AttributeSet attrs) {
        List<SkinPain> skinPainList = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                // 获取属性具体的值
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeTuils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    SkinPain skinPain = new SkinPain(attributeName, resId);
                    skinPainList.add(skinPain);
                }
            }
        }
        if (!skinPainList.isEmpty()) {
            SkinView skinView = new SkinView(view, skinPainList);

            skinView.applySkin();
            skinViewList.add(skinView);
        }
    }

    static class SkinView {
        View view;
        List<SkinPain> skinPains;

        public SkinView(View view, List<SkinPain> skinPains) {
            this.view = view;
            this.skinPains = skinPains;
        }

        public void applySkin() {
            for (SkinPain skinPain : skinPains) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPain.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPain.resId);
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPain
                                .resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList
                                (skinPain.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPain.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPain.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPain.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPain.resId);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
                }
            }
        }
    }

    static class SkinPain {
        String attributeName;
        int resId;

        public SkinPain(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }

    public void applySkin(){
        for(SkinView skinView : skinViewList){
            skinView.applySkin();
        }
    }
}