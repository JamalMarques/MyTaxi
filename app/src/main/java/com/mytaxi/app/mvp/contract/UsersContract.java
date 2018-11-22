package com.mytaxi.app.mvp.contract;

import android.support.annotation.Size;

import com.mytaxi.app.models.User;
import com.mytaxi.app.models.dataShare.UserDataContainer;
import com.mytaxi.app.mvp.model.BaseModel;
import com.mytaxi.app.restApi.responses.UsersPaginationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public interface UsersContract {

    interface View {

        /*Bus Notifications*/
        class OnItemClickEvent {

            public User user;

            public OnItemClickEvent(User user){
                this.user = user;
            }
        }

        class LoadNextPageEvent{/*Nothing to do here*/
        }

        void addItemsGrid(List<User> users);

        void showUserDetails(UserDataContainer dataContainer);

        void showApiErrorSnackbar();

        void setLoadingState(boolean isLoading);

        void setLoadingStatePagination(boolean isLoading);
    }

    interface Presenter extends PresenterCoreInterface {
        void onStart();
    }

    interface Model extends RetrofitManager{

        /*Bus Notifications*/
        class OnPaginationEvent extends BaseModel.RetrofitEvent<UsersPaginationResponse> {

            public OnPaginationEvent(Call call, Response<UsersPaginationResponse> response) {
                super(call, response);
            }

            public OnPaginationEvent(Call call, Throwable throwable) {
                super(call, throwable);
            }
        }

        boolean isFirstCallDone();

        void callUserPagination(@Size(min = 1) int page, @Size(min = 1) int pageSize);

        void setLastPage(int lastPage);

        void setPageSize(int pageSize);

        int getLastPage();

        int getPageSize();
    }
}
