package com.hades.android.example.rxjava2._1_observable;

import com.hades.android.example.rxjava2.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class Example {
    private static final String TAG = "Example";

    public static void main(String[] args) {
        Example example = new Example();
        example.test_onComplete();
//        example.test_onComplete2();
//        example.test_onError();
//        example.test_void();
    }

    /*
     Example:onSubscribe:
     Example:onNext: 1
     Example:onNext: 2
     Example:onComplete:
     */
    private void test_onComplete() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
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

    /*
    Example:onSubscribe:
    Example:onNext: 1
    Example:onNext: 2
    Example:onNext: 3
    Example:onNext: 4
    Example:onNext: 5
     */
    private void test_onComplete2() {
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
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
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

    /*
    Example:onSubscribe:
    Example:onNext: 1
    Example:onNext: 2
    Example:onError: error:stop emit
     */
    private void test_onError() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("error:stop emit"));
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
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

    private void test_void() {
        Observable.create(new ObservableOnSubscribe<Unit>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Unit> emitter) throws Exception {
                emitter.onNext(Unit.INSTANCE);
                emitter.onComplete();
            }
        })
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull Unit integer) {
                        Log.d(TAG, "onNext: " + integer);
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
