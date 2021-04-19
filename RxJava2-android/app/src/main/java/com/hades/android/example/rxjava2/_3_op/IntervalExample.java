package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class IntervalExample {
    private static final String TAG = IntervalExample.class.getSimpleName();
    private Disposable mDisposable;

    /*
    accept: doOnNext : 0,thread: id=4083,name=RxComputationThreadPool-1
    accept: 设置文本 ：0,thread: id=2,name=main

    accept: doOnNext : 1,thread: id=4083,name=RxComputationThreadPool-1
    accept: 设置文本 ：1,thread: id=2,name=main

    accept: doOnNext : 2,thread: id=4083,name=RxComputationThreadPool-1
    accept: 设置文本 ：2,thread: id=2,name=main

    accept: doOnNext : 3,thread: id=4083,name=RxComputationThreadPool-1
    accept: 设置文本 ：3,thread: id=2,name=main

    accept: doOnNext : 4,thread: id=4083,name=RxComputationThreadPool-1
    accept: 设置文本 ：4,thread: id=2,name=main
     */
    public void startInterval() {
        if (null != mDisposable) {
            return;
        }
        mDisposable = Flowable.interval(1, TimeUnit.SECONDS)  // 不延迟，只要订阅，立刻发送
//        mDisposable = Flowable.interval(10, 1, TimeUnit.SECONDS) //  先延迟10s，再发送心跳
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.d(TAG, "accept: doOnNext : " + aLong + ",thread: id=" + Thread.currentThread().getId() + ",name=" + Thread.currentThread().getName());
                    }
                })
//                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.d(TAG, "accept: 设置文本 ：" + aLong + ",thread: id=" + Thread.currentThread().getId() + ",name=" + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * 销毁时停止心跳
     */
    protected void stopInterval() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
