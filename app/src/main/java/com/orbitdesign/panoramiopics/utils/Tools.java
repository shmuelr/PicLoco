package com.orbitdesign.panoramiopics.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
