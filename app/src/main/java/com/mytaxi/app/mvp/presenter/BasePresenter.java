package com.mytaxi.app.mvp.presenter;

import com.mytaxi.app.mvp.contract.base.PresenterCoreInterface;
import com.mytaxi.app.mvp.contract.base.RetrofitManager;

public class BasePresenter<V, M extends RetrofitManager> implements PresenterCoreInterface {

    protected final V view;
    protected final M model;

    public BasePresenter(V view, M model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onDestroy() {
        /*Stop all cancellable requests*/
        model.stopCancellableRetrofitRequest();
    }
}