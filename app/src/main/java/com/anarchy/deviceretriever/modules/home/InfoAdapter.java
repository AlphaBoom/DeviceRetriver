package com.anarchy.deviceretriever.modules.home;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anarchy.deviceretriever.R;
import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.databinding.ItemInfoBinding;

import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 17:45
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<Info> mInfoList;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemInfoBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(Info info) {
            mBinding.setInfo(info);
            mBinding.executePendingBindings();
        }
    }
}
