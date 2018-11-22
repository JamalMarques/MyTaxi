package com.mytaxi.app.mvp.model;

import android.support.annotation.IntDef;

import com.mytaxi.app.mvp.contract.RetrofitManager;
import com.mytaxi.app.restApi.APIClient;
import com.mytaxi.app.restApi.APIService;
import com.mytaxi.app.utils.BusProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseModel implements RetrofitManager {

    private List<Call> cancellableRetrofitCalls = new ArrayList<>();
    private APIService apiService;
    private BusProvider.Bus bus;

    public BaseModel(BusProvider.Bus bus) {
        this.bus = bus;
        apiService = APIClient.getRetrofitClient();
    }

    /**
     * Stop all cancelable request
     * Must be called in stop or destroy lifecycle fragment/activity methods
     */
    @Override
    public void stopCancellableRetrofitRequest() {

        List<Call> toRemove = new ArrayList<>();
        for (Call call : cancellableRetrofitCalls) {
            if (!call.isCanceled()) {
                call.cancel();
                toRemove.add(call);
            }
        }

        /*Clear canceled*/
        cancellableRetrofitCalls.removeAll(toRemove);
    }

    /**
     * Using for retrofit callbacks
     */
    public static class RetrofitEvent<T> {

        @IntDef({SUCCESS, FAILURE})
        @interface Result {
        }

        public static final int SUCCESS = 0;
        public static final int FAILURE = 1;

        public @Result int status;
        public Call call;
        public Response<T> response;
        public Throwable throwable;


        public RetrofitEvent(Call call, Response<T> response) {
            this.status = SUCCESS;
            this.call = call;
            this.response = response;
        }

        public RetrofitEvent(Call call, Throwable throwable) {
            this.status = FAILURE;
            this.call = call;
            this.throwable = throwable;
        }
    }

    protected void post(Object event) {
        bus.post(event);
    }

    protected APIService getApiService() {
        return apiService;
    }

    private void addRetrofitCall(Call call) {
        cancellableRetrofitCalls.add(call);
    }


    /**
     * Call API endpoint with retrofit
     * Cancellable option enabled as default value
     */
    protected void callService(Call retrofitCall, Callback callback) {
        callService(retrofitCall, callback, true);
    }

    /**
     * Call API endpoint with retrofit
     *
     * @param isCancellable determines if the request can be cancellable from {@link #stopCancellableRetrofitRequest()}
     */
    protected void callService(Call retrofitCall, Callback callback, boolean isCancellable) {

        /*Check to add into cancellable list*/
        if (isCancellable) addRetrofitCall(retrofitCall);

        /*Make request*/
        retrofitCall.enqueue(callback);
    }

}
