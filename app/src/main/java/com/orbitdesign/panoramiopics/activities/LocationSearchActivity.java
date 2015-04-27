package com.orbitdesign.panoramiopics.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orbitdesign.panoramiopics.R;
import com.orbitdesign.panoramiopics.utils.Tools;

import java.util.List;
import java.util.Locale;

/**
 * Created by Shmuel Rosansky on 10/5/2014.
 */
public class LocationSearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchPopupActivity";

    private EditText inputEditText;
    private ListView resultsListView;
    private ProgressBar progressBar;
    private Boolean isSearching = false;

    private SearchLocation searchLocationAsync;
    private SearchResultsAdapter searchResultsAdapter;

    private Geocoder geocoder;

    public static final int ID = 23434;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_location);

        loadUIElements();
    }

    private void loadUIElements() {
        if(Tools.isLocationServiceEnabled(this)){
            findViewById(R.id.imageViewLocate).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.imageViewLocate).setVisibility(View.GONE);
        }


        geocoder = new Geocoder(this, Locale.getDefault());

        inputEditText = (EditText)findViewById(R.id.editTextLocationName);
        resultsListView = (ListView)findViewById(R.id.locationsResultsListView);
        progressBar = (ProgressBar)findViewById(R.id.search_progress_bar);

        searchResultsAdapter = new SearchResultsAdapter(this);
        resultsListView.setAdapter(searchResultsAdapter);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Address address = (Address) adapterView.getItemAtPosition(position);




                Intent intent = new Intent();

                intent.putExtra("address", address);

                setResult(RESULT_OK, intent);
                finish();

            }
        });


        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,  int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()>1){
                    if(searchLocationAsync!=null && isSearching){
                        searchLocationAsync.cancel(true);
                    }
                    searchLocationAsync = new SearchLocation();
                    searchLocationAsync.execute(editable.toString());
                }

            }
        });



    }

    public void showProgressBar(Boolean show){
        if(show){
            progressBar.setVisibility(View.VISIBLE);
            resultsListView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            resultsListView.setVisibility(View.VISIBLE);
        }
    }


    public void onClickLocate(View view){
        setResult(RESULT_OK);
        finish();
    }

    private class SearchLocation extends AsyncTask<String, Void, List<Address>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar(true);
        }

        @Override
        protected List<Address> doInBackground(String... strings) {
            Log.d(TAG, "Searching for " + strings[0]);
            List<Address> addressList = null;
            if(Geocoder.isPresent()){
                try {
                    addressList = geocoder.getFromLocationName(strings[0], 7);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }/* I can use this to support no google apps devices
            else {
                try {
                    GeoApiContext geoApiContext = new GeoApiContext().setApiKey(Tools.MAPS_API_KEY);
                    addressList = new ArrayList<>();
                    GeocodingResult geocodingResult = GeocodingApi.geocode(geoApiContext, strings[0]).await()[0];
                    Log.d(TAG,"AddressCompts length = "+geocodingResult.addressComponents.length);
                    for(int i = 0; i < geocodingResult.addressComponents.length; i++){
                        Log.d(TAG, i +" "+ geocodingResult.addressComponents[i].longName);
                        Log.d(TAG, i +" "+ geocodingResult.addressComponents[i].shortName);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/



            return addressList;
        }


        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);

            showProgressBar(false);
            searchResultsAdapter.clear();
            if(addresses == null){
                Toast.makeText(LocationSearchActivity.this, getString(R.string.oops_location_search_error), Toast.LENGTH_LONG).show();
            }else{
                searchResultsAdapter.addAll(addresses);
            }

        }
    }


    private class SearchResultsAdapter extends ArrayAdapter<Address> {

        private Address address;

        public SearchResultsAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            address = getItem(position);
            if(convertView==null){
                LayoutInflater inflater = LocationSearchActivity.this.getLayoutInflater();
                convertView = inflater.inflate( android.R.layout.simple_list_item_2, parent, false);
            }



            ((TextView)convertView.findViewById(android.R.id.text1)).setText( address.getAddressLine(0)+", "+address.getAddressLine(1) );

            if(address.getAddressLine(2)==null || address.getAddressLine(2).isEmpty()){
                ((TextView)convertView.findViewById(android.R.id.text2)).setVisibility(View.GONE);
            }else{
                ((TextView)convertView.findViewById(android.R.id.text2)).setVisibility(View.VISIBLE);
                ((TextView)convertView.findViewById(android.R.id.text2)).setText( address.getAddressLine(2) );
            }



            return convertView;
        }


    }

}
