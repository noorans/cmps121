package com.example.nooransalim.cs121hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.app.ListActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
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

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "JSON_LIST";
    public int memo;
    MediaPlayer mediaPlayer;
    String AudioSavePathInDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

    }

    protected void onResume() {
        super.onResume();

        ListView list = findViewById(R.id.data_list_view);
        memo =0;
        TextView text = findViewById(R.id.text);
        text.setVisibility(View.INVISIBLE);

        android.util.Log.d(TAG, "" + getFilesDir());


        try {
            // Reading a file that already exists
            java.io.File f = new java.io.File(getFilesDir(), "file.ser");
            java.io.FileInputStream fi = new java.io.FileInputStream(f);
            java.io.ObjectInputStream o = new java.io.ObjectInputStream(fi);
            String j = null;

            try {
                j = (String)o.readObject();
                //memo = Integer.parseInt(j);
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
            memo = Integer.parseInt(j);
            o.close();
            fi.close();


            // create ListData objects from data and store in ArrayList called list


            // Create an array and assign each element to be the title
            // field of each of the ListData objects (from the array list)
            String[] listItems = new String[memo];

            for (int i = 0; i < memo; i++) {
                //ListData listD = aList.get(i);
                listItems[i] = ("Audio Recording ") + (i+1);
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + (position + 1) + ".3gp";
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(AudioSavePathInDevice);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();// play the audio
                    Toast.makeText(MainActivity.this, "Recording Playing",
                            Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            // There's no JSON file that exists, so don't
            // show the list. But also don't worry about creating
            // the file just yet, that takes place in AddText.

            //Here, disable the list view
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);

            //show the text view
            text.setVisibility(View.VISIBLE);
            //}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, String.format("" + item.getItemId()));
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                /*the R.id.action_favorite is the ID of our button (defined in strings.xml).
                Change Activity here (if that's what you're intending to do, which is probably is).
                */

                Intent i = new Intent(this, RecordActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}

