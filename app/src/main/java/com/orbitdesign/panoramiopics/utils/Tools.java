package com.orbitdesign.panoramiopics.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Shmuel Rosansky on 3/18/2015.
 */
public class Tools {


    public static Boolean isLocationServiceEnabled(Context context){
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled( LocationManager.NETWORK_PROVIDER) || manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
    }



    private static final double latLonOffset = .5;
    public static String getpanoramioURL(double lat, double lon, int from, int to){
        return "http://www.panoramio.com/map/get_panoramas.php?set=public" +
                "&from="    +from+
                "&to="      +to+
                "&minx="    + (lon - latLonOffset) +
                "&miny="    + (lat - latLonOffset) +
                "&maxx="    + (lon + latLonOffset) +
                "&maxy="    + (lat + latLonOffset) +
                "&size=medium" +
                "&mapfilter=true";
    }

}
