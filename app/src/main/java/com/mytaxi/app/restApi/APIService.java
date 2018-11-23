package com.mytaxi.app.restApi;

import com.mytaxi.app.restApi.responses.VehiclesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET(APIConstants.GET_VEHICLES)
    Call<VehiclesResponse> getVehiclesInArea(@Query("p1Lat") Double latitudeOne, @Query("p1Lon") Double longitudeOne,
                                             @Query("p2Lat") Double latitudeTwo, @Query("p2Lon") Double longitudeTwo);
}
