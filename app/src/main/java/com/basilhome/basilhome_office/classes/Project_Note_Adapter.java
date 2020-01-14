package com.basilhome.basilhome_office.classes;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basilhome.basilhome_office.R;

import java.util.ArrayList;


public class Project_Note_Adapter extends RecyclerView.Adapter<Project_Note_Adapter.ViewHolder> {


    private ArrayList<Project_Note> os_version;

    public Project_Note_Adapter(ArrayList<Project_Note> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Project_Note_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_project_note, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Project_Note_Adapter.ViewHolder holder, int position)  {
        final Project_Note project_note = os_version.get(position);
        holder.note.setText(project_note.getNote());
        holder.time.setText(project_note.getTime());
        holder.advisor.setText(project_note.getAdvisor());


    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Project_Note> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView advisor, note, time;

        public ViewHolder(View view) {
            super(view);
            advisor = itemView.findViewById(R.id.shape_project_note_name);
            note = itemView.findViewById(R.id.shape_project_note_note);
            time = itemView.findViewById(R.id.shape_project_note_time);
        }
    }

}
