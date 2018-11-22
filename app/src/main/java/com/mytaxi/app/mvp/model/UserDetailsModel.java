package com.mytaxi.app.mvp.model;

import com.mytaxi.app.mvp.contract.UserDetailsContract;
import com.mytaxi.app.utils.BusProvider;

public class UserDetailsModel extends BaseModel implements UserDetailsContract.Model {

    private String userLargePicture;
    private String userName;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userAddress;

    public UserDetailsModel(BusProvider.Bus bus, String userLargePicture, String userName, String userFirstName, String userLastName,
                            String userEmail, String userAddress) {
        super(bus);
        this.userLargePicture = userLargePicture;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
    }

    @Override
    public String getUserImageLarge() {
        return userLargePicture;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getUserFirstName() {
        return userFirstName;
    }

    @Override
    public String getUserLastName() {
        return userLastName;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String getUserAddress() {
        return userAddress;
    }
}
