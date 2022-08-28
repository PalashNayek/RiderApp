package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hp.googlemap.R;

public class EarningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
    }
    public void onButtonClickback(View view) {
        Intent intent= new Intent(EarningActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    public void onButtonClickfront(View view) {
        Intent intent= new Intent(EarningActivity.this,TranjactionHistoryActivity.class);
        startActivity(intent);
    }
}
