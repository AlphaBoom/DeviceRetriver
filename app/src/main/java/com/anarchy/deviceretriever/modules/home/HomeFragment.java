package com.anarchy.deviceretriever.modules.home;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anarchy.deviceretriever.R;
import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.databinding.FragmentHomeBinding;
import com.anarchy.deviceretriever.modules.MainActivity;

import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/25 16:34
 */
public class HomeFragment extends Fragment implements HomeContract.View{
    private static final int REQUEST_CODE = 233;
    private HomeContract.Presenter mPresenter;
    private FragmentHomeBinding mBinding;
    private InfoAdapter mInfoAdapter;
    private Snackbar mSnackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        DrawerLayout drawerLayout = ((MainActivity)getActivity()).getAttachedDrawer();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawerLayout
                ,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mInfoAdapter = new InfoAdapter(mPresenter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(mInfoAdapter);
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refresh();
            }
        });
        mSnackbar = Snackbar.make(mBinding.getRoot(),"",Snackbar.LENGTH_SHORT);
        mPresenter.start();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }


    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_check_permission){
            mPresenter.checkUnGrantedPermissions();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUnGrantedPermissions(String[] unGrantedPermissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(unGrantedPermissions,null)
                .setTitle("ungranted Permission list")
                .setCancelable(true)
                .setPositiveButton("request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.requestUnGrantedPermissions(getActivity(),REQUEST_CODE);
                    }
                })
                .setNegativeButton("cancel",null)
                .show();
    }

    @Override
    public void showAllPermissionGranted() {
        mSnackbar.setText("all permission is granted not require permission");
        mSnackbar.show();
    }

    @Override
    public void showInfos(List<Info> infos) {
        mInfoAdapter.replaceData(infos);
    }

    @Override
    public void showFetchErrorToast(String reason) {
        mSnackbar.setText(reason);
        mSnackbar.show();
    }

    @Override
    public void showRefreshErrorToast(String reason) {
        mSnackbar.setText(reason);
        mSnackbar.show();
    }

    @Override
    public void unableToRequestPermissions() {
        mSnackbar.setText("unable to request permission");
        mSnackbar.show();
    }

    @Override
    public void showRefreshSuccessToast() {
        mSnackbar.setText("refresh success");
        mSnackbar.show();
    }

    @Override
    public void showInfoDetailDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Info Detail")
                .setMessage(message)
                .setCancelable(true)
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();
    }
}
