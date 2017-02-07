package com.z2wenfa.rxutildemo.rxjava.bean;

import rx.Observable;

public abstract class MyOnSubscribe<C> implements Observable.OnSubscribe<C> {
    private C c;

    public MyOnSubscribe(C c) {
        setT(c);
    }

    public C getT() {
        return c;
    }

    public void setT(C c) {
        this.c = c;
    }


}