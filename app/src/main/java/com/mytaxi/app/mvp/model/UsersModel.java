package com.mytaxi.app.mvp.model;

import android.support.annotation.Size;

import com.mytaxi.app.models.User;
import com.mytaxi.app.mvp.contract.UsersContract;
import com.mytaxi.app.restApi.responses.UsersPaginationResponse;
import com.mytaxi.app.utils.BusProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersModel extends BaseModel implements UsersContract.Model {

    private String seed;
    private List<User> userList = new ArrayList<>();
    private int lastPage;
    private int pageSize;
    private boolean isFirstCallDone = false;

    public UsersModel(BusProvider.Bus bus, String seed, int lastPage, int pageSize) {
        super(bus);
        this.lastPage = lastPage;
        this.pageSize = pageSize;
        this.seed = seed;
    }

    @Override
    public boolean isFirstCallDone() {
        return isFirstCallDone;
    }

    @Override
    public void callUserPagination(@Size(min = 1) int page, @Size(min = 1) int pageSize) {
        callService(getApiService().usersPagination(String.valueOf(page), String.valueOf(pageSize), seed), new Callback<UsersPaginationResponse>() {
            @Override
            public void onResponse(Call<UsersPaginationResponse> call, Response<UsersPaginationResponse> response) {
                isFirstCallDone = true;
                /*Update local list*/
                userList.addAll(response.body().getResults());
                /*Send notification*/
                post(new OnPaginationEvent(call, response));
            }

            @Override
            public void onFailure(Call<UsersPaginationResponse> call, Throwable t) {
                post(new OnPaginationEvent(call, t));
            }
        });
    }

    @Override
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getLastPage() {
        return lastPage;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}
