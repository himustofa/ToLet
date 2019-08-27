package com.zaaibo.tolet.views.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.models.Feedback;
import com.zaaibo.tolet.models.User;
import com.zaaibo.tolet.models.notification.DataModel;
import com.zaaibo.tolet.models.notification.NotificationModel;
import com.zaaibo.tolet.models.PostAd;
import com.zaaibo.tolet.models.notification.RootModel;
import com.zaaibo.tolet.repositories.firebase.FirebaseRepository;
import com.zaaibo.tolet.repositories.retrofit.ApiClient;
import com.zaaibo.tolet.repositories.retrofit.ApiInterface;
import com.zaaibo.tolet.session.SharedPrefManager;
import com.zaaibo.tolet.utils.LoadImageTask;
import com.zaaibo.tolet.utils.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private static final String TAG = "PostsAdapter";
    private ProgressDialog mProgress = null;
    private Context mContext;
    private ArrayList<PostAd> arrayList;
    private User mUser;

    public PostsAdapter(Context mContext, ArrayList<PostAd> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mUser = SharedPrefManager.getInstance(mContext).getUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_post_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostAd model = arrayList.get(position);

        String[] arr = model.getImageUrl().replaceAll("[\\[\\]]", "").split(",");
        //Picasso.get().load(arr[0]).into((holder.imageUrl));
        //Glide.with(mContext).asBitmap().load(arr[0]).into(holder.imageUrl);
        holder.rentPrice.setText("TK "+model.getRentPrice()+" /monthly");
        holder.address.setText(model.getAddress());
        holder.description.setText(model.getBedrooms()+" Beds, "+model.getBathrooms()+" Baths, "+model.getSquareFootage()+" (sq.ft)"); //4 Beds, 3 Baths, 1200 (sq.ft)

        Picasso.get().load(arr[0]).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_logo_ride).into(holder.imageUrl, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError(Exception e) {
                Picasso.get().load(arr[0]).placeholder(R.drawable.ic_logo_ride).into((holder.imageUrl));
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress = Utility.showProgressDialog(mContext, mContext.getResources().getString( R.string.progress), false);
                showListItem(model, arr);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You clicked "+model.getOwnerName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You clicked "+model.getOwnerName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.about_title)
                        .setMessage(R.string.msg_remove)
                        .setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                removePostAdData(model, position);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.msg_neg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        CircleImageView imageUrl;
        TextView rentPrice, address, description;
        ImageButton share, favorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.post_item_id);
            imageUrl = (CircleImageView) itemView.findViewById(R.id.post_image);
            rentPrice = (TextView) itemView.findViewById(R.id.post_rent_price);
            address = (TextView) itemView.findViewById(R.id.post_address);
            description = (TextView) itemView.findViewById(R.id.post_bed_bath_size);
            share = (ImageButton) itemView.findViewById(R.id.post_share);
            favorite = (ImageButton) itemView.findViewById(R.id.post_favorite);
        }
    }

    //====================================================| Show Details
    private void showListItem(PostAd model, String[] arr) {
        Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen); //new Dialog(PostAdActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //make map clear
        dialog.setContentView(R.layout.dialog_post_details);

        //WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        //lp.copyFrom(dialog.getWindow().getAttributes());
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.gravity = Gravity.CENTER;
        //dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(true); //dismiss by clicking outside
        dialog.show();

        MapView mMapView = (MapView) dialog.findViewById(R.id.static_google_maps);
        MapsInitializer.initialize(mContext);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng latLng = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
                googleMap.addMarker(new MarkerOptions().position(latLng).title(model.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); //23.793772, 90.388070
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
        });

        final ImageView img = (ImageView) dialog.findViewById(R.id.image1);
        Picasso.get().load(arr[0]).into((img));
        //Glide.with(mContext).asBitmap().load(arr[0]).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress = Utility.showProgressDialog(mContext, mContext.getResources().getString( R.string.progress), false);
                imageViewerDialog(arr);
            }
        });

        ((TextView) dialog.findViewById(R.id.post_details_location)).setText(model.getLocation());
        ((TextView) dialog.findViewById(R.id.post_details_price)).setText("TK "+model.getRentPrice()+" /monthly");
        ((TextView) dialog.findViewById(R.id.post_details_owner)).setText("Posted by "+model.getOwnerName());
        ((TextView) dialog.findViewById(R.id.post_details_date)).setText("Posted at "+Utility.getDateFromTimestamp(model.getCreatedAt()));
        ((TextView) dialog.findViewById(R.id.post_details_property)).setText(model.getPropertyType());
        ((TextView) dialog.findViewById(R.id.post_details_renter)).setText(model.getRenterType());
        ((TextView) dialog.findViewById(R.id.post_details_beds)).setText(model.getBedrooms());
        ((TextView) dialog.findViewById(R.id.post_details_baths)).setText(model.getBathrooms());
        ((TextView) dialog.findViewById(R.id.post_details_size)).setText(model.getSquareFootage());
        ((TextView) dialog.findViewById(R.id.post_details_amenities)).setText(model.getAmenities());
        ((TextView) dialog.findViewById(R.id.post_details_desc)).setText(model.getDescription());

        //-----------| Favorite
        ((ImageButton) dialog.findViewById(R.id.post_favorite)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(mContext).saveFavoriteItem(model.getOwnerAuthId());
            }
        });

        //-----------| Share
        ((ImageButton) dialog.findViewById(R.id.post_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, bitmap);
                mContext.startActivity(Intent.createChooser(share,"Share via"));
            }
        });

        //-----------| Interest | Notification
        ((Button) dialog.findViewById(R.id.post_details_interested)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotificationToUser(model.getOwnerAuthId(), model.getOwnerToken());
            }
        });

        //-----------| Reviews|Feedback
        getAdapterData(((ListView) dialog.findViewById(R.id.post_feedback_list)), model.getOwnerAuthId());

        final EditText feedback = (EditText) dialog.findViewById(R.id.post_details_feedback);
        ((Button) dialog.findViewById(R.id.post_feedback_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!feedback.getText().toString().trim().isEmpty()) {
                    Feedback feed = new Feedback(model.getOwnerAuthId(), feedback.getText().toString().trim(), mUser.getUserAuthId(), mUser.getUserFullName(), mUser.getUserImageUrl());
                    new FirebaseRepository().storeFeedback(new FirebaseRepository.StoreFeedbackCallback() {
                        @Override
                        public void onCallback(String result) {
                            if (result.equals("success")) {
                                Toast.makeText(mContext, "Submitted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, feed);
                }
            }
        });

        //-----------| Call
        ((Button) dialog.findViewById(R.id.post_details_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getOwnerMobileHide().equals("false")) {
                    intentCall(model.getOwnerMobile());
                } else {
                    Utility.alertDialog(mContext, mContext.getString(R.string.msg_phone_not_available));
                }
            }
        });

        //-----------| Email
        ((Button) dialog.findViewById(R.id.post_details_email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentEmail(model.getOwnerEmail());
            }
        });

        Utility.dismissProgressDialog(mProgress);
    }

    //====================================================| Feedback
    private void getAdapterData(ListView listView, String mOwnerAuthId) {
        ArrayList<String> list = new ArrayList<>();
        new FirebaseRepository().getAllFeedback(new FirebaseRepository.GetFeedbackCallback() {
            @Override
            public void onCallback(ArrayList<Feedback> arrayList) {
                if (arrayList != null) {
                    //list.addAll(arrayList);
                    for(Feedback obj : arrayList){
                        list.add(obj.getUserFullName()+" : "+obj.getMessage() +"\nPosted at "+Utility.getDateFromTimestamp(obj.getCreatedAt()));
                    }
                    Log.d(TAG, ""+new Gson().toJson(list));
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list));
                }
            }
        }, mOwnerAuthId);
    }

    //====================================================| Image Viewer
    private void imageViewerDialog(String[] arr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_post_image_viewer, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.create();

        final AlertDialog dialog = builder.show();

        final LinearLayout imgGroup = (LinearLayout) view.findViewById(R.id.post_all_image);

        for (String s : arr) {
            ImageView img = new ImageView(mContext);
            img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
            img.setBackground(mContext.getResources().getDrawable(R.drawable.shape_image_border));
            imgGroup.addView(img);
            new LoadImageTask(mContext, img).execute(s);
            //Picasso.get().load(arr[i]).into(img);
            //Glide.with(mContext).asBitmap().load(arr[0]).into(img);

            /*int n = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.get().load(arr[n]).into(img);
                }
            }, 3000);*/
        }

        Utility.dismissProgressDialog(mProgress);
    }

    //====================================================| Call
    private void intentCall(String number) {
        Intent dial = new Intent(Intent.ACTION_DIAL);
        try {
            dial.setData(Uri.parse("tel:" + number));
            mContext.startActivity(dial);
        } catch (ActivityNotFoundException e) {
            Utility.alertDialog(mContext, mContext.getString(R.string.msg_no_dialer));
        }
    }

    //====================================================| Email
    private void intentEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        try {
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "For Rent");
            mContext.startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Utility.alertDialog(mContext, mContext.getString(R.string.msg_no_email));
        }
    }

    //====================================================| Send Notification
    private void sendNotificationToUser(String mOwnerAuthId, String token) {
        RootModel rootModel = new RootModel(token, new NotificationModel("For Rent", "I am interested in this rental and would like to schedule a viewing"), new DataModel("Ali", "30"));

        ApiInterface apiService =  ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d(TAG,"Successfully notification send by using retrofit.");
                storeNotification(mOwnerAuthId, mUser);
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void storeNotification(String mOwnerAuthId, User mUser) {
        new FirebaseRepository().storeNotification(new FirebaseRepository.StoreNotificationCallback() {
            @Override
            public void onCallback(String result) {
                if (result.equals("success")) {
                    Log.d(TAG, result);
                }
            }
        }, mOwnerAuthId, mUser);
    }

    private void removePostAdData(PostAd model, int position) {
        new FirebaseRepository().removePostAd(new FirebaseRepository.RemovePostAdCallback() {
            @Override
            public void onCallback(String result) {
                if (result.equals("success")) {
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, arrayList.size());

                    //--------------------------------| Remove all images
                    String[] arr = model.getImageUrl().replaceAll("[\\[\\]]", "").split(", ");
                    for (String path : arr) {
                        new FirebaseRepository().removePostAdImages(new FirebaseRepository.RemovePostAdCallback() {
                            @Override
                            public void onCallback(String result) {
                                if (result != null) {
                                    Toast.makeText(mContext, "Removed successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, path);
                    }
                    //--------------------------------| Remove all feedback
                    new FirebaseRepository().removeFeedback(model.getOwnerAuthId());
                }
            }
        }, model);
    }
}
