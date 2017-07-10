package com.example.home.map.Places;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.home.map.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
    private Context context;
    private GoogleMap mMap;
    private MarkerOptions markerOptions;

    GeocoderTask(Context context, GoogleMap googleMap){
        this.context=context;
        this.mMap=googleMap;
    }

    @Override
    protected List<Address> doInBackground(String... locationName) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(locationName[0], 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        if(addresses==null || addresses.size()==0){
            Toast.makeText(context, context.getString(R.string.no_location_found), Toast.LENGTH_SHORT).show();
        }
        mMap.clear();
        for(int i=0;i<addresses.size();i++){

            Address address = (Address) addresses.get(i);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            String addressText = String.format("%s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getCountryName());

            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(addressText);

            mMap.addMarker(markerOptions);

            if(i==0)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        }
    }
}
