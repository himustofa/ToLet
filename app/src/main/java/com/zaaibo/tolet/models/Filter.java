package com.zaaibo.tolet.models;

import java.io.Serializable;

//https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Filter implements Serializable {

    private String location;
    private String minPrice;
    private String maxPrice;
    private String property;
    private String renter;
    private String beds;

    public Filter(String location, String minPrice, String maxPrice, String property, String renter, String beds) {
        this.location = location;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.property = property;
        this.renter = renter;
        this.beds = beds;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

    public String getBeds() {
        return beds;
    }

    public void setBeds(String beds) {
        this.beds = beds;
    }
}
