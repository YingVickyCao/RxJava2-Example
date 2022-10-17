package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class DelayExample {
    private static final String TAG = "DelayExample";

    /**
     * When is used ? We want to postpone the value’s emission to the subscriber,
     * Conclusion：Observable send a message, then after 5 seconds, consumer receives the message.
     * Log：
     * 2022-10-17 11:26:22.572 20219-20219/D/DelayExample: test,-->
     * 2022-10-17 11:26:22.594 20219-20219/D/DelayExample: test,<--
     * 2022-10-17 11:26:22.595 20219-20274/D/DelayExample: SentRxCachedThreadScheduler-1
     * 2022-10-17 11:26:27.600 20219-20283/D/DelayExample: Received:Delay Message,RxSingleScheduler-1
     */
    public static void test() {
        Log.d(TAG, "test,--> ");
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        Log.d(TAG, "Sent" + Thread.currentThread().getName());
                        emitter.onNext("Delay Message");
                        emitter.onComplete();
                    }
                })
                // Way 1 and Way 2 is the same result
                // Way 1, START
                .subscribeOn(Schedulers.io())
                .delay(5, TimeUnit.SECONDS)
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
}
