package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hp.googlemap.R;

public class TranjactionHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranjaction_history);
    }
    public void onButtonClick(View view) {
        Intent intent=new Intent(TranjactionHistoryActivity.this,EarningActivity.class);
        startActivity(intent);
    }
}
