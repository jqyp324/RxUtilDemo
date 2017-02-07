package com.z2wenfa.rxutildemo.rxjava;

import com.z2wenfa.rxutildemo.rxjava.bean.CommonRxTask;
import com.z2wenfa.rxutildemo.rxjava.bean.IOTask;
import com.z2wenfa.rxutildemo.rxjava.bean.MyOnSubscribe;
import com.z2wenfa.rxutildemo.rxjava.bean.UITask;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Rxjava封装工具类
 * Created by 韩莫熙 on 2017/2/7.
 */
public class RxjavaUtil {

    /**
     * 在ui线程中工作
     *
     * @param uiTask
     */
    public static <T> void doInUIThread(UITask<T> uiTask) {
        doInUIThreadDelay(uiTask, 0, TimeUnit.MILLISECONDS);
    }


    /**
     * 延时在主线程中执行任务
     *
     * @param uiTask
     * @param time
     * @param timeUnit
     * @param <T>
     */
    public static <T> void doInUIThreadDelay(UITask<T> uiTask, long time, TimeUnit timeUnit) {
        Observable.just(uiTask)
                .delay(time, timeUnit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UITask<T>>() {
                    @Override
                    public void call(UITask<T> uitask) {
                        uitask.doInUIThread();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    /**
     * 在IO线程中执行任务
     *
     * @param <T>
     */
    public static <T> void doInIOThread(IOTask<T> ioTask) {
        doInIOThreadDelay(ioTask, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * 延时在IO线程中执行任务
     *
     * @param <T>
     */
    public static <T> void doInIOThreadDelay(IOTask<T> ioTask, long time, TimeUnit timeUnit) {
        Observable.just(ioTask)
                .delay(time, timeUnit)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<IOTask<T>>() {
                    @Override
                    public void call(IOTask<T> ioTask) {
                        ioTask.doInIOThread();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    /**
     * 执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param t
     * @param <T>
     */
    public static <T> void executeRxTask(CommonRxTask<T> t) {
        executeRxTaskDelay(t, 0, TimeUnit.MILLISECONDS);
    }


    /**
     * 延时执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param t
     * @param <T>
     */
    public static <T> void executeRxTaskDelay(CommonRxTask<T> t, long time, TimeUnit timeUnit) {
    MyOnSubscribe<CommonRxTask<T>> onsubscribe = new MyOnSubscribe<CommonRxTask<T>>(t) {
            @Override
            public void call(Subscriber<? super CommonRxTask<T>> subscriber) {
                getT().doInIOThread();
                subscriber.onNext(getT());
                subscriber.onCompleted();
            }
        };
        Observable.create(onsubscribe)
                .delay(time, timeUnit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonRxTask<T>>() {
                    @Override
                    public void call(CommonRxTask<T> t) {
                        t.doInUIThread();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }



}
