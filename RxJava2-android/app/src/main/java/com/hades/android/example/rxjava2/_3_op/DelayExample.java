package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class DelayExample {
    private static final String TAG = "DelayExample";

    // When is used ? We’re doing some work and then emitting a value, but we want to postpone the value’s emission to the subscriber.
    public static void test() {
//        way1();
//        way2();
        way3();
    }

    // do the work -> after 5 seconds -> emit the result
    public static void way1() {
        Log.d(TAG, "test,--> ");
        /**
         * Log：
         * 2022-10-17 11:26:22.572 20219-20219/D/DelayExample: test,-->
         * 2022-10-17 11:26:22.594 20219-20219/D/DelayExample: test,<--
         * 2022-10-17 11:26:22.595 20219-20274/D/DelayExample: SentRxCachedThreadScheduler-1
         *
         * 2022-10-17 11:26:27.600 20219-20283/D/DelayExample: Received:Delay Message,RxSingleScheduler-1
         */
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        // Do the work, and get the result
                        Log.d(TAG, "Sent" + Thread.currentThread().getName());
                        emitter.onNext("Delay Message");
                        emitter.onComplete();
                    }
                })
                // Way 1 and Way 2 is the same result
                // Way 1, START
                .subscribeOn(Schedulers.io())
                .delay(5, TimeUnit.SECONDS) // After 5 seconds, send the result to consumer
                // Way 1, END
                // Way 2, START
//                .delay(5, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
                // Way 2, END
                .observeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "Received:" + s + "," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test,<-- ");
    }

    // Do the work -> then after 5 seconds -> emit the result
    public static void way2() {
        Log.d(TAG, "test,--> ");
        /*

        2022-10-17 14:36:05.246 6832-6832/D/DelayExample: test,-->
        2022-10-17 14:36:05.294 6832-6832/D/DelayExample: test,<--
        2022-10-17 14:36:05.299 6832-6864/D/DelayExample: send - apply: 0,RxCachedThreadScheduler-1
        2022-10-17 14:36:05.301 6832-6864/D/DelayExample: send - subscribe: 0,RxCachedThreadScheduler-1

        2022-10-17 14:36:10.303 6832-6866/D/DelayExample: Received:0,RxSingleScheduler-1
         */
        Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "send - apply: " + integer + "," + Thread.currentThread().getName());
                        // Do the work, and get the result
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "send - subscribe: " + integer + "," + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(integer));
                            }
                        });
                    }
                })
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "Received:" + s + "," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test,<-- ");
    }

    // After 5 seconds -> do the work, then  emit the result
    public static void way3() {
        Log.d(TAG, "test,--> ");
        /*
        2022-10-17 14:38:34.036 7093-7093/D/DelayExample: test,-->
        2022-10-17 14:38:34.052 7093-7093/D/DelayExample: test,<--

        2022-10-17 14:38:39.058 7093-7126/D/DelayExample: send - apply: 0,RxComputationThreadPool-1
        2022-10-17 14:38:39.059 7093-7126/D/DelayExample: send - subscribe: 0,RxComputationThreadPool-1
        2022-10-17 14:38:39.061 7093-7127/D/DelayExample: Received:0,RxSingleScheduler-1
         */
        Observable.just(0)
                .delay(5, TimeUnit.SECONDS)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "send - apply: " + integer + "," + Thread.currentThread().getName());
                        // Do the work, and get the result
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "send - subscribe: " + integer + "," + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(integer));
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "Received:" + s + "," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test,<-- ");
    }
}
