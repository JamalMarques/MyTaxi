package com.mytaxi.app.activities;

import android.os.Bundle;

import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.commons.Constants;
import com.mytaxi.app.fragments.MapFragment;
import com.mytaxi.app.models.Coordinate;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        changeFragmentContainedNoBackStack(MapFragment.getInstance(Constants.HAMBURG_COORDINATE), MapFragment.TAG);
    }
}
