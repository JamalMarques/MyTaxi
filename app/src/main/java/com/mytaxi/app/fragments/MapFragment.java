package com.mytaxi.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mytaxi.app.R;
import com.mytaxi.app.base.BaseFragment;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.mvp.model.MapModel;
import com.mytaxi.app.mvp.presenter.MapPresenter;
import com.mytaxi.app.mvp.view.MapView;
import com.mytaxi.app.utils.BusProvider;

public class MapFragment extends BaseFragment<MapContract.Presenter> {

    /*Extra data to instance*/
    public static final String P1_LAT_LNG = "P1LatLng";
    public static final String P2_LAT_LNG = "P2LatLng";

    @Override
    protected MapContract.Presenter getPresenter() {
        return new MapPresenter(
                new MapView(getBaseActivity(), BusProvider.getInstance()),
                new MapModel(BusProvider.getInstance()));
    }

    public static MapFragment getInstance(String p1LatLng, String p2LatLng) {
        Bundle bundle = new Bundle();
        bundle.putString(P1_LAT_LNG, p1LatLng);
        bundle.putString(P2_LAT_LNG, p2LatLng);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_layout, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        //BusProvider.register(presenter);
    }

    @Override
    public void onStop() {
        //BusProvider.unregister(presenter);
        super.onStop();
    }
}
