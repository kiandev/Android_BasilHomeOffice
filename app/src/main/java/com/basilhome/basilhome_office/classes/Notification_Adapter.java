package com.basilhome.basilhome_office.classes;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.activity.Customer_Show_Info;

import java.util.ArrayList;


public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.ViewHolder> {

    Context context;
    private ArrayList<Notification> os_version;

    public Notification_Adapter(ArrayList<Notification> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Notification_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_notification, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Notification_Adapter.ViewHolder holder, int position)  {
        final Notification notification = os_version.get(position);
        holder.title.setText(notification.getTitle());
        holder.text.setText(notification.getText());
        holder.time.setText(notification.getTime());
    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Notification> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, text, time;

        public ViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.shape_notification_title);
            text = itemView.findViewById(R.id.shape_notification_text);
            time = itemView.findViewById(R.id.shape_notification_time);

        }
    }

}
