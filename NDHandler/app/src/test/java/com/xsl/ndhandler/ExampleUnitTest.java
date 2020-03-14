package com.xsl.ndhandler;

import androidx.annotation.Nullable;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testThreadLocal(){

        final ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
            @Nullable
            @Override
            protected String initialValue() {
                return "铜锣湾扛把子";
            }
        };
        System.out.println("主线程:---"+threadLocal.get());

        ThreadLocal<Integer> threadLocalInteger = new ThreadLocal<Integer>(){
            @Nullable
            @Override
            protected Integer initialValue() {
                return 20;
            }
        };
        System.out.println("主线程：---"+threadLocalInteger.get());

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程1:---"+threadLocal.get());
                threadLocal.set("上海滩老大");
                System.out.println("线程1:---"+threadLocal.get());
            }
        });
        thread1.start();
    }
}