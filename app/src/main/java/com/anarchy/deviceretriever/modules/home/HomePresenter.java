package com.anarchy.deviceretriever.modules.home;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.data.source.PermissionInfoSource;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 17:02
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View mView;
    private final PermissionInfoSource mSource;
    private Subscription mSubscription;

    public HomePresenter(@NonNull HomeContract.View view, @NonNull PermissionInfoSource infoSource) {
        mView = view;
        mSource = infoSource;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = mSource.getInfoList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Info>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showFetchErrorToast(e == null ? "" : e.getMessage());
                        if (e != null)
                            e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Info> list) {
                        if (list != null) {
                            mView.showInfos(list);
                        } else {
                            mView.showFetchErrorToast("fetch failed,source return null");
                        }
                    }
                });
    }

    @Override
    public void checkUnGrantedPermissions() {
        String[] unGrantedPermissions = mSource.checkUnGrantedPermission();
        if (unGrantedPermissions == null || unGrantedPermissions.length == 0) {
            mView.showAllPermissionGranted();
        } else {
            mView.showUnGrantedPermissions(unGrantedPermissions);
        }
    }

    @Override
    public void requestUnGrantedPermissions(Activity activity, int requestCode) {
        String[] unGrantedPermissions = mSource.checkUnGrantedPermission();
        if (unGrantedPermissions == null || unGrantedPermissions.length == 0) {
            mView.showAllPermissionGranted();
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                activity.requestPermissions(unGrantedPermissions, requestCode);
            } else {
                mView.unableToRequestPermissions();
            }
        }
    }

    @Override
    public void refresh() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = mSource.getInfoList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Info>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefreshErrorToast(e == null ? "" : e.getMessage());
                        if(e != null)
                            e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Info> list) {
                        if (list != null) {
                            mView.showInfos(list);
                            mView.showRefreshSuccessToast();
                        } else {
                            mView.showRefreshErrorToast("refresh failed,source return null");
                        }
                    }
                });
    }


    @Override
    public void destroy() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }


}
