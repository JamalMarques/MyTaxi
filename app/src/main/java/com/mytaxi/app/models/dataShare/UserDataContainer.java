package com.mytaxi.app.models.dataShare;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDataContainer implements Parcelable {

    private String userLargePicture;
    private String userName;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userAddress;

    public UserDataContainer(String userLargePicture, String userName, String userFirstName, String userLastName,
                             String userEmail, String userAddress) {
        this.userLargePicture = userLargePicture;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
    }

    public UserDataContainer(Parcel parcel) {
        this.userLargePicture = parcel.readString();
        this.userName = parcel.readString();
        this.userFirstName = parcel.readString();
        this.userLastName = parcel.readString();
        this.userEmail = parcel.readString();
        this.userAddress = parcel.readString();
    }

    public String getUserLargePicture() {
        return userLargePicture;
    }

    public void setUserLargePicture(String userLargePicture) {
        this.userLargePicture = userLargePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /*Parcel section*/
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<UserDataContainer>() {
        @Override
        public UserDataContainer createFromParcel(Parcel source) {
            return new UserDataContainer(source);
        }

        @Override
        public UserDataContainer[] newArray(int size) {
            return new UserDataContainer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userLargePicture);
        dest.writeString(this.userName);
        dest.writeString(this.userFirstName);
        dest.writeString(this.userLastName);
        dest.writeString(this.userEmail);
        dest.writeString(this.userAddress);
    }
}
