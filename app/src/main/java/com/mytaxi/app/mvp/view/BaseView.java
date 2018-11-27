package com.mytaxi.app.mvp.view;

import android.content.Context;
import android.content.Intent;

import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.base.FragmentController;
import com.mytaxi.app.utils.BusProvider;

import java.lang.ref.WeakReference;

public class BaseView {

    private WeakReference<BaseActivity> activityReference;
    protected BusProvider.Bus bus;

    public BaseView(BaseActivity activity, BusProvider.Bus bus) {
        this.activityReference = new WeakReference<>(activity);
        this.bus = bus;
    }

    public BaseActivity getActivity() {
        return this.activityReference.get();
    }

    public Context getContext() {
        return this.getActivity();
    }

    public void post(Object event) {
        bus.post(event);
    }
}