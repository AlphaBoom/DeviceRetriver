package com.anarchy.deviceretriever.modules.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anarchy.deviceretriever.data.source.PermissionInfoSource;

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

    public HomePresenter(@NonNull HomeContract.View view, @NonNull PermissionInfoSource infoSource) {
        mView = view;
        mSource = infoSource;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void checkUnGrantedPermissions() {

    }

    @Override
    public void requestUnGrantedPermissions(Context context, int requestCode) {

    }

    @Override
    public void refresh() {

    }


}
