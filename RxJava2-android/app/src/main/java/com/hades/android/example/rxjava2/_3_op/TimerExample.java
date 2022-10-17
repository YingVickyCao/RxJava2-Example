package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * When is used: we want to do the work after a certain amount of time, and then emit the value
 */
public class TimerExample {
    private static final String TAG = "TimerExample";

    public static void test() {
//        way1_emit_onNext();
//        way1_emit_onComplete();
        way1_emit_onError();
//        way2();
    }

    // Recommended : after 5 seconds -> do the work, then emit onNext
    private static void way1_emit_onNext() {
        Log.d(TAG, "test: -->");

      /*
        2022-10-17 15:38:08.791 8524-8524/D/TimerExample: test: -->
        2022-10-17 15:38:08.806 8524-8524/D/TimerExample: test: <--

        2022-10-17 15:38:13.925 8524-8565/D/TimerExample: send - apply: 0,RxComputationThreadPool-1
        2022-10-17 15:38:13.926 8524-8565/D/TimerExample: emit - onNext: 0,RxComputationThreadPool-1
        2022-10-17 15:38:13.930 8524-8566/D/TimerExample: Received:onNext,0,RxCachedThreadScheduler-1
       */
        Observable.timer(5, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        // Work
                        Log.d(TAG, "send - apply: " + aLong + "," + Thread.currentThread().getName());
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "emit - onNext: " + aLong + "," + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(aLong));
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String onNext) throws Exception {
                        Log.d(TAG, "Received:onNext," + onNext + "," + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "Received:onError," + throwable.getMessage() + "," + Thread.currentThread().getName());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "Received:onComplete," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test: <--");
    }

    // Recommended : after 5 seconds -> do the work, then emit onComplete
    private static void way1_emit_onComplete() {
        Log.d(TAG, "test: -->");

        /*
        2022-10-17 15:40:14.758 8661-8661/D/TimerExample: test: -->
        2022-10-17 15:40:14.769 8661-8661/D/TimerExample: test: <--

        2022-10-17 15:40:19.780 8661-8698/D/TimerExample: send - apply: 0,RxComputationThreadPool-1
        2022-10-17 15:40:19.781 8661-8698/D/TimerExample: emit - onNext: 0,RxComputationThreadPool-1
        2022-10-17 15:40:19.785 8661-8699/D/TimerExample: Received:onNext,0,RxCachedThreadScheduler-1
        2022-10-17 15:40:19.785 8661-8699/D/TimerExample: Received:onComplete,RxCachedThreadScheduler-1
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
                                Log.d(TAG, "emit - onNext: " + aLong + "," + Thread.currentThread().getName());
//                                Log.d(TAG, "emit - onComplete: " + aLong + "," + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(aLong));
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String onNext) throws Exception {
                        Log.d(TAG, "Received:onNext," + onNext + "," + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "Received:onError," + throwable.getMessage() + "," + Thread.currentThread().getName());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "Received:onComplete," + Thread.currentThread().getName());
                    }
                });
        Log.d(TAG, "test: <--");
    }

    // Recommended : after 5 seconds -> do the work, then emit onError
    private static void way1_emit_onError() {
        Log.d(TAG, "test: -->");

        /*
        2022-10-17 15:42:48.508 8936-8936/D/TimerExample: test: -->
        2022-10-17 15:42:48.522 8936-8936/D/TimerExample: test: <--

        2022-10-17 15:42:53.527 8936-8969/D/TimerExample: send - apply: 0,RxComputationThreadPool-1
        2022-10-17 15:42:53.528 8936-8969/D/TimerExample: send - onError: 0,RxComputationThreadPool-1
        2022-10-17 15:42:53.531 8936-8970/D/TimerExample: Received:onError,Timer error,RxCachedThreadScheduler-1
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
                                Log.d(TAG, "send - onError: " + aLong + "," + Thread.currentThread().getName());
                                emitter.onError(new Exception("Timer error"));
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String onNext) throws Exception {
                        Log.d(TAG, "Received:onNext," + onNext + "," + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "Received:onError," + throwable.getMessage() + "," + Thread.currentThread().getName());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "Received:onComplete," + Thread.currentThread().getName());
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
