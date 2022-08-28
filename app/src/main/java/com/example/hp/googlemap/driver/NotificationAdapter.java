package com.example.hp.googlemap.driver;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.googlemap.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{
    //   private ArrayList<DriverListDto> driverList;
    private OnItemClickListener onItemClickListener;
    private Activity mactivity;
    public NotificationAdapter(/*ArrayList<DriverListDto> driverList,*/ Activity activity) {
        // this.driverList=driverList;
        this.mactivity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_rowpage_design_activity, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
      /*  viewHolder.tv_name.setText(driverList.get(position).driverName);
        viewHolder.tv_mid.setText(driverList.get(position).mdId);
        viewHolder.tv_email.setText(driverList.get(position).driverEmail);
        Picasso.with(mactivity).load(ApiClient.IMAGE_BASE_URL+driverList.get(position).driver_image).error(R.drawable.profilepic).
                into(viewHolder.img_driver);
        viewHolder.ll_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(driverList.get(position));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return 4;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        /*   TextView tv_mid,tv_name,tv_email;
           CircleImageView img_driver;
           LinearLayout ll_body;*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*tv_mid=itemView.findViewById(R.id.tv_mid);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_email=itemView.findViewById(R.id.tv_email);
            img_driver=itemView.findViewById(R.id.img_driver);
            ll_body=itemView.findViewById(R.id.ll_body);*/
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
