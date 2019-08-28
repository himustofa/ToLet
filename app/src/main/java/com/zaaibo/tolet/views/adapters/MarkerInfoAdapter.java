package com.zaaibo.tolet.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.models.PostAd;

import de.hdodenhof.circleimageview.CircleImageView;

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private String[] arr;
    private PostAd obj;

    public MarkerInfoAdapter(Context mContext, String[] arr, PostAd obj) {
        this.mContext = mContext;
        this.arr = arr;
        this.obj = obj;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.marker_info_window, null);
        Picasso.get().load(arr[0]).into(((CircleImageView) row.findViewById(R.id.photo)));
        ((TextView) row.findViewById(R.id.name)).setText(obj.getOwnerName());
        ((TextView) row.findViewById(R.id.phone)).setText(obj.getOwnerMobile());
        ((TextView) row.findViewById(R.id.address)).setText(obj.getAddress());
        ((TextView) row.findViewById(R.id.snippet)).setText(marker.getSnippet());
        return row;
    }
}
