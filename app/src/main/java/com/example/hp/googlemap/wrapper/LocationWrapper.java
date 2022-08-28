package com.example.hp.googlemap.wrapper;

import com.example.hp.googlemap.riderdto.LocationDto;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationWrapper implements Serializable {
    public ArrayList<LocationDto> predictions;
    public String status;
}
