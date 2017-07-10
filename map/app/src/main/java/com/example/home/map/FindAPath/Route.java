package com.example.home.map.FindAPath;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;


public class Route {
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;
    public List<LatLng> points;
}
