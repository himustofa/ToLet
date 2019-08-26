package com.zaaibo.tolet.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.zaaibo.tolet.R;

import java.io.InputStream;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    private ProgressDialog progress;
    private ImageView bmImage;

    public LoadImageTask(Context context, ImageView bmImage) {
        this.context = context;
        this.bmImage = bmImage;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        progress = new ProgressDialog(context);
        //rotate_progress.setTitle("Retrieving data");
        progress.setMessage(context.getString( R.string.progress_img));
        progress.setCancelable(true);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            if(progress != null) {
                progress.dismiss(); //close the dialog if error occurs
            }
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        bmImage.setImageBitmap(result);
        if(progress != null) {
            progress.dismiss(); //close dialog
        }
    }

}
