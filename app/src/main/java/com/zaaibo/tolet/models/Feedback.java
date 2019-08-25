package com.zaaibo.tolet.models;

import java.sql.Timestamp;

public class Feedback {

    private String ownerAuthId;
    private String message;
    private String userAuthId;
    private String userFullName;
    private String userImageUrl;
    private String createdAt = String.valueOf(new Timestamp(System.currentTimeMillis()));

    public Feedback() {
    }

    public Feedback(String ownerAuthId, String message, String userAuthId, String userFullName, String userImageUrl) {
        this.ownerAuthId = ownerAuthId;
        this.message = message;
        this.userAuthId = userAuthId;
        this.userFullName = userFullName;
        this.userImageUrl = userImageUrl;
    }

    public String getOwnerAuthId() {
        return ownerAuthId;
    }

    public void setOwnerAuthId(String ownerAuthId) {
        this.ownerAuthId = ownerAuthId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
