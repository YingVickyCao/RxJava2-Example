package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TimerExample {
    private static final String TAG = "TimerExample";

    public static void test() {
        way1();
        way2();
    }

    // Recommended
    private static void way1() {
        Log.d(TAG, "test: -->");

        /**
         * When is used: we want to do the work after a certain amount of time, and then emit the value
         */
        /**
         * 2022-10-17 13:23:33.316 5924-5924/D/TimerExample: test: -->
         * 2022-10-17 13:23:33.337 5924-5924/D/TimerExample: test: <--
         * 2022-10-17 13:23:38.352 5924-5957/D/TimerExample: send - apply: 0,RxComputationThreadPool-1
         * 2022-10-17 13:23:38.353 5924-5957/D/TimerExample: send - subscribe: 0,RxComputationThreadPool-1
         * 2022-10-17 13:23:38.356 5924-5958/D/TimerExample: Received:0,RxCachedThreadScheduler-1
         */
        Observable.timer(5, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        // Will go to flatMap after 5 seconds on Schedulers.computation(), not on Schedulers.io()
                        Log.d(TAG, "send - apply: " + aLong + "," + Thread.currentThread().getName());
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "send - subscribe: " + aLong + "," + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(aLong));
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "Received:" + s + "," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test: <--");
    }

    private static void way2() {
        Log.d(TAG, "test: -->");
        /**
         * 2022-10-17 11:59:22.346 21940-21940/D/TimerExample: test: -->
         * 2022-10-17 11:59:22.367 21940-21940/D/TimerExample: test: <--
         * 2022-10-17 11:59:27.373 21940-22014/D/TimerExample: Received:0,RxCachedThreadScheduler-1
         */
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.single())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long value) throws Exception {
                        Log.d(TAG, "Received:" + value + "," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test: <--");
    }
}
