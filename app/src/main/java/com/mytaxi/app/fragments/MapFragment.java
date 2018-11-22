package com.mytaxi.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mytaxi.app.R;
import com.mytaxi.app.base.BaseFragment;
import com.mytaxi.app.models.Coordinate;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.mvp.model.MapModel;
import com.mytaxi.app.mvp.presenter.MapPresenter;
import com.mytaxi.app.mvp.view.MapView;
import com.mytaxi.app.utils.BusProvider;

public class MapFragment extends BaseFragment<MapContract.Presenter> {

    public static final String TAG = "MapFragment";

    /*Extra data to instance*/
    public static final String P1_LAT_LNG = "P1LatLng";
    public static final String P2_LAT_LNG = "P2LatLng";

    public static MapFragment getInstance(Coordinate point1, Coordinate point2) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(P1_LAT_LNG, point1);
        bundle.putParcelable(P2_LAT_LNG, point2);
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
    protected MapContract.Presenter getPresenter() {
        /*Read data from arguments*/
        Coordinate point1 = getArguments().getParcelable(P1_LAT_LNG);
        Coordinate point2 = getArguments().getParcelable(P1_LAT_LNG);

        return new MapPresenter(
                new MapView(getBaseActivity(), BusProvider.getInstance()),
                new MapModel(BusProvider.getInstance(), point1, point2));
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.register(presenter);
    }

    @Override
    public void onStop() {
        BusProvider.unregister(presenter);
        super.onStop();
    }
}
