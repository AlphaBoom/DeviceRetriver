package com.anarchy.deviceretriever.modules.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anarchy.deviceretriever.R;
import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.databinding.FragmentHomeBinding;

import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 16:34
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public class HomeFragment extends Fragment implements HomeContract.View{

    private HomeContract.Presenter mPresenter;
    private FragmentHomeBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        mPresenter.start();
        return mBinding.getRoot();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUnGrantedPermissions(String[] unGrantedPermissions) {

    }

    @Override
    public void showInfos(List<Info> infos) {

    }
}
