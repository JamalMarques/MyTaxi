package com.mytaxi.app.mvp.view;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytaxi.app.R;
import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.customViews.TopBanner;
import com.mytaxi.app.customViews.VehicleBottomSheet;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.utils.BusProvider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapView extends BaseView implements MapContract.View {

    @BindView(R.id.vehicle_bottom_sheet) VehicleBottomSheet bottomSheet;
    @BindView(R.id.map_view) com.google.android.gms.maps.MapView mapView;
    @BindView(R.id.top_banner) TopBanner topBanner;

    private GoogleMap googleMap;
    private HashMap<Vehicle, Marker> currentMarkers = new HashMap<>();
    private int lastMovedReason = -1;

    public MapView(BaseActivity activity, BusProvider.Bus bus, Bundle savedInstantState) {
        super(activity, bus);
        ButterKnife.bind(this, activity);

        /*Bottom sheet setup*/
        prepareBottomSheet();

        /*Map setup*/
        mapView.onCreate(savedInstantState);
        mapView.getMapAsync(googleMap -> {
            MapView.this.googleMap = googleMap;
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            prepareMapListeners(googleMap);
            post(new OnMapLoaded());
        });
    }


    private void prepareBottomSheet() {
        bottomSheet.setOnHeaderClicked(v -> {
            bottomSheet.setSheetState((bottomSheet.getSheetState() == BottomSheetBehavior.STATE_COLLAPSED) ?
                    BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
        });

        bottomSheet.setOnListItemClicked((view, position, vehicle) -> {
            /*Moving camera & showing marker's window info*/
            lastMovedReason = GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION;
            Marker marker = currentMarkers.get(vehicle);
            marker.showInfoWindow();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
            post(new OnVehicleItemClicked(vehicle));
        });
    }

    private void prepareMapListeners(GoogleMap googleMap) {
        /*Camera idle*/
        googleMap.setOnCameraIdleListener(() -> {
            /*Send event only if was moved by the user or certain dev animations*/
            if (lastMovedReason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE ||
                    lastMovedReason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
                post(new OnCameraMovedByUser(googleMap.getProjection().getVisibleRegion().latLngBounds));
            }
            /*Reset state for next call*/
            lastMovedReason = -1;
        });
        /*Camera movement*/
        googleMap.setOnCameraMoveStartedListener(reason -> {
            /*View modifications*/
            bottomSheet.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
            /*Save state reason*/
            if (lastMovedReason == -1) {
                /*Only change it if any other previous flow in the view wanted to change the idle result*/
                lastMovedReason = reason;
            }
        });
        /*Marker listener*/
        googleMap.setOnMarkerClickListener(marker -> {
            for (Map.Entry<Vehicle, Marker> entry : currentMarkers.entrySet()) {
                if (entry.getValue().getId().equals(marker.getId())) {
                    post(new OnMarkerClicked(entry.getKey()));
                    break;
                }
            }
            /*Always return false to keep default functionality
             * of focus+zoom*/
            return false;
        });
    }

    @Override
    public void onStartMap() {
        mapView.onStart();
    }

    @Override
    public void onResumeMap() {
        mapView.onResume();
    }

    @Override
    public void onPauseMap() {
        mapView.onPause();
    }

    @Override
    public void onStopMap() {
        mapView.onStop();
    }

    @Override
    public void onDestroyMap() {
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstantStateMap(Bundle bundle) {
        mapView.onSaveInstanceState(bundle);
    }

    @Override
    public void onLowMemoryMap() {
        mapView.onLowMemory();
    }

    @Override
    public void refreshVehiclesComponent(List<Vehicle> vehiclesList) {
        /* Clear markers
         * Only new markers are going to be created, already existent
         * ones will be untouched and repositioned if needed to improve rendering of not changed markers
         *
         * Note: in the current implementation this advantage is not appreciated due that the mock API always returns
         *          random data with different vehicles no matter what.
         * */
        DecimalFormat df = new DecimalFormat(".######");
        List<Vehicle> vehiclesToAdd = new ArrayList<>(vehiclesList);
        List<Vehicle> markersToRemove = new ArrayList<>();
        for (Map.Entry<Vehicle, Marker> entry : currentMarkers.entrySet()) {
            boolean foundStored = false;
            if (vehiclesToAdd.contains(entry.getKey())) {
                foundStored = true;

                /*Find vehicle updated*/
                for (Vehicle vehicleUpdatedCoords : vehiclesToAdd) {
                    if (vehicleUpdatedCoords.equals(entry.getKey())) {

                        /*Update coordinates if needed */
                        Marker storedMarker = entry.getValue();
                        if (!df.format(storedMarker.getPosition().latitude).equals(df.format(vehicleUpdatedCoords.getCoordinate().getLatitude())) ||
                                !df.format(storedMarker.getPosition().longitude).equals(df.format(vehicleUpdatedCoords.getCoordinate().getLongitude()))) {
                            /*Coordinates updated*/
                            storedMarker.setPosition(vehicleUpdatedCoords.getCoordinate().getLatLng());
                        }
                        break;
                    }
                }
            }

            if (foundStored) {
                /*Vehicle found stored - remove from list to new markers*/
                vehiclesToAdd.remove(entry.getKey());
            } else {
                /*Vehicle no longer visible in area - so remove it*/
                markersToRemove.add(entry.getKey());
            }
        }

        /*Delete outdated vehicles*/
        for (Vehicle vehicle : markersToRemove) {
            currentMarkers.get(vehicle).remove();
            currentMarkers.remove(vehicle);
        }

        /*Adding markers*/
        for (Vehicle vehicle : vehiclesToAdd) {
            currentMarkers.put(
                    vehicle,
                    googleMap.addMarker(new MarkerOptions()
                            .position(vehicle.getCoordinate().getLatLng())
                            .title(vehicle.getFleetType())
                            .icon(BitmapDescriptorFactory.fromResource((vehicle.isTaxi())? R.drawable.marker_taxi : R.drawable.marker_pooling))));
        }

        /* Refreshing Sheet */
        bottomSheet.refreshVehicles(vehiclesList);
    }

    @Override
    public void setVisibleRegion(LatLngBounds latLngBounds) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(), 11));
    }

    @Override
    public void setLoadingVehiclesState() {
        bottomSheet.setDataState(VehicleBottomSheet.DATA_STATE_LOADING);
    }

    @Override
    public void setErrorVehiclesState() {
        bottomSheet.setDataState(VehicleBottomSheet.DATA_STATE_ERROR);
    }

    @Override
    public LatLngBounds getVisibleRegion() {
        return googleMap.getProjection().getVisibleRegion().latLngBounds;
    }

    @Override
    public void showBannerTopDefault() {
        topBanner.setDefaultState();
    }

    @Override
    public void showBannerTopInfo(Vehicle vehicle) {
        topBanner.setVehicleState(vehicle);
    }

}
