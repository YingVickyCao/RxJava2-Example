package com.hades.android.example.rxjava2._2_thread;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.hades.android.example.rxjava2.LogHelper;
import com.hades.android.example.rxjava2.R;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ThreadActivity extends Activity {
    private static final String TAG = ThreadActivity.class.getSimpleName();

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: -->");

        setContentView(R.layout.activity_thread);
        progressBar = findViewById(R.id.loading);
        findViewById(R.id.subscribeOn_vs_observeOn).setOnClickListener(view -> test_subscribeOn_vs_observeOn());
        Log.d(TAG, "onCreate: <--");
    }

    /**
     * <pre>
     *     Observable
     *     .map                    // 操作1   io
     *     .flatMap                // 操作2   io
     *     .subscribeOn(io)        // set 线程 = io
     *     .map                    // 操作3   io
     *     .subscribeOn(computation)// 无效，只有第一个subscribeOn有效。
     *     .flatMap                // 操作4   io
     *     .observeOn(main)        // set 线程 = main
     *     .map                    // 操作5  main
     *     .observeOn(Schedulers.io()) set 线程 = io
     *     .flatMap                // 操作6  io
     *     .subscribeOn(io)        // 无效。只有第一个subscribeOn有效
     *     .subscribe(handleData)  // 操作7  io
     */
    // thread  (result ) ->  main

    /*
    操作7:subscribe() - onSubscribe thread name=main,thread id=2
    操作1: thread name=RxCachedThreadScheduler-3,thread id=3711
    操作2: thread name=RxCachedThreadScheduler-3,thread id=3711
    操作3: thread name=RxCachedThreadScheduler-3,thread id=3711
    操作4: thread name=RxCachedThreadScheduler-3,thread id=3711
    操作5: thread name=main,thread id=2
    操作6: thread name=RxCachedThreadScheduler-4,thread id=3713
    操作7:onNext() - onNext thread name=RxCachedThreadScheduler-4,thread id=3713
    操作7:subscribe() - onComplete thread name=RxCachedThreadScheduler-4,thread id=3713
     */
    private void test_subscribeOn_vs_observeOn() {
        Observable.just(String.valueOf(30))
                .map(new Function<String, Integer>() { // 操作1
                    @Override
                    public Integer apply(String num) throws Exception {
                        Log.d(TAG, "操作1: " + LogHelper.getThreadInfo());
                        return Integer.valueOf(num) > 10 ? Integer.valueOf(num) : 15;
                    }
                })
                .flatMap(new Function<Integer, ObservableSource<String>>() {// 操作2
                    @Override
                    public ObservableSource<String> apply(Integer s) throws Exception {
                        Log.d(TAG, "操作2: " + LogHelper.getThreadInfo());
                        return Observable.just(String.valueOf(s));
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Integer>() { // 操作3
                         @Override
                         public Integer apply(String num) throws Exception {
                             Log.d(TAG, "操作3: " + LogHelper.getThreadInfo());
                             return Integer.valueOf(num) > 10 ? Integer.valueOf(num) : 15;
                         }
                     }
                )
                .subscribeOn(Schedulers.computation())
                .flatMap(new Function<Integer, ObservableSource<String>>() { //操作4
                    @Override
                    public ObservableSource<String> apply(Integer s) throws Exception {
                        Log.d(TAG, "操作4: " + LogHelper.getThreadInfo());
                        return Observable.just(String.valueOf(s));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, Integer>() { // 操作5
                    @Override
                    public Integer apply(String num) throws Exception {
                        Log.d(TAG, "操作5: " + LogHelper.getThreadInfo());
                        return Integer.valueOf(num) > 10 ? Integer.valueOf(num) : 15;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<String>>() {//操作6
                    @Override
                    public ObservableSource<String> apply(Integer s) throws Exception {
                        Log.d(TAG, "操作6: " + LogHelper.getThreadInfo());
                        return Observable.just(String.valueOf(s));
                    }
                })
                .subscribeOn(Schedulers.io())        //!!特别注意
                .subscribe(new Observer<String>() { // 操作7
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "操作7:subscribe() - onSubscribe " + LogHelper.getThreadInfo());

                    }

                    @Override
                    public void onNext(String num) {
                        Log.d(TAG, "操作7:onNext() - onNext " + LogHelper.getThreadInfo());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "操作7:subscribe() - onError " + LogHelper.getThreadInfo());

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "操作7:subscribe() - onComplete " + LogHelper.getThreadInfo());
                    }
                });
    }

    private void counter(String num) {

    }
}
