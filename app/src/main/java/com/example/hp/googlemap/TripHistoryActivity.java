package com.example.hp.googlemap;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hp.googlemap.rider.DashActivity;

public class TripHistoryActivity extends AppCompatActivity
{
    ViewPager viewPager;
    TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history3);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        tab=(TabLayout)findViewById(R.id.tab);

        FragmentManager manager=getSupportFragmentManager();
        MyViewPageAdapter adapter=new MyViewPageAdapter(manager);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);
        ImageView backArrowbtn=(ImageView)findViewById(R.id.backArrowbtn);
        backArrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TripHistoryActivity.this,DashActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MyViewPageAdapter extends FragmentPagerAdapter {
        public MyViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CurrentFragment();
                case 1:
                    return new ScheduledFragment();
                case 2:
                    return new SharedFragment();
                case 3:
                    return new PreviousFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Current";
                case 1:
                    return "Schedule";
                case 2:
                    return "Shared";
                case 3:
                    return "Previous";
            }
            return super.getPageTitle(position);
        }
    }
}
