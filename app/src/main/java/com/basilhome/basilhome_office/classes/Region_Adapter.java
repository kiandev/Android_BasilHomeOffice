package com.basilhome.basilhome_office.classes;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.activity.Project_Selling_List_AZ;

import java.util.ArrayList;


public class Region_Adapter extends RecyclerView.Adapter<Region_Adapter.ViewHolder> {


    private ArrayList<Region> os_version;

    public Region_Adapter(ArrayList<Region> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Region_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_region, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(Region_Adapter.ViewHolder holder, int position)  {
        final Region region = os_version.get(position);
        holder.name.setText(region.getName());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Project_Selling_List_AZ.class);
                    intent.putExtra("region", region.getName());
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Region> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CardView cardview;

        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.shape_region_name);
            cardview = itemView.findViewById(R.id.shape_region_card);
        }
    }

}
