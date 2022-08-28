package com.example.hp.googlemap;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.hp.googlemap.riderdto.LocationDto;
import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    private ArrayList<LocationDto> locationDtos;
    private OnItemClickListener onItemClickListener;
    private Activity mactivity;
    public PlaceAdapter(ArrayList<LocationDto> locationDtos,Activity activity) {
        this.locationDtos=locationDtos;
        this.mactivity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_text, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        viewHolder.placeName.setText(locationDtos.get(position).description);
        viewHolder.ll_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(locationDtos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationDtos.size();
    }

    public void updateData(ArrayList<LocationDto> locationDtos) {
        this.locationDtos=locationDtos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;

        LinearLayout ll_body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeName=itemView.findViewById(R.id.placeName);
           // placeAddress=itemView.findViewById(R.id.placeAddress);

            ll_body=itemView.findViewById(R.id.ll_body);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(LocationDto locationDto);
    }
}
