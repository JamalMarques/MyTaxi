package com.mytaxi.app.mvp.contract;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.base.PresenterCoreInterface;
import com.mytaxi.app.mvp.contract.base.RetrofitManager;

import java.util.List;

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

        void setErrorVehiclesState();

        LatLngBounds getVisibleRegion();

        void showBannerTopDefault();

        void showBannerTopInfo(Vehicle vehicle);

        /*Bus events*/
        class OnMapLoaded {/*Nothing to add here*/
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

        class OnVehicleItemClicked {
            private Vehicle vehicleCLicked;

            public OnVehicleItemClicked(Vehicle vehicle) {
                this.vehicleCLicked = vehicle;
            }

            public Vehicle getVehicleCLicked() {
                return vehicleCLicked;
            }
        }

        class OnMarkerClicked {
            private Vehicle vehicle;

            public OnMarkerClicked(Vehicle vehicle) {
                this.vehicle = vehicle;
            }

            public Vehicle getVehicle() {
                return vehicle;
            }
        }
    }

    interface Presenter extends PresenterCoreInterface {

        void onStart();

        void onResume();

        void onPause();

        void onStop();

        void destroy();

        void onSaveInstantState(Bundle bundle);

        void onLowMemory();
    }

    interface Model extends RetrofitManager {
        void updatePoints(LatLngBounds latLngBounds);

        String getReadableAddress(LatLng coordinates);

        LatLngBounds getLatestBounds();

        /*Bus events*/
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
    }
}
