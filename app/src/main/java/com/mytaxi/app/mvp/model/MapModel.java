package com.mytaxi.app.mvp.model;

import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mytaxi.app.models.Coordinate;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.restApi.responses.VehicleResponse;
import com.mytaxi.app.restApi.responses.VehiclesResponse;
import com.mytaxi.app.utils.BusProvider;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapModel extends BaseModel implements MapContract.Model {

    private LatLngBounds latestBounds;
    private Geocoder geocoder;
    private Handler uiHandler;

    /* Constructor used in case of one point coordinates needed */
    public MapModel(BusProvider.Bus bus, Handler uiHandler, @NonNull Coordinate startingCoordinate, @NonNull Geocoder geocoder) {
        super(bus);
        LatLng point = new LatLng(startingCoordinate.getLatitude(), startingCoordinate.getLongitude());
        this.latestBounds = new LatLngBounds(point, point);
        this.geocoder = geocoder;
        this.uiHandler = uiHandler;
    }

    /* Constructor used in case of two point coordinates needed */
    public MapModel(BusProvider.Bus bus, Handler uiHandler, @NonNull Coordinate northEast, @NonNull Coordinate southWest, @NonNull Geocoder geocoder) {
        super(bus);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(northEast.getLatitude(), northEast.getLongitude()));
        builder.include(new LatLng(southWest.getLatitude(), southWest.getLongitude()));
        this.latestBounds = builder.build();
        this.geocoder = geocoder;
        this.uiHandler = uiHandler;
    }

    @Override
    public LatLngBounds getLatestBounds() {
        return latestBounds;
    }

    @Override
    public void updatePoints(@NonNull LatLngBounds latLngBounds) {
        this.latestBounds = latLngBounds;
        getVehiclesInArea(latestBounds);
    }

    @Override
    public String getReadableAddress(LatLng coordinates) {
        /*Address generator*/
        try {
            List<Address> addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
            return addresses.get(0).getThoroughfare() + "," + addresses.get(0).getFeatureName();
        } catch (IOException e) {
            return null;
        }
    }

    private void getVehiclesInArea(LatLngBounds latLngBounds) {
        /*Cancel previous calls if exist any in queue*/
        stopCancellableRetrofitRequest();
        /*Call service*/
        callService(getApiService().getVehiclesInArea(
                latLngBounds.northeast.latitude, latLngBounds.northeast.longitude,
                latLngBounds.southwest.latitude, latLngBounds.southwest.longitude),
                new Callback<VehiclesResponse>() {
                    @Override
                    public void onResponse(Call<VehiclesResponse> call, Response<VehiclesResponse> response) {
                        if (response != null && response.body() != null && response.body().getVehicles() != null) {
                            List<Vehicle> vehiclesModelList = new ArrayList<>();
                            for (VehicleResponse vehicle : response.body().getVehicles()) {
                                //Vehicle vehicleModel = vehicle.toModel();
                                //vehicleModel.setAddress(getReadableAddress(vehicleModel.getCoordinate().getLatLng()));
                                vehiclesModelList.add(vehicle.toModel());
                            }
                            Collections.sort(vehiclesModelList, ((o1, o2) -> o1.getHeading().compareTo(o2.getHeading())));
                            post(new OnVehiclesInAreaSuccess(vehiclesModelList));

                            /*new VehiclesProcessor(uiHandler, geocoder,
                                    response.body().getVehicles(),
                                    vehiclesList -> post(new OnVehiclesInAreaSuccess(vehiclesList)))
                                    .start();*/
                        } else {
                            onFailure(call, new Exception("Null data"));
                        }
                    }

                    @Override
                    public void onFailure(Call<VehiclesResponse> call, Throwable t) {
                        post(new OnVehiclesInAreaFail(t));
                    }
                }, true);
    }

    private static class VehiclesProcessor extends Thread {

        public interface VehiclesProcessorCallback {
            void onProcessCompleted(List<Vehicle> vehiclesList);
        }

        private SoftReference<Handler> uiHandlerRef;
        private SoftReference<VehiclesProcessorCallback> callbackRef;
        private SoftReference<Geocoder> geocoderWR;
        private List<VehicleResponse> vehicles;

        public VehiclesProcessor(Handler uiHandler, Geocoder geocoder, List<VehicleResponse> vehicles, VehiclesProcessorCallback callback) {
            this.uiHandlerRef = new SoftReference<>(uiHandler);
            this.callbackRef = new SoftReference<>(callback);
            this.geocoderWR = new SoftReference<>(geocoder);
            this.vehicles = vehicles;
        }

        @Override
        public void run() {
            List<Vehicle> vehiclesModelList = new ArrayList<>();
            for (VehicleResponse vehicle : vehicles) {
                Vehicle vehicleModel = vehicle.toModel();
                vehicleModel.setAddress(getReadableAddress(vehicleModel.getCoordinate().getLatLng()));
                vehiclesModelList.add(vehicleModel);
            }
            Collections.sort(vehiclesModelList, ((o1, o2) -> o1.getHeading().compareTo(o2.getHeading())));

            if (uiHandlerRef.get() != null && callbackRef.get() != null) {
                uiHandlerRef.get().post(() -> callbackRef.get().onProcessCompleted(vehiclesModelList));
            }
        }

        private String getReadableAddress(LatLng coordinates) {

            /*Check not collected*/
            if (geocoderWR.get() == null) {
                return null;
            }

            /*Address generator*/
            try {
                List<Address> addresses = geocoderWR.get().getFromLocation(coordinates.latitude, coordinates.longitude, 1);
                return addresses.get(0).getThoroughfare() + "," + addresses.get(0).getFeatureName();
            } catch (IOException e) {
                return null;
            }
        }
    }
}
