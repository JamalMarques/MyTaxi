package com.mytaxi.app.mvp.contract;

public interface UserDetailsContract {

    interface View {
        void showUserData(String image, String username, String firstName, String lastName, String email, String address);
    }

    interface Presenter extends PresenterCoreInterface {
        void onStart();
    }

    interface Model extends RetrofitManager {

        String getUserImageLarge();

        String getUsername();

        String getUserFirstName();

        String getUserLastName();

        String getUserEmail();

        String getUserAddress();
    }
}
