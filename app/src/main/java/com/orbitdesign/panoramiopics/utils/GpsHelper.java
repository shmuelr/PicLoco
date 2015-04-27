package com.orbitdesign.panoramiopics.utils;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * Created by Shmuel Rosansky
 * This class simplifies using the GPS
 */

public class GpsHelper {

    public interface LocationCallback{
        public void onNewLocation(Location location);
    }

    public static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final String TAG = "GpsHelper";

    private LocationManager locationManager;

    private LocationListener gpsLocationListener;
    private Location lastLocation;

    private LocationCallback callback;

    public boolean isTracking = false;

    public long lastFix = -1;


    public GpsHelper(Context context, LocationCallback callback){

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.callback = callback;
        gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
    }

    public Location getLastKnownLocation(){
        Location  location = null;

        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if(isBetterLocation(locationManager.getLastKnownLocation(provider), location)){
                location = locationManager.getLastKnownLocation(provider);
            }

        }

        return location;
    }

    public void startTrackingLocation(){
        Log.d(TAG, "startTrackingLocation");
        isTracking = true;
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 0, 0, gpsLocationListener);
        }

    }

    public void stopTrackingLocation(){
        Log.d(TAG, "stopTrackingLocation");
        isTracking = false;
        locationManager.removeUpdates(gpsLocationListener);

    }


    private void makeUseOfNewLocation(Location location) {
        Log.d(TAG, "makeUseOfNewLocation");

        if(isBetterLocation(location, lastLocation)){
            lastFix = System.currentTimeMillis();
            callback.onNewLocation(location);
        }else{
            Log.d(TAG, "is NOT Better");
        }
        lastLocation = location;
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        if (location == null) {
            // A null location is always a worse location
            return false;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public void cleanUp() {
        callback = null;
    }




}
