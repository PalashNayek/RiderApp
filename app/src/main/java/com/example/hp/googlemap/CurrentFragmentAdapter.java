package com.example.hp.googlemap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrentFragmentAdapter extends RecyclerView.Adapter<CurrentFragmentAdapter.GatePassViewHolder> {
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;

    public CurrentFragmentAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }

    @Override

    public CurrentFragmentAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_trip_history, parent, false);
        CurrentFragmentAdapter.GatePassViewHolder gatePassViewHolder = new CurrentFragmentAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(CurrentFragmentAdapter.GatePassViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get(position);
        holder.txtPickUpAddress.setText(vehicle.getCurrentHistoryPickUpAddress());
        holder.txtDropAddress.setText(vehicle.getCurrentHistoryDropAddress());
        holder.txtDriverName.setText(vehicle.getCurrentHistoryDriverName());
        holder.txtCarTier.setText(vehicle.getCurrentHistoryCarTier());
        holder.txtPickUpDateTime.setText(vehicle.getCurrentHistoryPickUpDateTime());
        holder.txtAmount.setText(vehicle.getCurrentHistoryFireAmount());
    }

    @Override

    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView txtPickUpAddress, txtDropAddress, txtDriverName, txtCarTier, txtPickUpDateTime, txtAmount;
        ArrayList<Vehicle> vehicleList;
        Context context;

        public GatePassViewHolder(View itemView, Context context, ArrayList<Vehicle> vehicleList) {

            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
            txtPickUpAddress = (TextView) itemView.findViewById(R.id.txtPickUpAddress);
            txtDropAddress = (TextView) itemView.findViewById(R.id.txtDropAddress);
            txtDriverName = (TextView) itemView.findViewById(R.id.txtDriverName);
            txtCarTier = (TextView) itemView.findViewById(R.id.txtCarTier);
            txtPickUpDateTime = (TextView) itemView.findViewById(R.id.txtPickUpDateTime);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);

        }
    }
}
