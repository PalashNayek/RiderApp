package com.example.hp.googlemap.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDto implements Serializable
{
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("driver_id")
    @Expose
    public String driverId;
    @SerializedName("car_tire")
    @Expose
    public String carTire;
    @SerializedName("base_rate")
    @Expose
    public String baseRate;
    @SerializedName("min_daily_pay")
    @Expose
    public String minDailyPay;
    @SerializedName("random_bonus")
    @Expose
    public String randomBonus;
    @SerializedName("trip_cluster")
    @Expose
    public String tripCluster;
    @SerializedName("driver_no")
    @Expose
    public String driverNo;
    @SerializedName("message")
    @Expose
    public String message;
}
