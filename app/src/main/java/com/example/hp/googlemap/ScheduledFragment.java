package com.example.hp.googlemap;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduledFragment extends Fragment {
    String CurrentAPIurl = "http://45.55.64.233/Miles/api/trip_sheduled.php";
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
    ScheduledFragmentAdapter scheduledFragmentAdapter;
    private RecyclerView scheduledRecyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView txtNotDataFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scheduled, container, false);
        sharedPreferences = getContext().getSharedPreferences( "user_pref", 0 );
        editor = sharedPreferences.edit();
        layoutManager = new LinearLayoutManager(inflater.getContext());
        scheduledRecyclerView = rootView.findViewById(R.id.scheduledRecyclerView);
        txtNotDataFound = rootView.findViewById(R.id.txtNotDataFound);
        //Call Current Function
        currentFunc();
        return rootView;
    }
    //Current Function............
    private void currentFunc() {
        StringRequest dash_request = new StringRequest(Request.Method.POST, CurrentAPIurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("ScheduledTripResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error something occurs", "onResponse: " + e);
                }
                if (status == 2) {
                    txtNotDataFound.setVisibility(View.VISIBLE);
                    txtNotDataFound.setText(msg);
                }
                if (status == 1) {
                    if (!vehicleArrayList.isEmpty()) {
                        vehicleArrayList.clear();

                    }
                    try {
                        JSONObject jsonRootObject = new JSONObject(response);
                        JSONArray jsonArray = jsonRootObject.getJSONArray("trip_details");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String pickup_address = jsonObject.optString("pickup_address");
                            String drop_address = jsonObject.optString("drop_address");
                            String scheduled_dt = jsonObject.optString("scheduled_dt");
                            String scheduled_time = jsonObject.optString("scheduled_time");
                            String booking_time = jsonObject.optString("booking_time");

                            Vehicle vehicle = new Vehicle();
                            vehicle.setScheduledHistoryPickUpAddress(pickup_address);
                            vehicle.setScheduledHistoryDropAddress(drop_address);
                            vehicle.setScheduledHistoryDate(scheduled_dt);
                            vehicle.setScheduledHistoryTime(scheduled_time);
                            vehicle.setScheduledHistoryBookingDateTime(booking_time);

                            vehicleArrayList.add(vehicle);
                        }
                        scheduledRecyclerView.setLayoutManager(layoutManager);
                        scheduledFragmentAdapter = new ScheduledFragmentAdapter(vehicleArrayList, getContext());
                        scheduledRecyclerView.setAdapter(scheduledFragmentAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sharedPreferences.getString("user_id", ""));
                Log.i("Mona", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(dash_request);
    }

}
