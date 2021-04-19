package com.hades.android.example.rxjava2._3_op;

import android.util.Log;

import com.hades.android.example.rxjava2.bean.StuResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class ZipExample {
    private static final String TAG = ZipExample.class.getSimpleName();

    private String memoryCache_Name;
    private Integer memoryCache_score;

    /*
       Case 1 : all success
        apply:name=abc,score=100
        onNext: StuResponse{name='abc', score=100}
        onComplete:

        Case 2 : on fail
        onError: error when request name
     */
    public void test() {
        Observable<String> response1 = isMemoryCached(memoryCache_Name)
                ? Observable.just(memoryCache_Name)
                : requestName();

        Observable<Integer> response2 = isMemoryCached(memoryCache_score)
                ? Observable.just(memoryCache_score)
                : requestScore();

        Observable.zip(response1, response2, new BiFunction<String, Integer, StuResponse>() {
            @Override
            public StuResponse apply(@NonNull String name, @NonNull Integer score) throws Exception {
                Log.d(TAG, "apply:name=" + name + ",score=" + score);
                memoryCache_Name = name;
                memoryCache_score = score;
                StuResponse response = new StuResponse(name, score);
                return response;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new Observer<StuResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull StuResponse stuResponse) {
                        Log.d(TAG, "onNext: " + stuResponse.toString());
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

    private boolean isMemoryCached(String name) {
        return name != null;
    }

    private boolean isMemoryCached(Integer score) {
        return score != null;
    }

    private Observable<String> requestName() {
//        return Observable.just("abc");
        return Observable.error(new Exception("error when request name"));
    }

    private Observable<Integer> requestScore() {
        return Observable.just(100);
    }
}
