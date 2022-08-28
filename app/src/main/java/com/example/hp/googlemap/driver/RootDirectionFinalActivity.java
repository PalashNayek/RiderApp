package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hp.googlemap.R;

public class RootDirectionFinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_direction_final);
    }
    public void onButtonClickbackexit(View view) {
        Intent intent = new Intent(RootDirectionFinalActivity.this, RateYourRiderActivity.class);
        startActivity(intent);
    }
}
