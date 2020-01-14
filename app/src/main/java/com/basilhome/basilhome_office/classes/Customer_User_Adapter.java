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
import com.basilhome.basilhome_office.activity.Advisor_Note_Customer_Plus;
import com.basilhome.basilhome_office.activity.Customer_Show_Info;

import java.util.ArrayList;
import java.util.List;


public class Customer_User_Adapter extends RecyclerView.Adapter<Customer_User_Adapter.ViewHolder> {

    Context context;


    private ArrayList<Customer> os_version;

    public Customer_User_Adapter(ArrayList<Customer> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Customer_User_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_customer, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Customer_User_Adapter.ViewHolder holder, int position)  {
        final Customer customer = os_version.get(position);
        holder.name.setText(customer.getName());
        holder.create.setText(customer.getCreate());
        holder.readed.setText(customer.getReaded());
        holder.tel.setText(customer.getTel());
        holder.is_read = String.valueOf(customer.getRead());
        if (customer.getRead() == 1){
            holder.img_read.setImageDrawable(context.getResources().getDrawable(R.drawable.is_read));
        } else {
            holder.img_read.setImageDrawable(context.getResources().getDrawable(R.drawable.not_read));
        }

//        holder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!InternetTest.ok(view.getContext())) {
//                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(view.getContext(), Advisor_Note_Customer_Plus.class);
//                    intent.putExtra("customer_id", customer.getCustomerId());
//                    intent.putExtra("customer_name", customer.getName());
//                    view.getContext().startActivity(intent);
//                }
//            }
//        });

        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Advisor_Customer_Edit.class);
                    intent.putExtra("customer_id", customer.getCustomerId());
                    intent.putExtra("customer_name", customer.getName());
                    intent.putExtra("grade", customer.getGrade());
                    view.getContext().startActivity(intent);
                }
                return false;
            }
        });

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetTest.ok(view.getContext())) {
                    Toast.makeText(view.getContext(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), Customer_Show_Info.class);
                    intent.putExtra("customer_id", customer.getCustomerId());
                    intent.putExtra("name", customer.getName());
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

    public void swapData(ArrayList<Customer> data) {
        os_version = data;
        notifyDataSetChanged();
    }

    public void wipeData() {
        if (os_version != null) {
            os_version.clear();
        }
        notifyDataSetChanged();
    }


    public void setFilter(ArrayList<Customer> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, tel, create, readed;
        CardView cardview;
        ImageView img_read;
        String is_read;

        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.shape_customer_name);
            tel = itemView.findViewById(R.id.shape_customer_tel);
            create = itemView.findViewById(R.id.shape_customer_create);
            readed = itemView.findViewById(R.id.shape_customer_readed);
            cardview = itemView.findViewById(R.id.shape_customer_card);
            img_read = itemView.findViewById(R.id.shape_customer_imgread);
        }
    }

}
