package com.basilhome.basilhome_office.classes;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.activity.Project_Show_Info;

import java.util.ArrayList;


public class Project_Wit_Prepared_Adapter extends RecyclerView.Adapter<Project_Wit_Prepared_Adapter.ViewHolder> {


    private ArrayList<Project_With_Prepared> os_version;

    public Project_Wit_Prepared_Adapter(ArrayList<Project_With_Prepared> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Project_Wit_Prepared_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_project_with_prepared, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Project_Wit_Prepared_Adapter.ViewHolder holder, int position)  {
        final Project_With_Prepared project_With_Prepared = os_version.get(position);
        holder.name.setText(project_With_Prepared.getName());
        holder.region.setText(project_With_Prepared.getRegion());
        holder.id = String.valueOf(project_With_Prepared.getProject_id());
        holder.get_prepared = String.valueOf(project_With_Prepared.getPrepared());
        if (holder.get_prepared.equals("1")){
            holder.prepared.setText("در حال ساخت");

        } else if (holder.get_prepared.equals("2")){
            holder.prepared.setText("آماده تحویل");
        } else if (holder.get_prepared.equals("3")){
            holder.prepared.setText("دست دوم");
        }
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Project_Show_Info.class);
                    intent.putExtra("id", holder.id);
                    intent.putExtra("name", project_With_Prepared.getName());
                    intent.putExtra("region", project_With_Prepared.getRegion());
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Project_With_Prepared> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id, get_prepared;
        TextView name, region, prepared;
        CardView cardview;

        public ViewHolder(View view) {
            super(view);

            name = itemView.findViewById(R.id.shape_project_name);
            region = itemView.findViewById(R.id.shape_project_region);
            cardview = itemView.findViewById(R.id.shape_project_card);
            prepared = itemView.findViewById(R.id.shape_project_prepared);
        }
    }

}
