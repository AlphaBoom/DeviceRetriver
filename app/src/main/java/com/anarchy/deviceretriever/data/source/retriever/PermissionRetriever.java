package com.anarchy.deviceretriever.data.source.retriever;

import com.anarchy.deviceretriever.data.Info;

import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:27
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public interface PermissionRetriever extends Retriever {
    /**
     * 检查所有未获取的权限
     * @return
     */
    String[] checkUnGrantedPermission();

    /**
     * 获取设备信息
     * @param ignorePermission 是否忽略权限
     * @return
     */
    List<Info> retrieve(boolean ignorePermission);
}
