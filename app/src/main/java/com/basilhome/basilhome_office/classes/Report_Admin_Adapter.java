package com.basilhome.basilhome_office.classes;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.activity.Customer_Show_Info;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class Report_Admin_Adapter extends RecyclerView.Adapter<Report_Admin_Adapter.ViewHolder> {


    private ArrayList<Report_Admin> os_version;
    Context context;

    public Report_Admin_Adapter(ArrayList<Report_Admin> arrayList) {
        os_version = arrayList;
    }

    @Override
    public Report_Admin_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_report, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Report_Admin_Adapter.ViewHolder holder, int position)  {
        final Report_Admin report = os_version.get(position);
//        GlideApp
//                .with(context)
//                .load(new GlideUrlWithQueryParameter(Report_Admin.getImg(), "source", "feed"))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        holder.image.setVisibility(View.VISIBLE);
//                        holder.pb.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.image.setVisibility(View.VISIBLE);
//                        holder.pb.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(holder.image);
        holder.advisor.setText(report.getAdvisor());
        holder.text.setText(report.getText());
        holder.date.setText(report.getDate());


    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Report_Admin> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView advisor, text, date;
        CardView cardview;
//        ImageView image;
//        ProgressBar pb;

        public ViewHolder(View view) {
            super(view);
            advisor = itemView.findViewById(R.id.shape_report_name);
            text = itemView.findViewById(R.id.shape_report_text);
            date = itemView.findViewById(R.id.shape_report_date);
//            image = itemView.findViewById(R.id.shape_report_image);
//            pb = itemView.findViewById(R.id.shape_report_pb);
            cardview = itemView.findViewById(R.id.shape_report_card);
        }
    }

}
