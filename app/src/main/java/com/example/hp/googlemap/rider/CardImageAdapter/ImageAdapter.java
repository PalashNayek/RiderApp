package com.example.hp.googlemap.rider.CardImageAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.googlemap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    ArrayList<ModalAdapter> imageCardArrayList = new ArrayList();
    Context context;

    public ImageAdapter(ArrayList<ModalAdapter> imageCardArrayList, Context context) {
        this.imageCardArrayList = imageCardArrayList;
        this.context = context;
    }

    @Override

    public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_order_list, parent, false);
        ImageAdapter.ImageViewHolder gatePassViewHolder = new ImageAdapter.ImageViewHolder(view, context, imageCardArrayList);
        return gatePassViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position) {

       // ModalAdapter vendor = imageCardArrayList.get(position);
        //holder.imageView.setText(vendor.getCard_details());
        //holder.imageView.getImageMatrix()
        Picasso.with(context).load(imageCardArrayList.get(position).getCard_details()).into(holder.imageView);
        holder.card_num.setText(imageCardArrayList.get(position).getCard_no());

    }

    @Override

    public int getItemCount() {
        int vehicleSize = imageCardArrayList.size();
        return vehicleSize;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView card_num;
        ArrayList<ModalAdapter> imageCardArrayList;
        Context context;

        public ImageViewHolder(View itemView, Context context, ArrayList<ModalAdapter> imageCardArrayList) {

            super(itemView);
            this.context = context;
            this.imageCardArrayList = imageCardArrayList;
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            card_num = (TextView) itemView.findViewById(R.id.card_num);

            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ModalAdapter vendorList = imageCardArrayList.get(position);
            itemView.setOnClickListener(this);
            Intent intent = new Intent(this.context, GoToDestinationGoogleMap.class);
            Toast.makeText(context, "You are Click...", Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("row_id", vendorList.getOrder_id());
            intent.putExtra("item_name", vendorList.getRest_name());
            intent.putExtra("item_category", vendorList.getRest_add());
            this.context.startActivity(intent);
        }*/
    }
}

