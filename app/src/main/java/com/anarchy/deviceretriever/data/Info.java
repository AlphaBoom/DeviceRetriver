package com.anarchy.deviceretriever.data;

import java.util.UUID;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/19 16:10
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class Info {
    public String id;
    public String name;
    public String value;
    public String description;
    public String permission;
    public String permissionDesc;

    public Info(String name,String value){
        this(name,value,"");
    }

    public Info(String name,String value,String description){
        this(UUID.randomUUID().toString(),name,value,description,"","");
    }


    public Info(String id, String name, String value, String description, String permission, String permissionDesc) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.description = description;
        this.permission = permission;
        this.permissionDesc = permissionDesc;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", permission='" + permission + '\'' +
                ", permissionDesc='" + permissionDesc + '\'' +
                '}';
    }
}
