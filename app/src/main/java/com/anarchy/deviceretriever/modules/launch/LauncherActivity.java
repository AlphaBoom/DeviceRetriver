package com.anarchy.deviceretriever.modules.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.anarchy.deviceretriever.R;
import com.anarchy.deviceretriever.modules.MainActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 14:57
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.fade_and_scale_in,R.anim.fade_and_scale_out);
                        finish();
                        overridePendingTransition(R.anim.fade_and_scale_in,R.anim.fade_and_scale_out);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.fade_and_scale_in,R.anim.fade_and_scale_out);
                        finish();
                        overridePendingTransition(R.anim.fade_and_scale_in,R.anim.fade_and_scale_out);
                    }
                });

    }
}
