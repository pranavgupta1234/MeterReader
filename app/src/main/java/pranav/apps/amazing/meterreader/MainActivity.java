package pranav.apps.amazing.meterreader;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pranav.apps.amazing.meterreader.application.MeterReader;
import pranav.apps.amazing.meterreader.dbmanager.DBManagerLocal;
import pranav.apps.amazing.meterreader.pojo.Details;
import pranav.apps.amazing.meterreader.pojo.Reading;
import pranav.apps.amazing.meterreader.pojo.Residents;
import pranav.apps.amazing.meterreader.session.SessionManager;

/**
 * Created by Pranav Gupta on 6/9/2017.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.submit)
    ImageButton submit;
    @BindView(R.id.remarks)
    EditText remarks;
    @BindView(R.id.reading)
    EditText newReading;
    @BindView(R.id.campus_no)
    Spinner campusSpinner;
    @BindView(R.id.building_no)
    Spinner buildingSpinner;
    @BindView(R.id.flat_no)
    Spinner flatSpinner;
    @BindView(R.id.flat_no_text)
    TextView flat_no_text;
    @BindView(R.id.name_text)
    TextView name_text;
    @BindView(R.id.meter_no_text)
    TextView meter_no_text;
    @BindView(R.id.capacity_text)
    TextView capacity_text;
    @BindView(R.id.date_text)
    TextView date_text;

    private SessionManager sessionManager;
    private DBManagerLocal dbManagerLocal;
    private ProgressDialog progressDialog;
    private static final String URL_GET_CAMPUSES = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/campuses.php";
    private static final String URL_GET_BUILDINGS = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/buildings.php";
    private static final String URL_GET_FLATS = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/flats.php";
    private static final String URL_NEW_READING = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/reading.php";
    private static String TAG = MainActivity.class.getSimpleName();
    private static ArrayList<Residents> residents = new ArrayList<>();
    private String campus_s;
    private String building_s;
    private String flat_s;
    private Integer offline = 0;
    private Integer uploaded= 0;

    // temporary string to show the parsed response
    private String jsonResponse;

    @OnClick(R.id.submit)
    void saveReading() {

        if (!building_s.contentEquals("Select Building") && !campus_s.contentEquals("Select Campus") && !flat_s.contentEquals("Select Flat")) {

            final Reading reading = new Reading(campus_s+building_s+flat_s, newReading.getText().toString(), String.valueOf(System.currentTimeMillis()), remarks.getText().toString(),
                    sessionManager.getmName(), "0");

            new AlertDialog.Builder(this)
                    .setTitle("New Reading")
                    .setMessage("Are you sure to make a new entry ?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!dbManagerLocal.checkIfPresent(reading)) {
                                dbManagerLocal.add(reading);
                                Toast.makeText(MainActivity.this, "Entry added OFFLINE", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Reading already saved !", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        } else {
            Toast.makeText(MainActivity.this, "Please make proper selections", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //fetch the session
        sessionManager = new SessionManager(MainActivity.this);
        dbManagerLocal = new DBManagerLocal(MainActivity.this, null, 2);

        //adding toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setLogo(R.mipmap.ic_launcher);

        loadResidents();

        /*Populate Campus dropdown(spinner) with a list of districts in HP*/
        populateCampusSpinner();

        /*Populates the Building dropdown(spinner) with a dummy option of "Select Building" */
        populateBuildingSpinner();

        /*Populates the Flat dropdown(spinner) with a dummy option of "Select Flat" */
        populateFlatDropdown();

        /*When a campus is selected this method updates the building spinner according to the selected district*/
        setCampusChangeListener();

        /*When a police station is selected this method updates the police post spinner according to selected police post*/
        setBuildingChangeListener();

        setFlatChangeListener();

    }

    private void setFlatChangeListener() {

        flatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                flat_s = (String) parent.getItemAtPosition(position);

                for (int i = 0; i < residents.size(); i++) {
                    if (residents.get(i).getCampus().contentEquals(campus_s) && residents.get(i).getFlat_no().contentEquals(flat_s) &&
                            residents.get(i).getBuilding_no().contentEquals(building_s)) {
                        flat_no_text.setText(residents.get(i).getFlat_no());
                        name_text.setText(residents.get(i).getName());
                        meter_no_text.setText(residents.get(i).getMeter_no());
                        capacity_text.setText("5.20A, 3 x 240V, 50Hz");
                        date_text.setText("Initial date Not available");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadResidents() {

        residents.add(new Residents("Dr. Pradyumna Kumar Pathak", "102552", "17152", "South Campus", "C-1", "GF,NS", ""));
        residents.add(new Residents("Dr. Shyamashri Dass Gupta", "102581", "15682", "South Campus", "C-1", "GF,KS", ""));
        residents.add(new Residents("Dr. B. Subramanian", "102570", "12200", "South Campus", "C-1", "FF,KS", ""));
        residents.add(new Residents("Dr. Aditya Nigam", "102568", "5863", "South Campus", "C-1", "FF,NS", ""));
        residents.add(new Residents("Dr. Manoj Thakur", "102563", "15893", "South Campus", "C-1", "SF,NS", ""));
        residents.add(new Residents("Dr. Ankush Bag", "102566", "10686", "South Campus", "C-1", "SF,KS", ""));
        residents.add(new Residents("Dr. Rajendra Kumar Ray", "102578", "9249", "South Campus", "C-2", "GF,NS", ""));
        residents.add(new Residents("Dr. Satyajit Thakur", "102564", "10201", "South Campus", "C-2", "FF,NS", ""));
        residents.add(new Residents("Dr. Chayan Kant Nandi", "102580", "5931", "South Campus", "C-2", "FF, KS", ""));
        residents.add(new Residents("Dr. Pradeep Parameshhwaran", "102579", "10156", "South Campus", "C-2", "Second Floor", ""));
        residents.add(new Residents("Dr. Venkata Krishan", "102574", "4628", "South Campus", "C-3", "Second Floor", ""));
        residents.add(new Residents("Dr. Tushar Jain", "102565", "8620", "South Campus", "C-3", "GF,KS", ""));
        residents.add(new Residents("Dr. Satvasheel R Powar", "102576", "17986", "South Campus", "C-3", "GF,NS", ""));
        residents.add(new Residents("Dr. Kunal Ghosh", "102575", "13892", "South Campus", "C-3", "FF,NS", ""));


    }


    private void populateCampusSpinner() {

        /*Create an ArrayAdapter for all the districts for the districts dropdown*/
        ArrayAdapter<CharSequence> CampusArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.campuses, R.layout.spinner_layout);

        /*Specify the layout to use when the list of choices appears*/
        CampusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /*Apply the adapter to the spinner*/
        campusSpinner.setAdapter(CampusArrayAdapter);

    }

    private void populateBuildingSpinner() {

        /*Until a real district is selected show "Select Police Station" in the dropdown*/
        /*Create an array adapter for "Select Police Station"*/
        final ArrayAdapter<CharSequence> BuildingArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.option_building, R.layout.spinner_layout);

        /*Specify the layout to use when the list of choices appears*/
        BuildingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        /*Apply the adapter to the spinner*/
        buildingSpinner.setAdapter(BuildingArrayAdapter);

    }


    private void populateFlatDropdown() {

        /*Until a real police Station is selected show "Select Police Post" in the dropdown*/
        /*Create an array adapter for "Select Police Post"*/
        final ArrayAdapter<CharSequence> FlatArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.option_flat, R.layout.spinner_layout);

        /*Specify the layout to use when the list of choices appears*/
        FlatArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /*Apply the adapter to the spinner*/
        flatSpinner.setAdapter(FlatArrayAdapter);


    }

    private void setCampusChangeListener() {

        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String campus = (String) parent.getItemAtPosition(position);
                campus_s = campus;
                ArrayAdapter<CharSequence> adapter_building = ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.option_building, R.layout.spinner_layout);

                switch (campus) {
                    case "South Campus":
                        adapter_building = ArrayAdapter.createFromResource(getBaseContext(),
                                R.array.building_south, R.layout.spinner_layout);
                        break;
                    case "North Campus":

                        break;
                    case "Mandi Campus":

                        break;
                    case "Salgi/School Campus":

                        break;
                }

                buildingSpinner.setAdapter(adapter_building);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void setBuildingChangeListener() {

        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String building_no = (String) parent.getItemAtPosition(position);
                building_s = building_no;
                ArrayAdapter<CharSequence> adapter_flat = ArrayAdapter.createFromResource(getBaseContext(),
                        R.array.option_flat, R.layout.spinner_layout);

                switch (building_no) {

                    case "C-1":
                        adapter_flat = ArrayAdapter.createFromResource(getBaseContext(),
                                R.array.flat_c1, R.layout.spinner_layout);
                        break;
                    case "C-2":
                        adapter_flat = ArrayAdapter.createFromResource(getBaseContext(),
                                R.array.flat_c2, R.layout.spinner_layout);
                        break;
                    case "C-3":
                        adapter_flat = ArrayAdapter.createFromResource(getBaseContext(),
                                R.array.flat_c3, R.layout.spinner_layout);
                        break;
                }

                flatSpinner.setAdapter(adapter_flat);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sync:
                offline = 0;
                showProcessDialog();
                ArrayList<Reading> allReadings = dbManagerLocal.get();
                for(int i=0 ; i<allReadings.size() ; i++){
                    if(allReadings.get(i).getStatus().contentEquals("0")){
                        offline++;
                    }
                }
                if(offline!=0){
                    for(int i=0 ; i<allReadings.size() ; i++){
                        if(allReadings.get(i).getStatus().contentEquals("0")){
                            storeReading(String.valueOf(i+1),allReadings.get(i).getNewReading(),allReadings.get(i).getRemarks(),allReadings.get(i).getTakenBy());
                            dbManagerLocal.setStatus(allReadings.get(i),"1");
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Everything is in SYNC",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


                break;
        }

        return true;
    }

    /**
     * Function to store new reading in MySQL database will post params
     */
    private void storeReading(final String entry_number, final String newReading, final String remarks, final String name) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_NEW_READING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        updateMessage(entry_number);
                        uploaded++;

                        if(Objects.equals(uploaded, offline)){
                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error");
                        Toast.makeText(getApplicationContext(),"Error in uploading data, please try later !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LoginActivity.class.getSimpleName(), "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("flat", flat_s);
                params.put("building", building_s.replace("-", ""));
                params.put("ReadingValue", newReading);
                params.put("Remarks", remarks);
                params.put("TakenBy", sessionManager.getmName());
                return params;
            }

        };

        // Adding request to request queue
        MeterReader.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showProcessDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Smart Sync");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Syncing data with online server");
        progressDialog.show();

    }

    private void updateMessage(String msg){
        progressDialog.setMessage("Sending entries "+msg+" out of "+offline);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
