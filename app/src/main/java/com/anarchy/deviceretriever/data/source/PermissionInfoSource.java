package com.anarchy.deviceretriever.data.source;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 17:32
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public interface PermissionInfoSource extends InfoSource {
    /**
     * 查看未获取的权限
     * @return
     */
    String[] checkUnGrantedPermission();
}
