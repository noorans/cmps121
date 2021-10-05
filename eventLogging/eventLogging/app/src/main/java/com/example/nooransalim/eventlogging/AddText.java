package com.example.nooransalim.eventlogging;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.app.ListActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.EditText;

public class AddText extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private final String TAG = "TESTGPS";
    public JSONObject jo = null;
    public JSONArray ja = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start up the Location Service

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        final EditText title = findViewById(R.id.editText2);
        final EditText description = findViewById(R.id.editText3);
        android.widget.Button b = findViewById(R.id.button2);
        java.util.Date date = java.util.Calendar.getInstance().getTime();
        java.util.Calendar time = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String theDate = df.format(date);
        final String[] dt = theDate.split(" ");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        // Read the file


        try{
            java.io.File f = new java.io.File(getFilesDir(), "file.ser");
            java.io.FileInputStream fi = new java.io.FileInputStream(f);
            java.io.ObjectInputStream o = new java.io.ObjectInputStream(fi);
            // Notice here that we are de-serializing a String object (instead of
            // a JSONObject object) and passing the String to the JSONObjectâ€™s
            // constructor. Thatâ€™s because String is serializable and
            // JSONObject is not. To convert a JSONObject back to a String, simply
            // call the JSONObjectâ€™s toString method.
            String j = null;
            try{
                j = (String) o.readObject();
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            try {
                jo = new JSONObject(j);
                ja = jo.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        catch(IOException e){
            // Here, initialize a new JSONObject
            jo = new JSONObject();
            ja = new JSONArray();
            try{
                jo.put("data", ja);
            }
            catch(JSONException j){
                j.printStackTrace();
            }
        }

        b.setOnClickListener(new android.widget.Button.OnClickListener(){
            @SuppressLint("MissingPermission")
            public void onClick(View v){
                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();
                String latt = "0";
                String longt = "0";


                // A reference to the location manager. The LocationManager has already
                // been set up in MyService, we're just getting a reference here.
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                Location l;
                // Go through the location providers starting with GPS, stop as soon
                // as we find one.
                for (int i=providers.size()-1; i>=0; i--) {
                    l = lm.getLastKnownLocation(providers.get(i));
                    longt =(Double.toString(l.getLongitude()));
                    latt=(Double.toString(l.getLatitude()));
                    if (l != null) break;
                }


                JSONObject temp = new JSONObject();
                try {
                    temp.put("title", titleText);
                    temp.put("description", descriptionText);
                    temp.put("date", dt[0]);
                    temp.put("time", dt[1]);
                    temp.put("latitude", latt);
                    temp.put("longitude", longt);
                }
                catch(JSONException j){
                    j.printStackTrace();
                }

                ja.put(temp);

                // write the file
                try{
                    java.io.File f = new java.io.File(getFilesDir(), "file.ser");
                    java.io.FileOutputStream fo = new java.io.FileOutputStream(f);
                    java.io.ObjectOutputStream o = new java.io.ObjectOutputStream(fo);
                    String j = jo.toString();
                    o.writeObject(j);
                    o.close();
                    fo.close();
                }
                catch(IOException e){

                }

                //pop the activity off the stack
                Intent i = new Intent(AddText.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "callback");
        switch (requestCode) {
            case 99:
                // If the permissions aren't set, then return. Otherwise, proceed.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                                , 10);
                    }
                    Log.d(TAG, "returning program");
                    return;
                }
                else{
                    // Create Intent to reference MyService, start the Service.
                    Log.d(TAG, "starting service");
                    Intent i = new Intent(this, MyService.class);
                    if(i==null)
                        Log.d(TAG, "intent null");
                    else{
                        startService(i);
                    }

                }
                break;
            default:
                break;
        }
    }
}
