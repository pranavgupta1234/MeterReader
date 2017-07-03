package pranav.apps.amazing.meterreader;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pranav.apps.amazing.meterreader.application.MeterReader;
import pranav.apps.amazing.meterreader.dbmanager.DBManagerLocal;
import pranav.apps.amazing.meterreader.pojo.Reading;
import pranav.apps.amazing.meterreader.session.SessionManager;

/**
 * Created by Pranav Gupta on 6/9/2017.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.submit) ImageButton submit;
    @BindView(R.id.remarks) EditText remarks;
    @BindView(R.id.reading) EditText newReading;


    private SessionManager sessionManager;
    private DBManagerLocal dbManagerLocal;
    private ProgressDialog progressDialog;
    private static final String URL_GET_CAMPUSES = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/campuses.php";
    private static final String URL_GET_BUILDINGS = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/buildings.php";
    private static final String URL_GET_FLATS = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/flats.php";
    private static final String URL_NEW_READING = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/reading.php";
    private static String TAG = MainActivity.class.getSimpleName();


    // temporary string to show the parsed response
    private String jsonResponse;

    @OnClick(R.id.submit)
    void saveReading(){

        final Reading reading = new Reading("flat_id",newReading.getText().toString(),String.valueOf(System.currentTimeMillis()),remarks.getText().toString(),
                sessionManager.getmName(),"offline");
        dbManagerLocal.add(reading);
        storeReading("flat_id",newReading.getText().toString(),remarks.getText().toString(),sessionManager.getmName());

/*        new AlertDialog.Builder(this)
                .setTitle("New Reading")
                .setMessage("Your entry has been successfully noted")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbManagerLocal.add(reading);
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();*/

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //fetch the session
        sessionManager = new SessionManager(MainActivity.this);
        dbManagerLocal = new DBManagerLocal(MainActivity.this,null,2);

        //adding toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setLogo(R.mipmap.ic_launcher);



        /*Populate Campus dropdown(spinner) with a list of districts in HP*/
        populateCampusSpinner();

        /*Populates the Building dropdown(spinner) with a dummy option of "Select Building" */
        populateBuildingSpinner();

        /*Populates the Flat dropdown(spinner) with a dummy option of "Select Flat" */
        populateFlatDropdown();

        /*When a campus is selected this method updates the building spinner according to the selected district*/
        //setCampusChangeListener();

        /*When a police station is selected this method updates the police post spinner according to selected police post*/
        //setBuildingChangeListener();

    }

    private void populateFlatDropdown() {
    }

    private void populateBuildingSpinner() {
    }

    private void populateCampusSpinner() {
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.sync :
                break;
        }

        return true;
    }

    /**
     * Function to store new reading in MySQL database will post params
     * */
    private void storeReading(final String flat_id, final String newReading,
                              final String remarks,final String name) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        showProcessDialog();

        JsonArrayRequest req = new JsonArrayRequest(URL_GET_CAMPUSES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject campus = (JSONObject)response.get(i);

                                String name = campus.getString("Name");
                                Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



        // Adding request to request queue
        MeterReader.getInstance().addToRequestQueue(req,tag_string_req);
    }

    private void showProcessDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("logging in...");
        progressDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
