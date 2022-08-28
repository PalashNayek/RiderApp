package com.example.hp.googlemap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MilesAmountAdapter extends RecyclerView.Adapter<MilesAmountAdapter.GatePassViewHolder> {
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;

    int selected_position=0;

    public MilesAmountAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }

    @Override

    public MilesAmountAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amount_display, parent, false);
        MilesAmountAdapter.GatePassViewHolder gatePassViewHolder = new MilesAmountAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(final MilesAmountAdapter.GatePassViewHolder holder, final int position) {

        Vehicle vehicle = vehicleList.get(position);
        holder.amountTxt.setText(vehicle.getAmount());
        holder.amountId = (vehicle.getAmountId());
        if(position==selected_position)
        {
            // Selected Color
            holder.amountTxt.setBackgroundColor(Color.WHITE);
            holder.amountTxt.setTextColor(Color.BLACK);

        }else
        {
            // Defult Color
            holder.amountTxt.setBackgroundColor(Color.BLACK);
            holder.amountTxt.setTextColor(Color.WHITE);
        }
        holder.amountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position=position;
                notifyDataSetChanged();
            }
        });
        // position==0
    }

    @Override
    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public String getSelectedData()
    {
        return vehicleList.get(selected_position).getAmount();
    }
    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView amountTxt;
        String amountId;
        ImageView imageView;
        // LinearLayout layout_gatepass_btn;
        ArrayList<Vehicle> vehicleList;

        Context context;

        public GatePassViewHolder(View itemView, final Context context, final ArrayList<Vehicle> vehicleList) {

            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
            amountTxt = (TextView) itemView.findViewById(R.id.amountTxt);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            // layout_gatepass_btn=(LinearLayout)itemView.findViewById(R.id.layout_gatepass_btn);
            //Picasso.with(context).load(vehicle.getCardTypeImage()).into(imageView);
            final boolean[] iscolor = {true};

        }
    }
}