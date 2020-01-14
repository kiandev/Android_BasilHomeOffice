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


public class Customer_Admin_Adapter extends RecyclerView.Adapter<Customer_Admin_Adapter.ViewHolder> {

    Context context;
    private ArrayList<Customer_Full> os_version;

    public Customer_Admin_Adapter(ArrayList<Customer_Full> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Customer_Admin_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_customer_it_selector, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Customer_Admin_Adapter.ViewHolder holder, int position)  {
        final Customer_Full customer = os_version.get(position);
        holder.name.setText(customer.getName());
        holder.advisor.setText("کارشناس مربوطه : " + customer.getAdvisor());
//        holder.txtcreate.setText(customer.getCreate());
//        holder.txtreaded.setText(customer.getReaded());
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
                    Intent intent = new Intent(view.getContext(), Customer_Show_Info.class);
                    intent.putExtra("customer_id", customer.getCustomerId());
                    intent.putExtra("name", customer.getName());
                    intent.putExtra("advisor", customer.getAdvisor());
                    intent.putExtra("grade", customer.getGrade());
                    intent.putExtra("is_read", holder.is_read);
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Customer_Full> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView name, advisor, txtcreate, txtreaded;
        CardView cardview;
        ImageView img_read;
        String is_read;

        public ViewHolder(View view) {
            super(view);
//            name = itemView.findViewById(R.id.shape_customer_admin_name);
//            txtcreate = itemView.findViewById(R.id.shape_customer_admin_create);
//            txtreaded = itemView.findViewById(R.id.shape_customer_admin_readed);
//            advisor = itemView.findViewById(R.id.shape_customer_admin_advisor);
//            cardview = itemView.findViewById(R.id.shape_customer_admin_card);
//            img_read = itemView.findViewById(R.id.shape_customer_admin_read);

            name = itemView.findViewById(R.id.shape_customer_it_selector_name);
            advisor = itemView.findViewById(R.id.shape_customer_it_selector_advisor);
            cardview = itemView.findViewById(R.id.shape_customer_it_selector_card);
            img_read = itemView.findViewById(R.id.shape_customer_it_selector_read);

        }
    }

}
