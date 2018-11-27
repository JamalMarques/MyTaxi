package com.mytaxi.app.mvp.model;

import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.mytaxi.app.models.Coordinate;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.restApi.responses.VehicleResponse;
import com.mytaxi.app.restApi.responses.VehiclesResponse;
import com.mytaxi.app.utils.BusProvider;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapModel extends BaseModel implements MapContract.Model {

    private final int ADVICE_AT_NUMBER_OF_ADDRESS = 5;
    private int numberOfAddressRequested = 0;

    private LatLngBounds latestBounds;
    private HashMap<Vehicle, Marker> currentMarkers = new HashMap<>();
    private Geocoder geocoder;
    private Handler uiHandler;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future addressCallRunning;

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

    /**
     * Only update points if the currently bounds are outside of the area
     * previously loaded and in cache in order to save internet resources
     *
     * @param latLngBounds camera updated bounds.
     */
    @Override
    public boolean shouldUpdatePoints(LatLngBounds latLngBounds) {
        return latestBounds.northeast.latitude < latLngBounds.northeast.latitude ||
                latestBounds.northeast.longitude < latLngBounds.northeast.longitude ||
                latestBounds.southwest.latitude > latLngBounds.southwest.latitude ||
                latestBounds.southwest.longitude > latLngBounds.southwest.longitude;
    }

    /**
     * Adding new markers to the map
     *
     * @param newVehiclesMap new markers with it's markers associated
     */
    @Override
    public void addVehicles(Map<Vehicle, Marker> newVehiclesMap) {
        currentMarkers.putAll(newVehiclesMap);
    }

    @Override
    public List<Vehicle> getCurrentVehicles() {
        return new ArrayList<>(currentMarkers.keySet());
    }

    @Override
    public Vehicle getVehicleFromMarker(Marker marker) {
        for (Map.Entry<Vehicle, Marker> entry : currentMarkers.entrySet()) {
            if (entry.getValue().getId().equals(marker.getId())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Marker getMarkerFromVehicle(Vehicle vehicle) {
        return currentMarkers.get(vehicle);
    }

    @Override
    public void updatePoints(@NonNull LatLngBounds latLngBounds) {
        this.latestBounds = latLngBounds;
        getVehiclesInArea(latestBounds);
    }

    @Override
    public void obtainReadableAddress(Vehicle vehicle) {
        if (addressCallRunning != null && !addressCallRunning.isDone()) {
            addressCallRunning.cancel(true);
        }

        addressCallRunning = executor.submit(new AddressProcessor(uiHandler, geocoder, vehicle, vehicle1 -> {
            Marker marker = currentMarkers.get(vehicle1);
            currentMarkers.put(vehicle1, marker);
            addressCallRunning = null;
            post(new OnAddressObtained(vehicle1));
        }));

        /* Address request count just for credits button*/
        addAndCheckAddressCount();
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
                            syncData(response.body().getVehicles());
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

    /**
     * This method will sync markers-vehicles data
     * Only new markers are going to be created, already existent ones will be untouched and repositioned if needed
     * in order to improve rendering of not changed markers.
     * <p>
     * Note: in the current implementation this advantage is not appreciated due that the mock API always returns
     * random data with different vehicles no matter what.
     *
     * @param vehiclesResponse new or updated vehicles to process.
     */
    private void syncData(List<VehicleResponse> vehiclesResponse) {
        List<Vehicle> vehiclesModelList = new ArrayList<>();
        for (VehicleResponse vehicle : vehiclesResponse) {
            vehiclesModelList.add(vehicle.toModel());
        }

        DecimalFormat df = new DecimalFormat(".######");
        List<Vehicle> vehiclesToAdd = new ArrayList<>(vehiclesModelList);
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
            Marker marker = currentMarkers.get(vehicle);
            if (marker != null) {
                marker.remove();
            }
            currentMarkers.remove(vehicle);
        }

        /*Request new markers*/
        if (vehiclesToAdd.size() > 0) {
            post(new OnRequestNewMarkers(vehiclesToAdd));
        }
    }

    private static class AddressProcessor implements Runnable {

        public interface VehiclesProcessorCallback {
            void onProcessCompleted(Vehicle vehicle);
        }

        private SoftReference<Handler> uiHandlerRef;
        private SoftReference<VehiclesProcessorCallback> callbackRef;
        private SoftReference<Geocoder> geocoderWR;
        private Vehicle vehicle;

        public AddressProcessor(Handler uiHandler, Geocoder geocoder, Vehicle vehicle, VehiclesProcessorCallback callback) {
            this.uiHandlerRef = new SoftReference<>(uiHandler);
            this.callbackRef = new SoftReference<>(callback);
            this.geocoderWR = new SoftReference<>(geocoder);
            this.vehicle = vehicle;
        }

        @Override
        public void run() {
            vehicle.setAddress(getReadableAddress(vehicle.getCoordinate().getLatLng()));

            if (uiHandlerRef.get() != null && callbackRef.get() != null) {
                uiHandlerRef.get().post(() -> callbackRef.get().onProcessCompleted(vehicle));
            }
        }

        private String getReadableAddress(LatLng coordinates) {
            if (geocoderWR.get() == null) {
                return null;
            }

            try {
                List<Address> addresses = geocoderWR.get().getFromLocation(coordinates.latitude, coordinates.longitude, 1);
                return addresses.get(0).getThoroughfare() + "," + addresses.get(0).getFeatureName();
            } catch (IOException e) {
                return null;
            }
        }
    }

    private void addAndCheckAddressCount() {
        numberOfAddressRequested++;
        if (numberOfAddressRequested == ADVICE_AT_NUMBER_OF_ADDRESS) {
            post(new OnAddressTargetCountAcomplished());
        }
    }
}
