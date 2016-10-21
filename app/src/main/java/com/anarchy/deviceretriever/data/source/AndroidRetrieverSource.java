package com.anarchy.deviceretriever.data.source;

import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.data.source.InfoSource;

import java.util.List;

import rx.Observable;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:20
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class AndroidRetrieverSource implements InfoSource {
    @Override
    public Observable<List<Info>> getInfoList() {
        return null;
    }

    @Override
    public String getSourceType() {
        return null;
    }
}
