package com.example.hp.googlemap.rider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hp.googlemap.R;
import com.example.hp.googlemap.Vehicle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardDetailsAdapter extends RecyclerView.Adapter<CardDetailsAdapter.GatePassViewHolder>
{
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;
    ImageView imageView;
    int position;

    public CardDetailsAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }


    @Override
    public CardDetailsAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_display, parent, false);
        CardDetailsAdapter.GatePassViewHolder gatePassViewHolder = new CardDetailsAdapter.GatePassViewHolder(view, context, vehicleList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(CardDetailsAdapter.GatePassViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get(position);
       // holder.imageView.setText(vehicle.getAmount());
        //holder.amountId = (vehicle.getAmountId());
        //imageView = (ImageView) findViewById(R.id.imageView);*/
        Picasso.with(context).load(vehicle.getCardTypeImage()).into(imageView);
    }

    @Override
    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
       // ImageView imageView;
       // String amountId;
        ArrayList<Vehicle> vehicleList;

        Context context;

        public GatePassViewHolder(View itemView, final Context context, final ArrayList<Vehicle> vehicleList) {

            super(itemView);
            this.context = context;
            this.vehicleList = vehicleList;
           imageView = (ImageView) itemView.findViewById(R.id.imageView);
           // Vehicle vehicle = vehicleList.get(position);
            // holder.imageView.setText(vehicle.getAmount());
            //holder.amountId = (vehicle.getAmountId());
            //imageView = (ImageView) findViewById(R.id.imageView);
            //Picasso.with(context).load(vehicle.getCardTypeImage()).into(imageView);
        }
    }
}
