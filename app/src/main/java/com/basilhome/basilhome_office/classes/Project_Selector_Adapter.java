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
import com.basilhome.basilhome_office.activity.Advisor_Note_Project_Plus;
import com.basilhome.basilhome_office.activity.Project_Show_Info;

import java.util.ArrayList;


public class Project_Selector_Adapter extends RecyclerView.Adapter<Project_Selector_Adapter.ViewHolder> {


    private ArrayList<Project> os_version;

    public Project_Selector_Adapter(ArrayList<Project> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Project_Selector_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_project, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Project_Selector_Adapter.ViewHolder holder, int position)  {
        final Project project = os_version.get(position);
        holder.name.setText(project.getName());
        holder.region.setText(project.getRegion());
        holder.id = String.valueOf(project.getProject_id());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Advisor_Note_Project_Plus.class);
                    intent.putExtra("project_id", holder.id);
                    intent.putExtra("project_name", project.getName());
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Project> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView name, region;
        CardView cardview;

        public ViewHolder(View view) {
            super(view);

            name = itemView.findViewById(R.id.shape_project_name);
            region = itemView.findViewById(R.id.shape_project_region);
            cardview = itemView.findViewById(R.id.shape_project_card);
        }
    }

}
