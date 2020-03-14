package com.xsl.myeventbus.core;

import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MySubscribe {
    MyThreadMode threadMode() default MyThreadMode.POSTING;
}
