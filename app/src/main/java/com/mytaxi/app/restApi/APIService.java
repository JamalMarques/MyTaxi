package com.mytaxi.app.restApi;

import com.mytaxi.app.restApi.responses.UsersPaginationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yamil.marques on 9/10/2018.
 */

public interface APIService {


    @GET(APIConstants.URL_USERS_PAGINATION)
    Call<UsersPaginationResponse> usersPagination(@Query("page") String page, @Query("results") String results, @Query("seed") String seed);

    /*@GET(APIConstants.URL_TOP_RATED)
    Call<MoviesResponse> getTopRatedMovies(@Query(APIConstants.PARAMETER_API_KEY) String apiKey);

    @GET(APIConstants.URL_GET_MOVIE)
    Call<MoviesResponse> getMovieDetails(@Path(APIConstants.PARAMETER_API_ID) int id,
                                         @Query(APIConstants.PARAMETER_API_KEY) String apiKey);*/

}
