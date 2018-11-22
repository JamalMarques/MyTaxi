package com.mytaxi.app.activities;

import android.os.Bundle;

import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.fragments.UsersFragment;

public class MainActivity extends BaseActivity {

    private final String seed = "seed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        changeFragmentContainedNoBackStack(UsersFragment.getInstance(seed), UsersFragment.TAG);
    }
}
