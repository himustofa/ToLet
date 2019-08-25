package com.zaaibo.tolet.models;

import java.sql.Timestamp;

public class PostAd {

    private String ownerAuthId;
    private String ownerToken;
    private String ownerName;
    private String ownerEmail;
    private String ownerMobile;
    private String ownerMobileHide;
    private String propertyType;
    private String renterType;
    private String rentPrice;
    private String bedrooms;
    private String bathrooms;
    private String squareFootage;
    private String amenities;
    private String location;
    private String address;
    private String latitude;
    private String longitude;
    private String description;
    private String imageUrl;
    private String isEnable;
    private String createdAt = String.valueOf(new Timestamp(System.currentTimeMillis()));

    public static String getLocationFieldName() { return "location"; }

    public PostAd() {
    }

    public PostAd(String ownerAuthId, String ownerToken, String ownerName, String ownerEmail, String ownerMobile, String ownerMobileHide, String propertyType, String renterType, String rentPrice, String bedrooms, String bathrooms, String squareFootage, String amenities, String location, String address, String latitude, String longitude, String description, String imageUrl, String isEnable) {
        this.ownerAuthId = ownerAuthId;
        this.ownerToken = ownerToken;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerMobile = ownerMobile;
        this.ownerMobileHide = ownerMobileHide;
        this.propertyType = propertyType;
        this.renterType = renterType;
        this.rentPrice = rentPrice;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.squareFootage = squareFootage;
        this.amenities = amenities;
        this.location = location;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isEnable = isEnable;
    }

    public String getOwnerAuthId() {
        return ownerAuthId;
    }

    public void setOwnerAuthId(String ownerAuthId) {
        this.ownerAuthId = ownerAuthId;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public String getOwnerMobileHide() {
        return ownerMobileHide;
    }

    public void setOwnerMobileHide(String ownerMobileHide) {
        this.ownerMobileHide = ownerMobileHide;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getRenterType() {
        return renterType;
    }

    public void setRenterType(String renterType) {
        this.renterType = renterType;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(String squareFootage) {
        this.squareFootage = squareFootage;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
