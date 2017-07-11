package com.logcat.example.pc.orbitalproj;


import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.System.out;

public class UserInfo extends Object implements Parcelable{

    private String userName;
    private String userIC;
    private String userAge;
    private String userSex;
    private String userAllergies;
    private String userChronic;
    private String userOther;

    public UserInfo(String userName,
                    String userIC,
                    String userAge,
                    String userSex,
                    String userAllergies,
                    String userChronic,
                    String userOther) {
        this.userName = userName;
        this.userIC = userIC;
        this.userAge = userAge;
        this.userSex = userSex;
        this.userAllergies = userAllergies;
        this.userChronic = userChronic;
        this.userOther = userOther;
    }

    public UserInfo(){

    }

    private UserInfo(Parcel in){
        userName = in.readString();
        userIC = in.readString();
        userAge = in.readString();
        userSex = in.readString();
        userAllergies = in.readString();
        userChronic = in.readString();
        userOther = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(userName);
        out.writeString(userIC);
        out.writeString(userAge);
        out.writeString(userSex);
        out.writeString(userAllergies);
        out.writeString(userChronic);
        out.writeString(userOther);
    }


    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIC() {
        return userIC;
    }

    public void setUserIC(String userIC) {
        this.userIC = userIC;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAllergies() {
        return userAllergies;
    }

    public void setUserAllergies(String userAllergies) {
        this.userAllergies = userAllergies;
    }

    public String getUserChronic() {
        return userChronic;
    }

    public void setUserChronic(String userChronic) {
        this.userChronic = userChronic;
    }

    public String getUserOther() {
        return userOther;
    }

    public void setUserOther(String userOther) {
        this.userOther = userOther;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
