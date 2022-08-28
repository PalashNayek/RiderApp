package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hp.googlemap.R;

public class DriverForgotPasswordActivity extends AppCompatActivity
{
    ImageView backforgot;
    Button btnsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_forgot_password);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        backforgot=(ImageView) findViewById(R.id.backforgot);

        backforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverForgotPasswordActivity.this, DriverLogInPage.class);
                startActivity(intent);
            }
        });

        btnsubmit=(Button) findViewById(R.id.btnsubmit);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverForgotPasswordActivity.this, DriverLogInPage.class);
                startActivity(intent);
            }
        });
    }
}
