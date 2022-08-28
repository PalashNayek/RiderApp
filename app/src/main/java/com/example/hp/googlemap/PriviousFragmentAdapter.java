package com.example.hp.googlemap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PriviousFragmentAdapter extends RecyclerView.Adapter<PriviousFragmentAdapter.GatePassViewHolder> {
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;

    public PriviousFragmentAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }

    @Override

    public PriviousFragmentAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gate_pass_details_list, parent, false);
        PriviousFragmentAdapter.GatePassViewHolder gatePassViewHolder = new PriviousFragmentAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(PriviousFragmentAdapter.GatePassViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get(position);
        holder.driverName.setText(vehicle.getPreDriverName());
        holder.pickUpDateTime.setText(vehicle.getPreDateTime());
        holder.carTier.setText(vehicle.getPreCarTier());
        holder.txtBalance.setText(vehicle.getPreAmount());


    }

    @Override

    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView driverName, pickUpDateTime, carTier,txtBalance ;
        ArrayList<Vehicle> vehicleList;
        Context context;

        public GatePassViewHolder(View itemView, Context context, ArrayList<Vehicle> vehicleList) {

            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
            driverName = (TextView) itemView.findViewById(R.id.driverName);
            pickUpDateTime = (TextView) itemView.findViewById(R.id.pickUpDateTime);
            carTier = (TextView) itemView.findViewById(R.id.carTier);
            txtBalance = (TextView) itemView.findViewById(R.id.txtBalance);

        }
    }
}
