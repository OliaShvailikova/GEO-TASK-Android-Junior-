package com.example.home.map.Places;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.home.map.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import static com.example.home.map.FindAPath.DirectionFinder.origin;

public class FromActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnSaveLocation, btnDeleteLocation;
    private AutoCompleteTextView etOrigin;
    private PlacesTask placesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
        btnDeleteLocation = (Button) findViewById(R.id.btnDeleteLocation);
        etOrigin = (AutoCompleteTextView) findViewById(R.id.atv_origin);
        etOrigin.setThreshold(1);
        etOrigin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask(getBaseContext(),etOrigin);
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });

        btnDeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLocation();
            }
        });


    }

    private void saveLocation() {
        origin = etOrigin.getText().toString();
        if (!origin.isEmpty()) {
            new GeocoderTask(this, mMap).execute(origin);
        } else {
            Toast.makeText(this, getString(R.string.empty_origin), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteLocation() {
        origin = "";
        etOrigin.setText("");
        LatLng nullLocation = new LatLng(0, 0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nullLocation, 0));
        mMap.clear();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng nullLocation = new LatLng(0, 0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nullLocation, 0));
    }

}