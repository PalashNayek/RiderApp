package com.example.hp.googlemap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SharedFragmentAdapter extends RecyclerView.Adapter<SharedFragmentAdapter.GatePassViewHolder> {
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;

    public SharedFragmentAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }

    @Override

    public SharedFragmentAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_trip_history, parent, false);
        SharedFragmentAdapter.GatePassViewHolder gatePassViewHolder = new SharedFragmentAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(SharedFragmentAdapter.GatePassViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get(position);
        holder.txtPickUpAddress.setText(vehicle.getSharedHistoryPickUpAddress());
        holder.txtDropAddress.setText(vehicle.getSharedHistoryDropAddress());
        holder.txtSenderName.setText(vehicle.getSharedHistorySenderName());
        holder.txtRiderName.setText(vehicle.getSharedHistoryRiderName());
        holder.txtPickupTime.setText(vehicle.getSharedHistoryPickUpTime());
        holder.txtBookingDateTime.setText(vehicle.getSharedHistoryBookingTime());

    }

    @Override

    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView txtPickUpAddress, txtDropAddress, txtSenderName,txtRiderName,txtPickupTime,txtBookingDateTime ;
        ArrayList<Vehicle> vehicleList;
        Context context;

        public GatePassViewHolder(View itemView, Context context, ArrayList<Vehicle> vehicleList) {

            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
            txtPickUpAddress = (TextView) itemView.findViewById(R.id.txtPickUpAddress);
            txtDropAddress = (TextView) itemView.findViewById(R.id.txtDropAddress);
            txtSenderName = (TextView) itemView.findViewById(R.id.txtSenderName);
            txtRiderName = (TextView) itemView.findViewById(R.id.txtRiderName);
            txtPickupTime = (TextView) itemView.findViewById(R.id.txtPickupTime);
            txtBookingDateTime = (TextView) itemView.findViewById(R.id.txtBookingDateTime);


        }
    }
}
