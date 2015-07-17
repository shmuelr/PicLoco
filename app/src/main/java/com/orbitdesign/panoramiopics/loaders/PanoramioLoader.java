package com.orbitdesign.panoramiopics.loaders;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbitdesign.panoramiopics.models.Panoramio;
import com.orbitdesign.panoramiopics.utils.Tools;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Shmuel Rosansky on 3/18/2015.
 */
public class PanoramioLoader extends AsyncTaskLoader<Panoramio> {

    private static final String TAG = "PanoramioLoader";
    private Location location;
    private Panoramio panoramioCache;

    private int from = 0, to = 100;

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();


    public PanoramioLoader(Context context) {
        super(context);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public PanoramioLoader(Context context, Bundle args) {
        this(context);
        location = (Location)args.get("location");
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFrom(int from){
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void loadNext100Images(){
        Log.d(TAG, "Load next 100 images! ");
        from += 100;
        to += 100;

        onStartLoading();

    }

    @Override
    protected void onStartLoading() {
        if(location == null){
            return;
        }

        if(panoramioCache == null){
            forceLoad();
        }else{

            if(location.getLongitude() == panoramioCache.getMapLocation().getLon() &&
                    location.getLatitude() == panoramioCache.getMapLocation().getLat()){
                deliverResult(panoramioCache);
            }else{
                forceLoad();
            }


        }

    }

    @Override
    public Panoramio loadInBackground() {
        Panoramio panoramio = null;
        Log.d(TAG, "Loader in background");

        Request request = new Request.Builder()
                .url(Tools.getpanoramioURL(location.getLatitude(), location.getLongitude(), from, to))
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();

            panoramio = mapper.readValue(response.body().byteStream(), Panoramio.class);

        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }

        return panoramio;
    }

    @Override
    public void deliverResult(Panoramio data) {
        panoramioCache = data;

        super.deliverResult(data);

    }


}
