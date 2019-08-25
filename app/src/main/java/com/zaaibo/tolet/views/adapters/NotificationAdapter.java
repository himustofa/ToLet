package com.zaaibo.tolet.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<User> mArrayList;

    public NotificationAdapter(Context mContext, ArrayList<User> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_notification_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User model = mArrayList.get(position);
        Picasso.get().load(model.getUserImageUrl()).into((holder.userImageUrl));
        holder.userFullName.setText(model.getUserFullName());
        holder.userAddress.setText(model.getUserAddress());
        holder.userPhoneNumber.setText(model.getUserPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        CircleImageView userImageUrl;
        TextView userFullName, userAddress, userPhoneNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.notification_item_id);
            userImageUrl = (CircleImageView) itemView.findViewById(R.id.userImageUrl);
            userFullName = (TextView) itemView.findViewById(R.id.userFullName);
            userAddress = (TextView) itemView.findViewById(R.id.userAddress);
            userPhoneNumber = (TextView) itemView.findViewById(R.id.userPhoneNumber);
        }
    }
}
