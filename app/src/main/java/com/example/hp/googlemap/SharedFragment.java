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

public class SharedFragment extends Fragment {
    String CurrentAPIurl = "http://45.55.64.233/Miles/api/trip_shared.php";
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
    SharedFragmentAdapter sharedFragmentAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView txtNotDataFound;
    private RecyclerView scheduledRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shared, container, false);
        sharedPreferences = getContext().getSharedPreferences("user_pref", 0);
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
                Log.i("SharedTripResponse", "onResponse: " + response);
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
                    //Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
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
                            String sender_name = jsonObject.optString("sender_name");
                            String rider_name = jsonObject.optString("rider_name");
                            String pickup_time = jsonObject.optString("pickup_time");
                            String booking_time = jsonObject.optString("booking_time");

                            Vehicle vehicle = new Vehicle();
                            vehicle.setSharedHistoryPickUpAddress(pickup_address);
                            vehicle.setSharedHistoryDropAddress(drop_address);
                            vehicle.setSharedHistorySenderName(sender_name);
                            vehicle.setSharedHistoryRiderName(rider_name);
                            vehicle.setSharedHistoryPickUpTime(pickup_time);
                            vehicle.setSharedHistoryBookingTime(booking_time);

                            vehicleArrayList.add(vehicle);
                        }
                        scheduledRecyclerView.setLayoutManager(layoutManager);
                        sharedFragmentAdapter = new SharedFragmentAdapter(vehicleArrayList, getContext());
                        scheduledRecyclerView.setAdapter(sharedFragmentAdapter);
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
