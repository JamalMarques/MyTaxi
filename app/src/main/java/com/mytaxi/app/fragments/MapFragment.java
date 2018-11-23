package com.mytaxi.app.fragments;

import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.util.Locale;

public class MapFragment extends BaseFragment<MapContract.Presenter> {

    public static final String TAG = "MapFragment";

    /*Extra data to instance*/
    public static final String P1_LAT_LNG = "P1LatLng";
    public static final String P2_LAT_LNG = "P2LatLng";

    private Bundle lastInstantStateSaved;

    /**
     * Fragment instance to center on specific coordinate
     *
     * @param startingCoordinate specific latitude and longitude for starting map
     */
    public static MapFragment getInstance(Coordinate startingCoordinate) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(P1_LAT_LNG, startingCoordinate);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Fragment instance to center on specific area based on coordinate points
     *
     * @param northEast north east point
     * @param southWest south west point
     */
    public static MapFragment getInstance(Coordinate northEast, Coordinate southWest) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(P1_LAT_LNG, northEast);
        bundle.putParcelable(P2_LAT_LNG, southWest);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lastInstantStateSaved = savedInstanceState;
        return inflater.inflate(R.layout.map_fragment_layout, container, false);
    }

    @Override
    protected MapContract.Presenter getPresenter() {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        return new MapPresenter(
                new MapView(getBaseActivity(), BusProvider.getInstance(), lastInstantStateSaved),
                getArguments().getParcelable(P2_LAT_LNG) == null ?
                        new MapModel(BusProvider.getInstance(),
                                new Handler(Looper.getMainLooper()),
                                getArguments().getParcelable(P1_LAT_LNG),
                                geocoder) :
                        new MapModel(BusProvider.getInstance(),
                                new Handler(Looper.getMainLooper()),
                                getArguments().getParcelable(P1_LAT_LNG),
                                getArguments().getParcelable(P2_LAT_LNG),
                                geocoder));
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

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstantState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        presenter.onLowMemory();
    }
}
