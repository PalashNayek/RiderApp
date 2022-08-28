package com.example.hp.googlemap.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.hp.googlemap.R;

public class NotificationActivity extends AppCompatActivity
{
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);
        initView();
    }

    private void initView() {
        recyclerView=findViewById(R.id.rv_notificationlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new NotificationAdapter(this));
    }

    public void onButtonClick(View view) {
        onBackPressed();
    }

}
