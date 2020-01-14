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
import com.basilhome.basilhome_office.activity.Customer_Show_Info;

import java.util.ArrayList;


public class Customer_Note_Adapter extends RecyclerView.Adapter<Customer_Note_Adapter.ViewHolder> {


    private ArrayList<Customer_Note> os_version;

    public Customer_Note_Adapter(ArrayList<Customer_Note> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Customer_Note_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_customer_note, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Customer_Note_Adapter.ViewHolder holder, int position)  {
        final Customer_Note customer_note = os_version.get(position);
        holder.note.setText(customer_note.getNote());
        holder.time.setText(customer_note.getTime());
        holder.advisor.setText(customer_note.getAdvisor());


    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }

    public void wipeData() {
        if (os_version != null) {
            os_version.clear();
        }
        notifyDataSetChanged();
    }

    public void setFilter(ArrayList<Customer_Note> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView advisor, note, time;
        CardView cardview;

        public ViewHolder(View view) {
            super(view);
            advisor = itemView.findViewById(R.id.shape_customer_note_name);
            note = itemView.findViewById(R.id.shape_customer_note_note);
            time = itemView.findViewById(R.id.shape_customer_note_time);
            cardview = itemView.findViewById(R.id.shape_customer_note_card);
        }
    }

}
