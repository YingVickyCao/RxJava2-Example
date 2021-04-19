package com.hades.android.example.rxjava2._2_observer;

import com.hades.android.example.rxjava2.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class Example {
    private static final String TAG = "Example";
    private Disposable mDisposable;

    public static void main(String[] args) {
        Example example = new Example();
        example.test_Disposable();
    }

    /*
    Example:onSubscribe:
    Example:onNext: 1
    Example:onNext: 2
     */
    private void test_Disposable() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                if (null == mDisposable) {
                    mDisposable = d;
                }
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
                if (integer == 2) {
                    if (null != mDisposable && !mDisposable.isDisposed()) {
                        mDisposable.dispose();
                        mDisposable = null;
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }
}
