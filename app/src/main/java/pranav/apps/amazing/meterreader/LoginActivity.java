package pranav.apps.amazing.meterreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pranav.apps.amazing.meterreader.application.MeterReader;
import pranav.apps.amazing.meterreader.session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private SessionManager session;

    @BindView(R.id.name) EditText name;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.login) ImageView login;
    private String URL_LOGIN = "https://pranavgupta4321.000webhostapp.com/metereader/endpoints/login.php";
    private ProgressDialog progressDialog;

    @OnClick(R.id.login)
    void validateCredentials(){
        if(name.getText().toString().contentEquals("") || password.getText().toString().contentEquals("")){
            if(name.getText().toString().contentEquals("") && password.getText().toString().contentEquals("")){
                name.setError("Please enter your name");
                password.setError("Please enter your password");
            }else if(password.getText().toString().contentEquals("")){
                password.setError("Please enter your password");
            }else if(name.getText().toString().contentEquals("")){
                name.setError("Please enter your name");
            }
        }else {
            checkLogin(name.getText().toString(),password.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String name, final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        showProcessDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    response = response.substring(response.indexOf("{"),response.lastIndexOf("}")+1);
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        progressDialog.dismiss();

                        session = new SessionManager(LoginActivity.this);
                        session.createLoginSession(name);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        // user successfully logged in

/*                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");*/

                    } else {
                        progressDialog.dismiss();
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("name", name);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        MeterReader.getInstance().addToRequestQueue(strReq, tag_string_req);
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
