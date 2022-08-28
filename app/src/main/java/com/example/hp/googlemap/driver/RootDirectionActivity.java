package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hp.googlemap.R;

public class RootDirectionActivity extends AppCompatActivity
{
    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_direction);
        btn_exit=findViewById(R.id.btn_exit);
    }
    public void onButtonClickbackexit(View view) {
        Intent intent = new Intent(RootDirectionActivity.this, MeetYourRiderStartTripActivity.class);
        startActivity(intent);
    }
}
