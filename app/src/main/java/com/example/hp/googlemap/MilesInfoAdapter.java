package com.example.hp.googlemap;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MilesInfoAdapter extends RecyclerView.Adapter<MilesInfoAdapter.GatePassViewHolder> {
    ArrayList<Vehicle> vehicleList;
    Context context;
    String membershipStatus;

    int selected_position = 0;

    public MilesInfoAdapter(ArrayList<Vehicle> vehicles, Context context, String membershipStatus) {
        this.vehicleList = vehicles;
        this.context = context;
        this.membershipStatus = membershipStatus;
    }

    @Override
    public MilesInfoAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amount_display, parent, false);
        MilesInfoAdapter.GatePassViewHolder gatePassViewHolder = new MilesInfoAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(final MilesInfoAdapter.GatePassViewHolder holder, final int position) {

        Vehicle vehicle = vehicleList.get(position);
        holder.amountTxt.setText(vehicle.getAmount());
        holder.amountId = vehicle.getAmountId();

        if (position == selected_position) {
            // Selected Color
            holder.amountTxt.setBackgroundColor(Color.WHITE);
            holder.amountTxt.setTextColor(Color.BLACK);

            double total_amount;
            if (membershipStatus.equals("ON")) {
                total_amount = Integer.parseInt(vehicle.getAmount()) * 1.65 + 1;
            } else {
                total_amount = Integer.parseInt(vehicle.getAmount()) * 1.65 + 3;
            }

            Toast.makeText(context, "You will be charged "+ "$" + total_amount , Toast.LENGTH_LONG).show();

        } else {
            // Defult Color
            holder.amountTxt.setBackgroundColor(Color.BLACK);
            holder.amountTxt.setTextColor(Color.WHITE);
        }
        holder.amountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public String getSelectedData() {
        return vehicleList.get(selected_position).getAmount();
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView amountTxt;
        String amountId;
        ImageView imageView;
        ArrayList<Vehicle> vehicleList;
        Context context;

        public GatePassViewHolder(View itemView, final Context context, final ArrayList<Vehicle> vehicleList) {
            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
            amountTxt = (TextView) itemView.findViewById(R.id.amountTxt);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            final boolean[] iscolor = {true};
        }
    }
}
