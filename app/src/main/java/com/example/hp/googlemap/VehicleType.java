package com.example.hp.googlemap;

/**
 * Created by opprss on 2/23/2018.
 */

class VehicleType {
    private String amount;

    public VehicleType(String vehicle_type_name) {
        this.amount = vehicle_type_name;
    }

    public String getVehicle_type_name() {
        return amount;
    }

    public void setVehicle_type_name(String vehicle_type_name) {
        this.amount = vehicle_type_name;
    }

    @Override
    public String toString() {
        return amount;
    }
}
