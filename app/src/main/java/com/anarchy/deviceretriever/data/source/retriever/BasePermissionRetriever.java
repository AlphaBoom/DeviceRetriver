package com.anarchy.deviceretriever.data.source.retriever;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anarchy.deviceretriever.data.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/21 16:16
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

abstract class BasePermissionRetriever implements PermissionRetriever {
    final Context mContext;
    List<Info> mResult;

    BasePermissionRetriever(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public List<Info> retrieve() {
        return prepareRetrieve(true);
    }

    private List<Info> prepareRetrieve(boolean ignorePermission) {
        if (mResult == null) {
            mResult = new ArrayList<>();
        } else {
            mResult.clear();
        }
        return retrieve(ignorePermission);
    }


    String[] addPermission(@NonNull String[] source, String add) {
        int length = source.length + 1;
        String[] result = new String[length];
        System.arraycopy(source, 0, result, 0, source.length);
        result[length - 1] = add;
        return result;
    }
}
