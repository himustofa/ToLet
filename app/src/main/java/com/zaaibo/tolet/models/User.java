package com.zaaibo.tolet.models;

import java.sql.Timestamp;

public class User {

    private String userAuthId;
    private String userFullName;
    private String userRelation;
    private String userOccupation;
    private String userEmail;
    private String userPhoneNumber;
    private String userBirthDate;
    private String userAddress;
    private String isUserOwner;
    private String userImageUrl;
    private String userToken;
    private String createdAt = String.valueOf(new Timestamp(System.currentTimeMillis()));

    public User() {
    }

    public User(String userAuthId, String userFullName, String userRelation, String userOccupation, String userEmail, String userPhoneNumber, String userBirthDate, String userAddress, String isUserOwner, String userImageUrl, String userToken) {
        this.userAuthId = userAuthId;
        this.userFullName = userFullName;
        this.userRelation = userRelation;
        this.userOccupation = userOccupation;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userBirthDate = userBirthDate;
        this.userAddress = userAddress;
        this.isUserOwner = isUserOwner;
        this.userImageUrl = userImageUrl;
        this.userToken = userToken;
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

    public String getUserRelation() {
        return userRelation;
    }

    public void setUserRelation(String userRelation) {
        this.userRelation = userRelation;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public void setUserOccupation(String userOccupation) {
        this.userOccupation = userOccupation;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getIsUserOwner() {
        return isUserOwner;
    }

    public void setIsUserOwner(String isUserOwner) {
        this.isUserOwner = isUserOwner;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
