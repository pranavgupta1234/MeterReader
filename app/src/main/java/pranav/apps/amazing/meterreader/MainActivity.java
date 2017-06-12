package pranav.apps.amazing.meterreader;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @OnClick(R.id.submit)
    void saveReading(){

        final Reading reading = new Reading("flat_id",newReading.getText().toString(),String.valueOf(System.currentTimeMillis()),remarks.getText().toString(),
                sessionManager.getmName(),"offline");
        dbManagerLocal.add(reading);
        new AlertDialog.Builder(this)
                .setTitle("New Reading")
                .setMessage("Your entry has been successfully noted")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbManagerLocal.add(reading);
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
