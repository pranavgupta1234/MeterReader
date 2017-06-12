package pranav.apps.amazing.meterreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pranav.apps.amazing.meterreader.session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private SessionManager session;

    @BindView(R.id.name) EditText name;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.login) ImageView login;

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
            session = new SessionManager(LoginActivity.this);
            session.createLoginSession(name.getText().toString());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
