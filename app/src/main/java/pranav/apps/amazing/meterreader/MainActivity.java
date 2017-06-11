package pranav.apps.amazing.meterreader;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pranav.apps.amazing.meterreader.pojo.Reading;

/**
 * Created by Pranav Gupta on 6/9/2017.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.submit) Button submit;
    @BindView(R.id.remarks) EditText remarks;
    @BindView(R.id.reading) EditText newReading;

    @OnClick(R.id.submit)
    void saveReading(){

        Reading reading = new Reading("Flat_id",newReading.getText().toString(),"takenon",remarks.getText().toString(),
                "takenby");

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //adding toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
