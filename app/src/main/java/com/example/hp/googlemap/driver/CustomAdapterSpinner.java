package com.example.hp.googlemap.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.googlemap.R;

import java.util.ArrayList;

public class CustomAdapterSpinner extends BaseAdapter {
    Context context;
    ArrayList<SpinSetGet> arrayList;

    public CustomAdapterSpinner(Context context, ArrayList <SpinSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v= LayoutInflater.from(context).inflate(R.layout.spinner_row,viewGroup,false);
        TextView textView=v.findViewById(R.id.tvRow);
        textView.setText(arrayList.get(i).getModel_name());
        return  v;
    }
}
