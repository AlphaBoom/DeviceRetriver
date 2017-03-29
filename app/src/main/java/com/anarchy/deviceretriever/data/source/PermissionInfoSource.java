package com.anarchy.deviceretriever.data.source;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 17:32
 * <p/>
 */

public interface PermissionInfoSource extends InfoSource {
    /**
     * 查看未获取的权限
     * @return
     */
    String[] checkUnGrantedPermission();
}
