package com.zaaibo.tolet;

import android.app.Application;
import android.content.Context;

import com.zaaibo.tolet.utils.language.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
