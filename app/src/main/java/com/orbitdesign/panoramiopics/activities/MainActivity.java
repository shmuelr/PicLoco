package com.orbitdesign.panoramiopics.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.orbitdesign.panoramiopics.R;
import com.orbitdesign.panoramiopics.adapters.MainListAdapter;
import com.orbitdesign.panoramiopics.application.MyApplication;
import com.orbitdesign.panoramiopics.loaders.PanoramioLoader;
import com.orbitdesign.panoramiopics.models.Panoramio;
import com.orbitdesign.panoramiopics.models.Photo;
import com.orbitdesign.panoramiopics.utils.GpsHelper;
import com.orbitdesign.panoramiopics.utils.PreferencesHelper;
import com.orbitdesign.panoramiopics.utils.Tools;

import java.util.Collections;


public class MainActivity extends AppCompatActivity implements GpsHelper.LocationCallback, LoaderManager.LoaderCallbacks<Panoramio>{

    private static final String TAG = "MainActivity";
    private static final float DEFAULT_LATITUDE = 44.106877999999990000f;
    private static final float DEFAULT_LONGITUDE = 9.728382000000010000f;

    private GpsHelper mGPSHelper;

    private static final int LOADER_ID = 4356;

    private MainListAdapter mAdapter;

    private ProgressBar mProgressBar;

    private Toolbar mToolbar;

    private ObservableRecyclerView mObservableRecView;

    private long mLastGPSlockTime = 0;

    private boolean mLoading = true; // True if we are still waiting for the last set of data to load.

    private PanoramioLoader mPanoLoader;

    private int mTotalAmtOfImagesForLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getTracker();

        setContentView(R.layout.activity_main);
        setupApp();
        startLoader();

    }




    /**
     * If auto-locate is set on and the phone's location services are enabled then create gpsHelper using this activity as a callback.
     * Start the loader with the last known location so that the user doesn't have to wait until the gps locks on. Once the lock happens the callback
     * will be triggered and the loader will restart with the proper location.
     * Otherwise set location to the user's previous choice or the default if they haven't set a location yet.
     *
     */
    private void startLoader(){
        // Bundle will get passed to the loader
        Bundle bundle = new Bundle();
        // Location will be placed in the bundle
        Location location = null;

        if(isAutoLocateEnabled() && Tools.isLocationServiceEnabled(this)){
            mGPSHelper = new GpsHelper(this, this);
            location = mGPSHelper.getLastKnownLocation();
        }else{
            setAutoLocate(false);
            location = new Location("custom");
            location.setAccuracy(0f);
            location.setLatitude(
                    (double)PreferencesHelper.getSharedPreferencesFloat(this, PreferencesHelper.KEYS.LOCATION_LAT, DEFAULT_LATITUDE)
            );
            location.setLongitude(
                    (double)PreferencesHelper.getSharedPreferencesFloat(this, PreferencesHelper.KEYS.LOCATION_LON, DEFAULT_LONGITUDE)
            );

        }

        bundle.putParcelable("location", location);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    /**
     * Utility method to setup the app components
     */
    private void setupApp() {
        setupGUI();
        setupRecyclerView();
    }

    private void setupGUI() {
        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mObservableRecView = (ObservableRecyclerView) findViewById(R.id.mainList);
    }

    private void setupRecyclerView() {

        mObservableRecView.setHasFixedSize(true);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mObservableRecView.setLayoutManager(gridLayoutManager);

        mAdapter = new MainListAdapter();
        mObservableRecView.setAdapter(mAdapter);

        mObservableRecView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            private int previousTotal = 0; // The total number of items in the dataset after the last load
            private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before mLoading more.
            private int firstVisibleItem, visibleItemCount, totalItemCount;

            @Override
            public void onScrollChanged(int i, boolean b, boolean b2) {

                visibleItemCount =  mObservableRecView.getChildCount();
                totalItemCount =    gridLayoutManager.getItemCount();
                firstVisibleItem =  gridLayoutManager.findFirstVisibleItemPosition();

                if (mLoading) {
                    if (totalItemCount > previousTotal) {
                        mLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!mLoading &&
                        (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)
                        ) {
                    // End has been reached

                    if(totalItemCount < mTotalAmtOfImagesForLocation -100){
                        // Stop 100 away from the end so we dont overrun
                        // Null check is a dirty fix for the rotate crash
                        if( mPanoLoader != null ){
                            Toast.makeText(MainActivity.this, "Loading more images...", Toast.LENGTH_SHORT).show();
                            mPanoLoader.loadNext100Images();
                        }

                    }


                    mLoading = true;


                }

            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                if (scrollState == ScrollState.UP) {
                    if (toolbarIsShown()) {
                        hideToolbar();
                    }
                } else if (scrollState == ScrollState.DOWN) {
                    if (toolbarIsHidden()) {
                        showToolbar();
                    }
                }



            }
        });

    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }


    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }


    private void showToolbar() {
        moveToolbar(0);
    }


    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        final ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY((View) mObservableRecView, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mObservableRecView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) mObservableRecView).requestLayout();
            }
        });
        animator.start();
    }


    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }


    public void setLoadingSpinnerVisibility(boolean visible){
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onPause() {
        if (mGPSHelper != null) {
            mGPSHelper.stopTrackingLocation();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (
                isAutoLocateEnabled() &&
                mGPSHelper != null &&
                (mLastGPSlockTime - System.currentTimeMillis() ) > 600000 //If last gpslock was more than 10 min ago
                ) {
            mGPSHelper.startTrackingLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_map){
            startActivityForResult(new Intent(this, LocationSearchActivity.class), LocationSearchActivity.ID);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == LocationSearchActivity.ID && resultCode == RESULT_OK){

            if(data != null && data.hasExtra("address")){
                Address address = data.getParcelableExtra("address");

                Location location = new Location("custom");
                location.setAccuracy(0f);
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());

                setAutoLocate(false);
                PreferencesHelper.putSharedPreferencesFloat(this, PreferencesHelper.KEYS.LOCATION_LAT, (float) location.getLatitude());
                PreferencesHelper.putSharedPreferencesFloat(this, PreferencesHelper.KEYS.LOCATION_LON, (float)location.getLongitude());

                onNewLocation(location);
            }else{
                setAutoLocate(true);
                if(mGPSHelper != null){
                    mObservableRecView.setAlpha(128);
                    setLoadingSpinnerVisibility(true);
                    mGPSHelper.startTrackingLocation();
                }
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNewLocation(Location location) {
        if(location.getAccuracy() < 1000){
            if(mGPSHelper != null ) mGPSHelper.stopTrackingLocation();

            Bundle bundle = new Bundle();
            bundle.putParcelable("location", location);

            if(getSupportLoaderManager().hasRunningLoaders()){
                getSupportLoaderManager().restartLoader(LOADER_ID, bundle, this);
            }else{
                getSupportLoaderManager().destroyLoader(LOADER_ID);
                getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
            }
        }
    }


    public boolean isAutoLocateEnabled(){
        return PreferencesHelper.getSharedPreferencesBoolean(this, PreferencesHelper.KEYS.USE_AUTO_LOCATE, true);
    }

    public void setAutoLocate(boolean autoLocate){
        PreferencesHelper.putSharedPreferencesBoolean(this, PreferencesHelper.KEYS.USE_AUTO_LOCATE, autoLocate);
    }

    @Override
    public Loader<Panoramio> onCreateLoader(int id, Bundle args) {

        mAdapter.swapData(Collections.<Photo>emptyList());
        setLoadingSpinnerVisibility(true);
        mPanoLoader =  new PanoramioLoader(this, args);
        return mPanoLoader;
    }

    @Override
    public void onLoadFinished(Loader<Panoramio> loader, Panoramio data) {

        if(data != null){
            mTotalAmtOfImagesForLocation = data.getCount();

            if(mAdapter.getItemCount() == 0 ){
                mAdapter.swapData(data.getPhotos());

            }else{

                mAdapter.addData(data.getPhotos());

            }

            setLoadingSpinnerVisibility(mAdapter.getItemCount() == 0);
        }

        mLoading = false;
    }

    @Override
    public void onLoaderReset(Loader<Panoramio> loader) {
        mAdapter.swapData(Collections.<Photo>emptyList());
        setLoadingSpinnerVisibility(mAdapter.getItemCount() == 0);
    }
}
