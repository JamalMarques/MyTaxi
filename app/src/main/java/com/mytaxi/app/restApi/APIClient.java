package com.mytaxi.app.restApi;

import com.mytaxi.app.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static APIService retrofit = null;

    public static final String BASE_URL = APIConstants.BASE_URL;

    /* Singleton instance */
    public static APIService getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient())
                    .build()
                    .create(APIService.class);
        }
        return retrofit;
    }

    private static OkHttpClient getHttpClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);
        }
        /*Add more configurations if needed*/
        return httpClient.build();
    }
}
