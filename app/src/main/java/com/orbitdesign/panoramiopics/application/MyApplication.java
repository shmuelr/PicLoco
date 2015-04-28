package com.orbitdesign.panoramiopics.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.orbitdesign.panoramiopics.R;


/**
 * Created by Shmuel Rosansky on 3/13/2015.
 */
public class MyApplication extends Application {


    public enum TrackerName {
        APP_TRACKER
    }

    private Tracker tracker = null;

    public MyApplication() {
        super();
    }

    public synchronized Tracker getTracker() {
        if (tracker == null) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t =  analytics.newTracker(R.xml.global_tracker);
            t.enableAutoActivityTracking(true);
            t.enableExceptionReporting(true);
            tracker = t;

        }
        return tracker;
    }
}
