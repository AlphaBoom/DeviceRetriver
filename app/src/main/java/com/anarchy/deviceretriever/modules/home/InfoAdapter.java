package com.anarchy.deviceretriever.modules.home;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private final HomeContract.Presenter mPresenter;

    public InfoAdapter(HomeContract.Presenter presenter){
        mPresenter = presenter;
    }

    public void replaceData(List<Info> listInfo){
        mInfoList = listInfo;
        notifyItemRangeChanged(0,mInfoList.size()-1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        Log.d("wzd","create view holder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("wzd","bind view holder");
        final Info info = mInfoList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onItemClick(info);
            }
        });
        holder.bind(info);
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
