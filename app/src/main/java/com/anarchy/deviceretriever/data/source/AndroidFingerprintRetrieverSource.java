package com.anarchy.deviceretriever.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.data.source.retriever.AppRetriever;
import com.anarchy.deviceretriever.data.source.retriever.DeviceRetriever;
import com.anarchy.deviceretriever.data.source.retriever.LocationRetriever;
import com.anarchy.deviceretriever.data.source.retriever.NetworkRetriever;
import com.anarchy.deviceretriever.data.source.retriever.PermissionRetriever;
import com.anarchy.deviceretriever.data.source.retriever.Retriever;
import com.anarchy.deviceretriever.data.source.retriever.TelephoneRetriever;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:20
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class AndroidFingerprintRetrieverSource implements PermissionInfoSource {
    private final List<PermissionRetriever> mRetrievers;
    private boolean mIgnoreUnGrantedPermission;

    /**
     * 获取默认的Android 指纹提供者
     *
     * @param context 不能为空
     * @return
     */
    public static AndroidFingerprintRetrieverSource getDefault(@NonNull Context context) {
        return getDefault(context, true);
    }

    public static AndroidFingerprintRetrieverSource getDefault(@NonNull Context context, boolean ignoreUnGrantedPermission) {
        Builder builder = new Builder()
                .addPermissionRetriever(new AppRetriever(context))
                .addPermissionRetriever(new DeviceRetriever(context))
                .addPermissionRetriever(new LocationRetriever(context))
                .addPermissionRetriever(new NetworkRetriever(context))
                .addPermissionRetriever(new TelephoneRetriever(context));
        return new AndroidFingerprintRetrieverSource(builder, ignoreUnGrantedPermission);
    }

    private AndroidFingerprintRetrieverSource(Builder builder, boolean ignoreUnGrantedPermission) {
        mRetrievers = Collections.unmodifiableList(builder.permissionRetrievers());
        mIgnoreUnGrantedPermission = ignoreUnGrantedPermission;
    }

    private List<Info> getAllInfo() {
        List<Info> result = new ArrayList<>();
        for (PermissionRetriever retriever : mRetrievers) {
            List<Info> infos = retriever.retrieve(mIgnoreUnGrantedPermission);
            if (infos != null) {
                result.addAll(infos);
            }
        }
        return result;
    }

    public String[] checkUnGrantedPermission() {
        List<String> unGrantedPermissions = new ArrayList<>();
        for (PermissionRetriever retriever : mRetrievers) {
            String[] permissions = retriever.checkUnGrantedPermission();
            for (String permission : permissions) {
                unGrantedPermissions.add(permission);
            }


        }
        return unGrantedPermissions.toArray(new String[0]);
    }

    @Override
    public Observable<List<Info>> getInfoList() {
        return Observable.defer(new Func0<Observable<List<Info>>>() {
            @Override
            public Observable<List<Info>> call() {
                return Observable.just(getAllInfo());
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public String getSourceType() {
        return "AndroidFingerprint";
    }


    public static class Builder {
        private final List<PermissionRetriever> mRetrievers = new ArrayList<>();
        private boolean shouldIgnore = true;

        public Builder shouldIgnore(boolean ignore) {
            shouldIgnore = ignore;
            return this;
        }

        public Builder addPermissionRetriever(PermissionRetriever permissionRetriever) {
            mRetrievers.add(permissionRetriever);
            return this;
        }

        public List<PermissionRetriever> permissionRetrievers() {
            return mRetrievers;
        }

        public AndroidFingerprintRetrieverSource build() {
            return new AndroidFingerprintRetrieverSource(this, shouldIgnore);
        }
    }
}
