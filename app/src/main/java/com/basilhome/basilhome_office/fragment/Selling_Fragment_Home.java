package com.basilhome.basilhome_office.fragment;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.activity.MainActivity;
import com.basilhome.basilhome_office.classes.GlideApp;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.User_Info;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Selling_Fragment_Home extends Fragment {

    public static final String TAG = MainActivity.TAG;
    TextView name, title, admin_message;
    ImageView image, rating;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_selling_home, container, false);
        name = view.findViewById(R.id.frag_selling_home_name);
        title = view.findViewById(R.id.frag_selling_home_title);
        image = view.findViewById(R.id.frag_selling_home_image);
        progressBar = view.findViewById(R.id.frag_selling_home_pb);
        admin_message = view.findViewById(R.id.frag_selling_home_admin_message);
        rating = view.findViewById(R.id.frag_selling_home_rating);

        String getname = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString(User_Info.name, "");
        String getpart = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString(User_Info.part, "");
        String gettitle = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString(User_Info.title, "");
        String getimage = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString(User_Info.image, "");
        name.setText(getname);
        title.setText(gettitle + " بخش " + getpart);

        GlideApp
                .with(getActivity().getApplicationContext())
                .load(getimage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);
        load_admin_message();
        load_rating();
        return view;
    }

    private void load_admin_message() {
        String httpurl = HttpUrl.geturl + "get_admin_message_home.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String gettext = jsonObject.getString("text");
                                admin_message.setText(gettext);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void load_rating() {
        String httpurl = HttpUrl.geturl + "get_rating_advisor.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String getrating = jsonObject.getString("rating");
                                if (getrating.equals("1")){
                                    rating.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.stars_1));
                                }
                                else if (getrating.equals("2")){
                                    rating.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.stars_2));
                                }
                                else if (getrating.equals("3")){
                                    rating.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.stars_3));
                                }
                                else if (getrating.equals("4")){
                                    rating.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.stars_4));
                                }
                                else if (getrating.equals("5")){
                                    rating.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.stars_5));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String user_fromapp = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(User_Info.user,"");
                params.put("user", user_fromapp);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
