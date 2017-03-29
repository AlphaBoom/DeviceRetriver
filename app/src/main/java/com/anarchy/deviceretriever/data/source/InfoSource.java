package com.anarchy.deviceretriever.data.source;

import com.anarchy.deviceretriever.data.Info;

import java.util.List;

import rx.Observable;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/19 16:19
 * <p/>
 */
interface InfoSource {
    /**
     * 获取所有信息
     * @return
     */
    Observable<List<Info>> getInfoList();


    /**
     * 获取信息类别
     * @return
     */
    String getSourceType();
}
