package com.mytaxi.app.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.mytaxi.app.mvp.contract.PresenterCoreInterface;

public abstract class BaseFragment<P extends PresenterCoreInterface> extends Fragment implements FragmentController<BaseFragment> {

    protected P presenter;

    protected abstract P getPresenter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = getPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    protected BaseActivity getBaseActivity() {
        try {
            if (getActivity() instanceof BaseActivity) {
                return (BaseActivity) getActivity();
            } else {
                throw new Exception("Fragments extended from BaseFragment must be hosted in a BaseActivity");
            }
        } catch (Exception e) {
            Log.e("getBaseActivity()", e.getMessage());
        }

        return null;
    }

    @Override
    public void changeFragmentContained(BaseFragment fragment, String TAG) {
        getBaseActivity().changeFragmentContained(fragment, TAG);
    }

    @Override
    public void changeFragmentContainedNoBackStack(BaseFragment fragment, String TAG) {

        getBaseActivity().changeFragmentContainedNoBackStack(fragment, TAG);
    }

    @Override
    public void changeFragmentContainedForceNew(BaseFragment fragment, String TAG) {
        getBaseActivity().changeFragmentContainedForceNew(fragment, TAG);
    }

    @Override
    public void changeFragmentContainedForceNewNoBackStack(BaseFragment fragment, String TAG) {
        getBaseActivity().changeFragmentContainedForceNewNoBackStack(fragment, TAG);
    }

    @Override
    public void changeFragmentContainedClearStack(BaseFragment fragment, String TAG) {
        getBaseActivity().changeFragmentContainedClearStack(fragment, TAG);
    }

    /*Add extra behavior here if needed*/
}
