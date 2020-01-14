package com.basilhome.basilhome_office.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.GlideApp;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.Tokenid_Info;
import com.basilhome.basilhome_office.classes.User_Info;
import com.basilhome.basilhome_office.fragment.Admin_Fragment_Advisor;
import com.basilhome.basilhome_office.fragment.Admin_Fragment_Customer;
import com.basilhome.basilhome_office.fragment.Admin_Fragment_Home;
import com.basilhome.basilhome_office.fragment.Selling_Fragment_Project;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Panel_IT extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private Boolean exit = false;
    TextView name;
    ImageView image;
    ProgressBar progressBar;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_panel_it);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    login_success();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        drawerLayout = findViewById(R.id.it_drawer);
        name = findViewById(R.id.it_panel_name);
        image = findViewById(R.id.it_panel_image);
        progressBar = findViewById(R.id.it_panel_pb);

        String get_masi_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.masi,"");
        String get_samira_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.samira,"");
        String get_amir_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.amir,"");
        String get_vahideh_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.vahideh,"");
        Log.d(TAG, "tokenid: " + get_masi_tokenid);
        Log.d(TAG, "tokenid: " + get_samira_tokenid);
        Log.d(TAG, "tokenid: " + get_amir_tokenid);
        Log.d(TAG, "tokenid: " + get_vahideh_tokenid);


        NavigationView navigationView = findViewById(R.id.it_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.it_itemdrawer_basilhome:
                        startActivity(new Intent(Panel_IT.this, About_Basil.class));
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        break;
                    case R.id.it_itemdrawer_settings:
                        startActivity(new Intent(Panel_IT.this, IT_Settings.class));
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        break;
                }
                return false;
            }
        });

        String getname = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.name, "");
        String getimage = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.image, "");
        name.setText(getname);

        GlideApp
                .with(getApplicationContext())
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
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);

        String get_istokenid_create = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("tokenidwascreated", "no");
        if (get_istokenid_create.equals("no")) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String httpurl = HttpUrl.geturl + "tokenid_plus.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                httpurl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: " + response);
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("tokenidwascreated", "yes").apply();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                }

                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                String get_advisor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                                String get_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.tokenid, "");
                                params.put("advisor", get_advisor);
                                params.put("tokenid", get_tokenid);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(Panel_IT.this);
                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.it_drawer);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            if (exit) {
                moveTaskToBack(true);
//                super.onBackPressed();
//                finish();
            } else {
                Toast.makeText(this, "برای خروج کلید بازگشت را مجدد فشار دهید !",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }

    public void it_panel_btndrawer_click(View view) {
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    public void it_panel_customer_plus_click(View view) {
        Intent intent = new Intent(Panel_IT.this,Customer_Plus.class);
        startActivity(intent);
    }

    public void it_panel_customer_list_click(View view) {
        Intent intent = new Intent(Panel_IT.this,Customer_IT_List.class);
        startActivity(intent);
    }

    public void it_panel_customer_edit_click(View view) {
        Intent intent = new Intent(Panel_IT.this,Customer_IT_Selector.class);
        startActivity(intent);
    }


    private void login_success() {
        String httpurl = HttpUrl.geturl + "login_success.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                String get_advisor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                String get_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.tokenid, "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", get_advisor);
                params.put("tokenid", get_tokenid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Panel_IT.this);
        requestQueue.add(stringRequest);
    }

}
