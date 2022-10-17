package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class DelayExample {
    private static final String TAG = "DelayExample";

    /**
     * When is used ? We’re doing some work and then emitting a value, but we want to postpone the value’s emission to the subscriber.
     */
    public static void test() {
//        way1();
//        way2_onNext_onComplete();
//        way2_emit_onError();
//        way3_emit_onNext_onComplete();
        way3_onError();
    }

    // Recommended : do the work, emit the onNext/onComplete -> after 5 seconds -> receive onNext/onComplete
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
                        Log.d(TAG, "Sent: " + Thread.currentThread().getName());
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

    // Recommended : do the work, emit the onNext/onComplete -> after 5 seconds -> receive onNext/onComplete
    public static void way2_onNext_onComplete() {
        Log.d(TAG, "test,--> ");
        /*
        2022-10-17 15:49:22.845 9363-9363/D/DelayExample: test,-->
        2022-10-17 15:49:22.860 9363-9363/D/DelayExample: test,<--
        2022-10-17 15:49:22.863 9363-9394/D/DelayExample: apply: 0,RxCachedThreadScheduler-1
        2022-10-17 15:49:22.864 9363-9394/D/DelayExample: emit - onNext: 0,RxCachedThreadScheduler-1
        2022-10-17 15:49:22.864 9363-9394/D/DelayExample: emit - onComplete: RxCachedThreadScheduler-1

        2022-10-17 15:49:27.870 9363-9397/D/DelayExample: Received:onNext,0,RxSingleScheduler-1
        2022-10-17 15:49:27.870 9363-9397/D/DelayExample: Received:onComplete,RxSingleScheduler-1
         */
        Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: " + integer + "," + Thread.currentThread().getName());
                        // Do the work, and get the result
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "emit - onNext: " + integer + "," + Thread.currentThread().getName());
                                Log.d(TAG, "emit - onComplete:" + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(integer));
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
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
        Log.d(TAG, "test,<-- ");
    }

    // Recommended : Do the work, emit the onError -> immediately -> receive onError
    public static void way2_emit_onError() {
        Log.d(TAG, "test,--> ");
        /*
        2022-10-17 15:54:25.469 9738-9738/D/DelayExample: test,-->
        2022-10-17 15:54:25.485 9738-9738/D/DelayExample: test,<--
        2022-10-17 15:54:25.488 9738-9776/D/DelayExample: send - apply: 0,RxCachedThreadScheduler-1
        2022-10-17 15:54:25.494 9738-9776/D/DelayExample: emit - onNext: 0,RxCachedThreadScheduler-1
        2022-10-17 15:54:25.494 9738-9776/D/DelayExample: emit - onError: RxCachedThreadScheduler-1

        2022-10-17 15:54:25.500 9738-9778/D/DelayExample: Received:onError,Delay error,RxSingleScheduler-1
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
                                Log.d(TAG, "emit - onNext: " + integer + "," + Thread.currentThread().getName());
                                Log.d(TAG, "emit - onError: " + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(integer));
                                emitter.onError(new Exception("Delay error"));
                            }
                        });
                    }
                })
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
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
        Log.d(TAG, "test,<-- ");
    }

    // Depressed : After 5 seconds -> do the work, then  emit the onNext / onComplete
    // This way works as same as timer
    public static void way3_emit_onNext_onComplete() {
        Log.d(TAG, "test,--> ");
        /*
        2022-10-17 16:01:53.456 10144-10144/D/DelayExample: test,-->
        2022-10-17 16:01:53.469 10144-10144/D/DelayExample: test,<--

        2022-10-17 16:01:58.475 10144-10175/D/DelayExample: apply: 0,RxComputationThreadPool-1
        2022-10-17 16:01:58.479 10144-10175/D/DelayExample: emit - onNext: 0,RxComputationThreadPool-1
        2022-10-17 16:01:58.479 10144-10175/D/DelayExample: emit - onComplete: ,RxComputationThreadPool-1
        2022-10-17 16:01:58.482 10144-10176/D/DelayExample: Received:onNext,0,RxSingleScheduler-1
        2022-10-17 16:01:58.482 10144-10176/D/DelayExample: Received:onComplete,RxSingleScheduler-1
         */
        Observable.just(0)
                .delay(5, TimeUnit.SECONDS)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: " + integer + "," + Thread.currentThread().getName());
                        // Do the work, and get the result
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "emit - onNext: " + integer + "," + Thread.currentThread().getName());
                                Log.d(TAG, "emit - onComplete: " + "," + Thread.currentThread().getName());
                                emitter.onNext(String.valueOf(integer));
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
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
        Log.d(TAG, "test,<-- ");
    }

    // Depressed : After 5 seconds -> do the work, then emit onError
    public static void way3_onError() {
        Log.d(TAG, "test,--> ");
        /*
        2022-10-17 16:35:01.670 10475-10475/D/DelayExample: test,-->
        2022-10-17 16:35:01.683 10475-10475/D/DelayExample: test,<--

        2022-10-17 16:35:06.692 10475-10507/D/DelayExample: apply: 0,RxComputationThreadPool-1
        2022-10-17 16:35:06.694 10475-10507/D/DelayExample: emit onError: 0,RxComputationThreadPool-1
        2022-10-17 16:35:06.699 10475-10508/D/DelayExample: Received:onError,Delay error,RxSingleScheduler-1s
         */
        Observable.just(0)
                .delay(5, TimeUnit.SECONDS)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: " + integer + "," + Thread.currentThread().getName());
                        // Do the work, and get the result
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d(TAG, "emit onError: " + integer + "," + Thread.currentThread().getName());
                                emitter.onError(new Exception("Delay error"));
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
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
        Log.d(TAG, "test,<-- ");
    }
}
