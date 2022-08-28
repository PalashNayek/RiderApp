package com.example.hp.googlemap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduledFragmentAdapter extends RecyclerView.Adapter<ScheduledFragmentAdapter.GatePassViewHolder> {
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;

    public ScheduledFragmentAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }

    @Override

    public ScheduledFragmentAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduled_trip_history, parent, false);
        ScheduledFragmentAdapter.GatePassViewHolder gatePassViewHolder = new ScheduledFragmentAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(ScheduledFragmentAdapter.GatePassViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get(position);
        holder.txtPickUpAddress.setText(vehicle.getScheduledHistoryPickUpAddress());
        holder.txtDropAddress.setText(vehicle.getScheduledHistoryDropAddress());
        holder.txtScheduledDate.setText(vehicle.getScheduledHistoryDate());
        holder.txtScheduledTime.setText(vehicle.getScheduledHistoryTime());
        holder.txtBookingDateTime.setText(vehicle.getScheduledHistoryBookingDateTime());

    }

    @Override

    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView txtPickUpAddress, txtDropAddress, txtScheduledDate,txtScheduledTime,txtBookingDateTime ;
        ArrayList<Vehicle> vehicleList;
        Context context;

        public GatePassViewHolder(View itemView, Context context, ArrayList<Vehicle> vehicleList) {

            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
            txtPickUpAddress = (TextView) itemView.findViewById(R.id.txtPickUpAddress);
            txtDropAddress = (TextView) itemView.findViewById(R.id.txtDropAddress);
            txtScheduledDate = (TextView) itemView.findViewById(R.id.txtScheduledDate);
            txtScheduledTime = (TextView) itemView.findViewById(R.id.txtScheduledTime);
            txtBookingDateTime = (TextView) itemView.findViewById(R.id.txtBookingDateTime);


        }
    }
}
