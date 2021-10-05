package com.example.nooransalim.eventlogging;

import java.io.IOException;
import java.io.InputStream;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;





public class MainActivity extends AppCompatActivity {
    //private ListView mListView;
    //private final String TAG = "ListView";

    public JSONObject jos = null;
    public JSONArray ja = null;
    private static final String TAG = "JSON_LIST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    protected void onResume(){
        super.onResume();
        ListView list = findViewById(R.id.data_list_view);
      //  TextView text = findViewById(R.id.text);
       // text.setVisibility(View.INVISIBLE);

        android.util.Log.d(TAG, ""+getFilesDir());

        jos = null;
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
            }
            try {
                jos = new JSONObject(j);
                ja = jos.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }


        // The ListData class (created by me) simply holds a String for title and String for description.
        // See the ListData class for more details.
        final ArrayList<ListData> aList = new ArrayList<ListData>();

        // create ListData objects from data and store in ArrayList called list
        for(int i = 0; i < ja.length(); i++){

            ListData ld = new ListData();
            try {
                ld.title = ja.getJSONObject(i).getString("title");
                ld.description = ja.getJSONObject(i).getString("description");
                ld.date = ja.getJSONObject(i).getString("date");
                ld.time = ja.getJSONObject(i).getString("time");
                ld.latitude = ja.getJSONObject(i).getString("latitude");
                ld.longitude = ja.getJSONObject(i).getString("longitude");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            aList.add(ld);
        }

        // Create an array and assign each element to be the title
        // field of each of the ListData objects (from the array list)
        String[] listItems = new String[aList.size()];

        for(int i = 0; i < aList.size(); i++){
            ListData listD = aList.get(i);
            listItems[i] = listD.title;
        }

        // Show the list view with the each list item an element from listItems
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

        // Set an OnItemClickListener for each of the list items
        final Context context = this;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListData selected = aList.get(position);


                // Create an Intent to reference our new activity, then call startActivity
                // to transition into the new Activity.
                Intent detailIntent = new Intent(context, DetailActivity.class);

                // pass some key value pairs to the next Activity (via the Intent)
                detailIntent.putExtra("title", selected.title);
                detailIntent.putExtra("description", selected.description);
                detailIntent.putExtra("date", selected.date);
                detailIntent.putExtra("time", selected.time);
                detailIntent.putExtra("latitude", selected.latitude);
                detailIntent.putExtra("longitude", selected.longitude);
                detailIntent.putExtra("pos", position+"");

                startActivity(detailIntent);
            }

        });

    }
    catch(IOException e){
            // There's no JSON file that exists, so don't
            // show the list. But also don't worry about creating
            // the file just yet, that takes place in AddText.

            //Here, disable the list view
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);

            //show the text view
            //text.setVisibility(View.VISIBLE);
        }
    }

    // This method will just show the menu item (which is our button "ADD")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /* Here is the event handler for the menu button that I forgot in class.
    The value returned by item.getItemID() is
     */
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        android.util.Log.d(TAG, String.format("" + item.getItemId()));
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                /*the R.id.action_favorite is the ID of our button (defined in strings.xml).
                Change Activity here (if that's what you're intending to do, which is probably is).
                 */
                Intent addIntent = new Intent(this, AddText.class);
                startActivity(addIntent);
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}
