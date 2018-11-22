package com.mytaxi.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mytaxi.app.R;
import com.mytaxi.app.base.BaseFragment;
import com.mytaxi.app.mvp.contract.UsersContract;
import com.mytaxi.app.mvp.model.UsersModel;
import com.mytaxi.app.mvp.presenter.UsersPresenter;
import com.mytaxi.app.mvp.view.UsersView;
import com.mytaxi.app.utils.BusProvider;


public class UsersFragment extends BaseFragment<UsersContract.Presenter> {

    public static final String TAG = "UsersFragment";

    /*Extra data keys*/
    public static final String SEED = "seed";

    public static UsersFragment getInstance(String seed) {
        UsersFragment fragment = new UsersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEED, seed);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users_layout, container, false);
    }

    @Override
    protected UsersContract.Presenter getPresenter() {
        return presenter = new UsersPresenter(
                new UsersView(getBaseActivity(), BusProvider.getInstance()),
                new UsersModel(BusProvider.getInstance(), getArguments().getString(SEED), 1, 30));
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.register(presenter);
        presenter.onStart();
    }

    @Override
    public void onStop() {
        BusProvider.unregister(presenter);
        super.onStop();
    }
}
