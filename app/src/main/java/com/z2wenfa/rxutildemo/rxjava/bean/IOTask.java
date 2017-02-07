package com.z2wenfa.rxutildemo.rxjava.bean;

/**
 * 在IO线程中执行的任务
 * Created by 韩莫熙 on 2017/2/7.
 */
public abstract class IOTask<T> {
    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }


    public IOTask(T t) {
        setT(t);
    }


    public abstract void doInIOThread();
}
