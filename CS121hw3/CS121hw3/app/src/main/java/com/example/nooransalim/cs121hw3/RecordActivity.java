package com.example.nooransalim.cs121hw3;

import java.io.File;
import java.io.FileInputStream;
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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.app.ListActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {


    static final public String MYPREFS = "myprefs";
    static final public String PREF_URL = "restore_url";
    static final public String WEBPAGE_NOTHING = "about:blank";
    static final public String MY_WEBPAGE = "https://users.soe.ucsc.edu/~dustinadams/CMPS121/assignment3/www/index.html";
    static final public String LOG_TAG = "webview_example";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    String AudioPathInDevice;
    Random random ;
    int numMemos;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        if(!checkPermission()){
            requestPermission();
        }

        myWebView = (WebView) findViewById(R.id.webView1);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Binds the Javascript interface
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        myWebView.loadUrl(MY_WEBPAGE);
        //random = new Random();

    }

    public class JavaScriptInterface {
        Context mContext; // Having the context is useful for lots of things,
        // like accessing preferences.

        /**
         * Instantiate the interface and set the context
         */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void record() {
            Log.i(LOG_TAG, "I am in the javascript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    /*Method code here*/

                    try{
                        File f = new File(getFilesDir(), "file.ser");
                        FileInputStream fin = new FileInputStream(f);
                        ObjectInputStream oin = new ObjectInputStream(fin);
                        String j = null;
                        try{
                            j = (String) oin.readObject();
                        }
                        catch(ClassNotFoundException c){
                            c.printStackTrace();
                        }
                        numMemos = Integer.parseInt(j)+1;
                        oin.close();
                        fin.close();
                    }
                    catch(IOException e){
                        numMemos = 1;
                    }
                    AudioPathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+numMemos+".3gp";
                    MediaRecorderReady();
                    try {
                        // recording starts
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RecordActivity.this, "Recording",
                            Toast.LENGTH_LONG).show();
                }
            });

        }

        @JavascriptInterface
        public void stop() {
            Log.i(LOG_TAG, "I am in the javascript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    /*Method code here*/
                    mediaRecorder.stop();

                    // There's no JSON file that exists, so don't
                    // show the list. But also don't worry about creating
                    // the file just yet, that takes place in AddText.
                    try{
                        File f = new File(getFilesDir(), "file.ser");
                        FileOutputStream fout = new FileOutputStream(f);
                        ObjectOutputStream oout = new ObjectOutputStream(fout);
                        String j = Integer.toString(numMemos);
                        oout.writeObject(j);
                        oout.close();
                        fout.close();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(RecordActivity.this, "Stopping",
                            Toast.LENGTH_LONG).show();
                }
            });

        }

        @JavascriptInterface
        public void play() {
            Log.i(LOG_TAG, "I am in the javascript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    /*Method code here*/
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(AudioPathInDevice);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();// play the audio
                    Toast.makeText(RecordActivity.this, "Recording Playing",
                            Toast.LENGTH_LONG).show();
                }
            });

        }

        @JavascriptInterface
        public void stoprec() {
            Log.i(LOG_TAG, "I am in the javascript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    /*Method code here*/
                    if (mediaPlayer != null) {
                        mediaPlayer.stop(); // stop audio
                        mediaPlayer.release(); // free up memory
                        MediaRecorderReady();
                    }
                    Toast.makeText(RecordActivity.this, "Stopping recording",
                            Toast.LENGTH_LONG).show();
                }
            });

        }

        @JavascriptInterface
        public void exit(){
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    Toast.makeText(RecordActivity.this, "Exiting the Webview",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RecordActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }



    }



    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioPathInDevice);
    }
    // method to create a random file name
    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }
    // permissions from user
    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    // callback method
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecordActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}

