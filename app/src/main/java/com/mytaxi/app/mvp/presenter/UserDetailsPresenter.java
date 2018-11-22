package com.mytaxi.app.mvp.presenter;

import com.mytaxi.app.mvp.contract.UserDetailsContract;

public class UserDetailsPresenter extends BasePresenter<UserDetailsContract.View, UserDetailsContract.Model> implements UserDetailsContract.Presenter {

    public UserDetailsPresenter(UserDetailsContract.View view, UserDetailsContract.Model model) {
        super(view, model);
    }

    @Override
    public void onStart() {
        view.showUserData(
                model.getUserImageLarge(), model.getUsername(),
                model.getUserFirstName(), model.getUserLastName(),
                model.getUserEmail(), model.getUserAddress());
    }

}
