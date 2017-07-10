package com.example.home.map.Places;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesTask extends AsyncTask<String, Void, String> {

    private ParserTask parserTask;
    private Context context;
    private AutoCompleteTextView autoCompleteTextView;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private static final String GOOGLE_API_KEY = "key=AIzaSyAfiv4E-B32jIlgOolyzMLbmswsXZdjwj0";


    public PlacesTask(Context context, AutoCompleteTextView autoCompleteTextView){
        this.autoCompleteTextView=autoCompleteTextView;
        this.context=context;
    }

    @Override
    protected String doInBackground(String... place) {
        String data = "";
        String input = "";
        try {
            input = "input=" + URLEncoder.encode(place[0], "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String types = "types=geocode";
        String sensor = "sensor=false";
        String parameters = input + "&" + types + "&" + sensor + "&" + GOOGLE_API_KEY;
        String url = DIRECTION_URL_API + parameters;

        try {
            data = downloadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        parserTask = new ParserTask();
        parserTask.execute(result);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream is = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            is.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                places = parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            String[] fromPlace = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};
            SimpleAdapter adapter = new SimpleAdapter(context, result, android.R.layout.simple_list_item_1, fromPlace, to);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setDropDownHeight(200);
        }
    }

    public List<HashMap<String,String>> parse(JSONObject jObject){
        JSONArray jPlaces = null;
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        try {
            jPlaces = jObject.getJSONArray("predictions");
            int number = jPlaces.length();
            for(int i=0; i<number;i++){
                HashMap<String, String> place = new HashMap<String, String>();
                String description="";
                JSONObject jPlace=(JSONObject)jPlaces.get(i);
                description = jPlace.getString("description");
                place.put("description", description);
                placesList.add(place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placesList;
    }
}
