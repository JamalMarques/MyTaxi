package com.mytaxi.app.mvp.contract;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.base.PresenterCoreInterface;
import com.mytaxi.app.mvp.contract.base.RetrofitManager;

import java.util.List;
import java.util.Map;

public interface MapContract {

    interface View {

        void onStartMap();

        void onResumeMap();

        void onPauseMap();

        void onStopMap();

        void onDestroyMap();

        void onSaveInstantStateMap(Bundle bundle);

        void onLowMemoryMap();

        void refreshVehiclesComponent(List<Vehicle> list);

        void setVisibleRegion(LatLngBounds latLngBounds);

        void setLoadingVehiclesState();

        void notifyVehicleDataChanged();

        Map<Vehicle, Marker> generateMapMarkers(List<Vehicle> newVehicles);

        void animateCameraToMarker(Marker marker);

        void selectVehicleSheetItem(Vehicle vehicle);

        void setErrorVehiclesState();

        void showTopBannerDefaultState();

        void showBannerTopInfo(Vehicle vehicle, boolean animate);

        void showExtraButton();

        void showFirstTimeIntro();

        /*Bus events*/
        class OnMapLoaded {/*Nothing to add here*/
        }

        class OnBottomSheetVehicleClicked {
            private Vehicle vehicle;

            public OnBottomSheetVehicleClicked(Vehicle vehicle) {
                this.vehicle = vehicle;
            }

            public Vehicle getVehicleClicked() {
                return vehicle;
            }
        }

        class OnCameraMovedByUser {

            private LatLngBounds newBounds;

            public OnCameraMovedByUser(LatLngBounds newBounds) {
                this.newBounds = newBounds;
            }

            public LatLngBounds getNewBounds() {
                return newBounds;
            }
        }

        class OnMarkerClicked {
            private Marker marker;

            public OnMarkerClicked(Marker marker) {
                this.marker = marker;
            }

            public Marker getMarker() {
                return marker;
            }
        }
    }

    interface Presenter extends PresenterCoreInterface {

        void onStart();

        void onResume();

        void onPause();

        void onStop();

        void onDestroy();

        void onSaveInstantState(Bundle bundle);

        void onLowMemory();
    }

    interface Model extends RetrofitManager {
        void addVehicles(Map<Vehicle, Marker> newVehiclesMap);

        List<Vehicle> getCurrentVehicles();

        Vehicle getVehicleFromMarker(Marker marker);

        Marker getMarkerFromVehicle(Vehicle vehicle);

        void updatePoints(LatLngBounds latLngBounds);

        void obtainReadableAddress(Vehicle vehicle);

        LatLngBounds getLatestBounds();

        boolean shouldUpdatePoints(LatLngBounds latLngBounds);

        /*Bus events*/
        class OnRequestNewMarkers {
            private List<Vehicle> vehicles;

            public OnRequestNewMarkers(List<Vehicle> vehicles) {
                this.vehicles = vehicles;
            }

            public List<Vehicle> getVehicles() {
                return vehicles;
            }
        }

        class OnVehiclesInAreaSuccess {

            private List<Vehicle> vehicles;

            public OnVehiclesInAreaSuccess(List<Vehicle> vehicles) {
                this.vehicles = vehicles;
            }

            public List<Vehicle> getVehicles() {
                return vehicles;
            }
        }

        class OnVehiclesInAreaFail {
            private Throwable t;

            public OnVehiclesInAreaFail(Throwable t) {
                this.t = t;
            }

            public Throwable getT() {
                return t;
            }
        }

        class OnAddressObtained {
            private Vehicle vehicle;

            public OnAddressObtained(Vehicle vehicle) {
                this.vehicle = vehicle;
            }

            public Vehicle getVehicleUpdated() {
                return vehicle;

            }
        }

        class OnAddressTargetCountAcomplished {
            /*Nothing to add here*/
        }
    }
}
