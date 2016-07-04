package com.artikov.photototext;

import android.app.Application;

import com.arellomobile.mvp.MvpFacade;

/**
 * Date: 4/7/2016
 * Time: 17:14
 *
 * @author Artur Artikov
 */

public class PhotoToTextApplication extends Application {
    static private PhotoToTextApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MvpFacade.init();
        sInstance = this;
    }

    static public PhotoToTextApplication getInstance() {
        return sInstance;
    }
}