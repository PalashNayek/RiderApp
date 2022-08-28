package com.example.hp.googlemap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SchedulTripAdapter extends RecyclerView.Adapter<SchedulTripAdapter.GatePassViewHolder>
{
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Context context;

    public SchedulTripAdapter(ArrayList<Vehicle> vehicles, Context context) {
        this.vehicleList = vehicles;
        this.context = context;
    }

    @Override

    public SchedulTripAdapter.GatePassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.schedul_trip_details, parent, false );
        SchedulTripAdapter.GatePassViewHolder gatePassViewHolder = new SchedulTripAdapter.GatePassViewHolder( view, context, vehicleList );
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(SchedulTripAdapter.GatePassViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get( position );
        holder.pickup_address_schedul.setText( vehicle.getPickup_address_schedul() );
        holder.drop_address_schedul.setText( vehicle.getDrop_address_schedul() );
        holder.scheduled_dt.setText( vehicle.getScheduled_dt() );
        holder.scheduled_time.setText( vehicle.getScheduled_time() );
        holder.booking_time.setText( vehicle.getBooking_time() );


    }

    @Override

    public int getItemCount() {
        int vehicleSize = vehicleList.size();
        return vehicleSize;
    }

    public class GatePassViewHolder extends RecyclerView.ViewHolder {
        TextView pickup_address_schedul, drop_address_schedul, scheduled_dt,scheduled_time,booking_time;
        ArrayList<Vehicle> vehicleList;

        Context context;

        public GatePassViewHolder(View itemView, Context context, ArrayList<Vehicle> vehicleList) {

            super( itemView );
            this.context = context;
            this.vehicleList = vehicleList;
            pickup_address_schedul = (TextView) itemView.findViewById( R.id.pickup_address_schedul );
            drop_address_schedul = (TextView) itemView.findViewById( R.id.drop_address_schedul );
            scheduled_dt = (TextView) itemView.findViewById( R.id.scheduled_dt );
            scheduled_time = (TextView) itemView.findViewById( R.id.scheduled_time );
            booking_time = (TextView) itemView.findViewById( R.id.booking_time );




          /*  image_btn_modify.setOnClickListener( this );
            image_btn_delete.setOnClickListener( this );*/

        }

       /* public void onClick(View v) {
            int position = getAdapterPosition();
            Vehicle vehicle = vehicleList.get( position );

            switch (v.getId()) {
                case R.id.image_btn_modify:
                    Intent modifyIntent = new Intent( this.context, ModifyActivity.class );
                    modifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    modifyIntent.putExtra( "userVehicleDtlId", vehicle.getUserVehDtlId() );
                    this.context.startActivity( modifyIntent );
                    break;

                case R.id.image_btn_delete:
                    Intent deleteIntent = new Intent( this.context, DeleteActivity.class );
                    deleteIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    deleteIntent.putExtra( "userVehDeletetlId", vehicle.getUserVehDtlId() );
                    this.context.startActivity( deleteIntent );
                    break;
            }
        }*/
    }
}
