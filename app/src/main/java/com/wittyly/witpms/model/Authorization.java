package com.wittyly.witpms.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Authorization extends RealmObject {

    private String type;
    @SerializedName("expires_in") private String expiresIn;
    @SerializedName("access_token") private String accessToken;
    @SerializedName("refresh_token") private String refreshToken;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
