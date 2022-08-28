package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.googlemap.R;

public class AlldoneActivity extends AppCompatActivity
{
    TextView tv_car_tire,tv_baseRate,tv_minDailyPay,tv_tripCluster,tv_driverId,tv_randomBonus;

    Button btn_alldone;
    DriverDto driverDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alldone);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        btn_alldone=(Button)findViewById(R.id.btn_alldone);
        btn_alldone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AlldoneActivity.this, "Thank You! Your information has been submitted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AlldoneActivity.this, DriverLogInPage.class);
                startActivity(intent);
            }
        });

        initView();
        readFromBundle();
    }

    private void initView() {
        tv_car_tire=findViewById(R.id.tv_car_tire);
        tv_baseRate=findViewById(R.id.tv_baseRate);
        tv_minDailyPay=findViewById(R.id.tv_minDailyPay);
        tv_tripCluster=findViewById(R.id.tv_tripCluster);
        tv_driverId=findViewById(R.id.tv_driverId);
        tv_randomBonus=findViewById(R.id.tv_randomBonus);
    }

    private void readFromBundle() {
        driverDto= (DriverDto) getIntent().getExtras().getSerializable("driverDto");
        tv_car_tire.setText(driverDto.carTire);
        tv_baseRate.setText(driverDto.baseRate);
        tv_minDailyPay.setText(driverDto.minDailyPay);
        tv_tripCluster.setText(driverDto.tripCluster);
        tv_driverId.setText(driverDto.driverId);
        tv_randomBonus.setText(driverDto.randomBonus);
    }
}
