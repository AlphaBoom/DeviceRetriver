package com.anarchy.deviceretriever.modules.home;

import android.app.Activity;
import android.content.Context;

import com.anarchy.deviceretriever.core.BaseView;
import com.anarchy.deviceretriever.data.Info;

import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 16:53
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public interface HomeContract {
    interface View extends BaseView<HomeContract.Presenter>{
        /**
         * 显示需要但未获取的权限
         * @param unGrantedPermissions
         */
        void showUnGrantedPermissions(String[] unGrantedPermissions);

        /**
         * 以获取到全部权限
         */
        void showAllPermissionGranted();

        /**
         * 显示获取到的数据
         * @param infos
         */
        void showInfos(List<Info> infos);

        /**
         * 显示初始化失败提示
         */
        void showFetchErrorToast(String reason);

        /**
         * 显示刷新失败提示
         */
        void showRefreshErrorToast(String reason);

        /**
         * 无法申请权限
         */
        void unableToRequestPermissions();

        /**
         * 显示刷新成功
         */
        void showRefreshSuccessToast();


        /**
         * 显示详情窗口
         */
        void showInfoDetailDialog(String message);

    }
    interface Presenter {
        /**
         * 开始加载
         */
        void start();

        /**
         * 查看未获取到的权限
         */
        void checkUnGrantedPermissions();

        /**
         * 向系统申请权限
         * @param activity
         * @param requestCode  请求code 用于Activity result 验证
         */
        void requestUnGrantedPermissions(Activity activity, int requestCode);

        /**
         * 刷新数据
         */
        void refresh();


        /**
         * 页面关闭
         */
        void destroy();


        /**
         * 目标item 被点击
         * @param info
         */
        void onItemClick(Info info);
    }
}
