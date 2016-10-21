package com.anarchy.deviceretriever.data.source.retriever;

import com.anarchy.deviceretriever.data.Info;

import java.util.List;

import rx.Observable;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:23
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public interface Retriever {

    List<Info> retrieve();
}
