package com.example.hp.googlemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.googlemap.driver.DriverLogInPage;

public class ChooseActivity extends AppCompatActivity
{
    Button bt_rider,bt_driver;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        bt_rider=findViewById(R.id.bt_rider);
        bt_driver=findViewById(R.id.bt_driver);
        tv = (TextView) this.findViewById(R.id.mywidget);
        tv.setSelected(true);

        //Rider Logo Click.............

        bt_rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
        });

        //Driver Logo Click.............

        bt_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity.this, DriverLogInPage.class);
                startActivity(intent);
            }
        });
    }
}
