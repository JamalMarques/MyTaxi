package com.mytaxi.app.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.mytaxi.app.R;

public abstract class BaseActivity extends AppCompatActivity implements FragmentController<BaseFragment> {

    private CoordinatorLayout coordinatorLayout;

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public void setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    /**
     * Method used to set default container
     */
    public void setContentView() {
        setContentView(0);
    }

    /**
     * Set container with specific layout resource inside
     *
     * @param layoutResID resource id to be inflated inside
     */
    @Override
    public void setContentView(int layoutResID) {
        ViewGroup coreLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base_layout, null);
        FrameLayout contentFrame = coreLayout.findViewById(R.id.content_frame);

        /*Coordinator setup*/
        setCoordinatorLayout(coreLayout.findViewById(R.id.coordinator_layout));

        if (layoutResID != 0) {
            contentFrame.addView(getLayoutInflater().inflate(layoutResID, null));
        }
        super.setContentView(coreLayout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private String getTAGatTopStack() {
        return (getSupportFragmentManager().getBackStackEntryCount() > 0) ?
                getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() :
                "";
    }

    @Override
    public void changeFragmentContained(BaseFragment fragment, String TAG) {
        changeFragmentContainedBase(fragment, TAG, true, false);
    }

    @Override
    public void changeFragmentContainedNoBackStack(BaseFragment fragment, String TAG) {
        changeFragmentContainedBase(fragment, TAG, false, false);
    }

    @Override
    public void changeFragmentContainedForceNew(BaseFragment fragment, String TAG) {
        changeFragmentContainedBase(fragment, TAG, true, true);
    }

    @Override
    public void changeFragmentContainedForceNewNoBackStack(BaseFragment fragment, String TAG) {
        changeFragmentContainedBase(fragment, TAG, false, true);
    }

    @Override
    public void changeFragmentContainedClearStack(BaseFragment fragment, String TAG) {
        if ((fragment == null && TAG == null) || !getTAGatTopStack().equals(TAG)) {
            /*Clear fragment stack*/
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
            /* Load fragment */
            if (fragment != null && TAG != null) {
                changeFragmentContainedBase(fragment, TAG, true, true);
            }
        }
    }

    private void changeFragmentContainedBase(BaseFragment fragment, String TAG, boolean addToStack, boolean forceNew) {
        Fragment existentFragment = getSupportFragmentManager().findFragmentByTag(TAG);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (existentFragment == null || forceNew) {
            if (addToStack) {
                fragmentTransaction.replace(R.id.content_frame, fragment, TAG).addToBackStack(TAG).commit();
            } else {
                fragmentTransaction.replace(R.id.content_frame, fragment, TAG).commit();
            }
        } else {
            if (!existentFragment.isVisible()) {
                getSupportFragmentManager().popBackStackImmediate(TAG, 0);
            }
        }
    }
}
