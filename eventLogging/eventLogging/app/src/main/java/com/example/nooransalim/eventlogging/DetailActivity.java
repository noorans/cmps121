package com.example.nooransalim.eventlogging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.content.Context;
import android.app.ListActivity;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Button;
import java.io.ObjectOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    public JSONObject jos = null;
    public JSONArray ja = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String description = i.getStringExtra("description");
        String time = i.getStringExtra("time");
        String date = i.getStringExtra("date");
        String longitude = i.getStringExtra("longitude");
        String latitude = i.getStringExtra("latitude");

        Button delete = (Button) findViewById(R.id.delete);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String pos = intent.getStringExtra("pos");
        final int position = Integer.parseInt(pos);
        try{
            // Reading a file that already exists
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                jos = new JSONObject(j);
                ja = jos.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }

        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ja.remove(position);
                try {
                    java.io.File f = new java.io.File(getFilesDir(), "file.ser");
                    java.io.FileOutputStream fi = new java.io.FileOutputStream(f);
                    java.io.ObjectOutputStream o = new java.io.ObjectOutputStream(fi);
                    String j = jos.toString();
                    o.writeObject(j);
                    o.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });


                TextView t = (TextView)findViewById(R.id.textView);
        TextView d = (TextView)findViewById(R.id.textView8);
        TextView t2 = (TextView)findViewById(R.id.textView2);
        TextView d2 = (TextView)findViewById(R.id.textView3);
        TextView la = (TextView)findViewById(R.id.textView4);
        TextView lo = (TextView)findViewById(R.id.textView5);

        t.setText("Title: "+title);
        d.setText("Description: "+description);
        d2.setText("Date: "+date);
        t2.setText("Time: "+time);
        la.setText("Latitude: "+latitude);
        lo.setText("Longitude: "+longitude);




        }
        catch(IOException e){
            // There's no JSON file that exists, so don't
            // show the list. But also don't worry about creating
            // the file just yet, that takes place in AddText.

            //Here, disable the list view
            

            //show the text view
            //text.setVisibility(View.VISIBLE);
        }
    }


        protected void onResume(Bundle savedInstanceState){
        //
    }
}

