package com.xsl.myeventbus.core;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyEventBus {

    private static MyEventBus instance = new MyEventBus();
    private Map<Object, List<SubscribleMethod>> cacheMap;
    private Handler mHandler;
    private ExecutorService executorService;

    private MyEventBus(){
        cacheMap = new HashMap<>();
        // 主进程Handler
        mHandler = new Handler(Looper.getMainLooper());
        // 执行子任务的线程池
        executorService = Executors.newCachedThreadPool();
    }

    public static MyEventBus getDefault(){
        return instance;
    }

    /**
     *  注册接收事件的类
     * @param subscriber MainActivity
     */
    public void register(Object subscriber){
        Class<?> subscriberClass = subscriber.getClass();
        List<SubscribleMethod> subscribleMethods = cacheMap.get(subscriberClass);
        // 等于空就表示这个subscriber没有注册到cacheMap中
        if(subscribleMethods == null){
            subscribleMethods = getMethodsFromSubscriber(subscriber);
            cacheMap.put(subscriber,subscribleMethods);
        }
    }

    // 寻找订阅者(MainActivity)中的订阅方法。即打了Subscrible注解的方法
    private List<SubscribleMethod> getMethodsFromSubscriber(Object subscriber) {
        List<SubscribleMethod> subscribleMethodList = new ArrayList<>();
        Class<?> subscriberClass = subscriber.getClass();
        // Eventbus也许是注册在BaseActivity中的，所以要父类也需要遍历。
        while (subscriberClass != null){
            String name = subscriberClass.getName();
            // BaseActiviy的父类是Activity，Activity肯定是不需要遍历的，Activity的包名以Android.开头，所以遍历到这里，就break
            if(name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.") || name.startsWith("androidx.")){
                break;
            }
            Method[] methods = subscriberClass.getDeclaredMethods();
            // 寻找和 EventBus 相关的方法，并保存
            for(int i = 0; i < methods.length; i++){
                //  EventBus语法，只能接收一个参数。
                if(methods[i].getParameterTypes().length != 1){
                    continue;
                }
                // 必须要有 MySubscribe 注解，才是 EventBus 需要的方法
                MySubscribe annotation = methods[i].getAnnotation(MySubscribe.class);
                if(annotation == null){
                    continue;
                }
                MyThreadMode myThreadMode = annotation.threadMode();
                Class<?> eventType = methods[i].getParameterTypes()[0]; // 只有一个参数，所以获取第0个位置上的数据
                SubscribleMethod method = new SubscribleMethod(methods[i],myThreadMode,eventType);
                // 将符合条件的方法保存到List集合中，并返回
                subscribleMethodList.add(method);
            }
            subscriberClass = subscriberClass.getSuperclass();
        }

        return subscribleMethodList;
    }

    /**
     *  发送事件
     * @param event  MessageEvent
     */
    public void post(Object event){
        Set<Object> keySet = cacheMap.keySet();
        Iterator<Object> iterator = keySet.iterator();
        // 遍历所有的注册类，在他们的注册事件中寻找类型一样的方法，利用反射执行这些方法
        while(iterator.hasNext()){
            // 注册类 （MainActivity）
            Object next = iterator.next();
            List<SubscribleMethod> subscribleMethodList = cacheMap.get(next);
            for(int i = 0; i < subscribleMethodList.size(); i++){
                SubscribleMethod method = subscribleMethodList.get(i);
                if(method.getEventType().isAssignableFrom(event.getClass())){
                    try{
                        invoke(method,next,event);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // 使用反射执行
    private void invoke(final SubscribleMethod method,final Object next,final Object event) throws Exception{
            // 注册事件 指定的线程
        try{
            MyThreadMode threadMode = method.getThreadMode();
            if(threadMode == MyThreadMode.MAIN){
                // 发送事件如果是主线程
                if(Looper.myLooper() == Looper.getMainLooper()){
                    // 主线程发送，主线程接收
                    method.getMethod().invoke(next,event);
                }else{
                    // 发送事件是子线程，使用Handler发送消息
                    // 子线程发送，主线程接收
                    mHandler.post(new Runnable(){
                        @Override
                        public void run() {
                            try{
                                method.getMethod().invoke(next,event);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }else if(threadMode == MyThreadMode.ASYNC){
                if(Looper.myLooper() == Looper.getMainLooper()){
                    // 使用线程池，创建任务异步执行
                    // 主线程发送，子线程接收
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                method.getMethod().invoke(next,event);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    // 子线程发送，子线程接收
                    method.getMethod().invoke(next,event);
                }
            }else{
                method.getMethod().invoke(next,event);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  解除订阅
     */
    public void unregister(Object subscriber){
        List<SubscribleMethod> subscribleMethodList = cacheMap.get(subscriber);
        if(subscribleMethodList != null){
            cacheMap.remove(subscriber);
        }
    }
}
