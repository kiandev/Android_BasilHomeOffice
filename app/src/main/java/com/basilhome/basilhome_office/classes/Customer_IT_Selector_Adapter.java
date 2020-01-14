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
import com.basilhome.basilhome_office.activity.Advisor_Customer_Edit;
import com.basilhome.basilhome_office.activity.Customer_Change_Advisor;
import com.basilhome.basilhome_office.activity.Customer_Edit;
import com.basilhome.basilhome_office.activity.Customer_IT_Selector;

import java.util.ArrayList;


public class Customer_IT_Selector_Adapter extends RecyclerView.Adapter<Customer_IT_Selector_Adapter.ViewHolder> {

    Context context;
    private ArrayList<Customer_it> os_version;

    public Customer_IT_Selector_Adapter(ArrayList<Customer_it> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Customer_IT_Selector_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_customer_it_selector, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Customer_IT_Selector_Adapter.ViewHolder holder, int position)  {
        final Customer_it customer = os_version.get(position);
        holder.name.setText(customer.getName());
        holder.advisor.setText("کارشناس مربوطه : " + customer.getAdvisor());
        holder.is_read = String.valueOf(customer.getRead());
        if (customer.getRead() == 1){
            holder.img_read.setImageDrawable(context.getResources().getDrawable(R.drawable.is_read));
        } else {
            holder.img_read.setImageDrawable(context.getResources().getDrawable(R.drawable.not_read));
        }


        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Customer_Change_Advisor.class);
                    intent.putExtra("customer_id", customer.getCustomerId());
                    intent.putExtra("name", customer.getName());
                    intent.putExtra("tel", customer.getTel());
                    intent.putExtra("advisor", customer.getAdvisor());
                    view.getContext().startActivity(intent);
                }
            }
        });

        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Customer_Edit.class);
                    intent.putExtra("customer_id", customer.getCustomerId());
                    intent.putExtra("name", customer.getName());
                    intent.putExtra("tel", customer.getTel());
                    intent.putExtra("advisor", customer.getAdvisor());
                    view.getContext().startActivity(intent);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }

    public void swapData(ArrayList<Customer_it> data) {
        os_version = data;
        notifyDataSetChanged();
    }

    public void wipeData() {
        if (os_version != null) {
            os_version.clear();
        }
        notifyDataSetChanged();
    }
    public void setFilter(ArrayList<Customer_it> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView name, advisor;
        CardView cardview;
        ImageView img_read;
        String is_read;

        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.shape_customer_it_selector_name);
            advisor = itemView.findViewById(R.id.shape_customer_it_selector_advisor);
            cardview = itemView.findViewById(R.id.shape_customer_it_selector_card);
            img_read = itemView.findViewById(R.id.shape_customer_it_selector_read);

        }
    }

}
