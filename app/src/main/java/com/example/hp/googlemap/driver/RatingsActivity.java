package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hp.googlemap.R;

public class RatingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
    }
    public void onButtonClick(View view) {
        Intent intent = new Intent(RatingsActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
