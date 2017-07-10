package com.example.home.map;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.home.map.FindAPath.FindDirection;
import com.example.home.map.Places.FromActivity;
import com.example.home.map.Places.ToActivity;

import static com.example.home.map.FindAPath.DirectionFinder.destination;
import static com.example.home.map.FindAPath.DirectionFinder.origin;

public class GeoTaskActivity extends TabActivity {
    private Button btnFindPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_task);
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAPath();
            }
        });
        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.from));
        tabSpec.setContent(new Intent(this, FromActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.to));
        tabSpec.setContent(new Intent(this, ToActivity.class));
        tabHost.addTab(tabSpec);
    }

    private void findAPath() {
        if (origin.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_origin), Toast.LENGTH_SHORT).show();
            return;
        } else  if (destination.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_destination), Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, FindDirection.class));
    }

}

