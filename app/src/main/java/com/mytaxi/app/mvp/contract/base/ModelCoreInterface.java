package com.mytaxi.app.mvp.contract.base;

import android.support.annotation.IntDef;

import retrofit2.Call;
import retrofit2.Response;

public interface ModelCoreInterface {

    /**
     * Using for retrofit callbacks
     */
    public static class RetrofitBusEventBase<T> {

        @IntDef({SUCCESS, FAILURE})
        @interface Result {
        }

        public static final int SUCCESS = 0;
        public static final int FAILURE = 1;

        public @Result int status;
        public Call call;
        public Response<T> response;
        public Throwable throwable;


        public RetrofitBusEventBase(Call call, Response<T> response) {
            this.status = SUCCESS;
            this.call = call;
            this.response = response;
        }

        public RetrofitBusEventBase(Call call, Throwable throwable) {
            this.status = FAILURE;
            this.call = call;
            this.throwable = throwable;
        }
    }
}
