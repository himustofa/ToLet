package com.zaaibo.tolet.utils;

import com.zaaibo.tolet.models.Filter;
import com.zaaibo.tolet.models.PostAd;

public class FilterResult {

    public static PostAd filters(PostAd model, Filter filter) {

        PostAd postAd = null;

        if (model != null) {

            if (model.getPropertyType().equals(filter.getProperty())) {
                postAd = model;
            } else if(filter.getProperty() != null) {
                if (filter.getProperty().equals("Any"))
                    postAd = model;
            }

            if (model.getRenterType().equals(filter.getRenter())) {
                postAd = model;
            } else if(filter.getRenter() != null) {
                if (filter.getRenter().equals("Any"))
                    postAd = model;
            }

            if (model.getBedrooms().equals(filter.getBeds())) {
                postAd = model;
            } else if(filter.getBeds() != null) {
                if (filter.getBeds().equals("Any"))
                    postAd = model;
            }

            if (model.getLocation().equals(filter.getLocation())) {
                postAd = model;
            }

        }

        return postAd;
    }

}
