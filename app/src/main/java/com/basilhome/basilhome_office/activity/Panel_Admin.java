package com.basilhome.basilhome_office.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.User_Info;
import com.basilhome.basilhome_office.fragment.Admin_Fragment_Advisor;
import com.basilhome.basilhome_office.fragment.Admin_Fragment_Customer;
import com.basilhome.basilhome_office.fragment.Admin_Fragment_Home;
import com.basilhome.basilhome_office.fragment.Selling_Fragment_Project;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Panel_Admin extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private int currentId;
    private Boolean exit = false;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_panel_admin);

        drawerLayout = findViewById(R.id.admin_drawer);

        bottomNavigationView = findViewById(R.id.admin_bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.admin_item_home);
        disableShiftMode(bottomNavigationView);

        fragmentManager = getSupportFragmentManager();
        fragment = new Admin_Fragment_Home();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, fragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (currentId == item.getItemId()) {
                    return false;
                }
                currentId = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.admin_item_home:
                        fragment = new Admin_Fragment_Home();
                        break;
                    case R.id.admin_item_customer:
                        fragment = new Admin_Fragment_Customer();
                        break;
                    case R.id.admin_item_advisor:
                        fragment = new Admin_Fragment_Advisor();
                        break;
                    case R.id.admin_item_project:
                        fragment = new Selling_Fragment_Project();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, fragment).commit();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                return true;
            }
        });

        NavigationView navigationView = findViewById(R.id.admin_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.admin_itemdrawer_basilhome:
                        startActivity(new Intent(Panel_Admin.this, About_Basil.class));
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        break;
                    case R.id.admin_itemdrawer_settings:
                        startActivity(new Intent(Panel_Admin.this, Admin_Settings.class));
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        break;
                }
                return false;
            }
        });

        String get_istokenid_create = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("tokenidwascreated","no");
        if (get_istokenid_create.equals("no")){
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
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("tokenidwascreated","yes").apply();
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
                        RequestQueue requestQueue = Volley.newRequestQueue(Panel_Admin.this);
                        requestQueue.add(stringRequest);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.admin_drawer);
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

    public void frag_admin_home_btndrawer_click (View view){
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    public void frag_admin_customer_btncustomerlist (View view){
        Intent intent = new Intent(Panel_Admin.this,Customer_IT_List.class);
        startActivity(intent);
    }

    public void frag_admin_advisor_report_click (View view){
        Intent i = new Intent(Panel_Admin.this,Report_Admin_List.class);
        startActivity(i);
    }

    public void frag_selling_customer_btnprojectlistaz (View view){
        Intent i = new Intent(Panel_Admin.this , Project_Selling_List_AZ.class);
        i.putExtra("region","0");
        startActivity(i);
    }

    public void frag_selling_customer_btnprojectlistregion (View view){
        Intent i = new Intent(Panel_Admin.this , Project_Selling_List_Region.class);
        i.putExtra("region","0");
        startActivity(i);
    }

    public void frag_selling_project_btnsearchbyprice (View view){
        Intent i = new Intent(Panel_Admin.this,Get_Num_Search_Price.class);
        startActivity(i);
    }

    public void frag_selling_project_btnplusnote (View view){
        Intent i = new Intent(Panel_Admin.this,Project_Selling_Selector.class);
        startActivity(i);
    }

    public void frag_admin_btnadminmessage (View view){
        Intent i = new Intent(Panel_Admin.this,Admin_Message_Plus.class);
        startActivity(i);
    }

    public void frag_admin_btnsendnotification (View view){
        Intent i = new Intent(Panel_Admin.this,Admin_Notification_Plus.class);
        startActivity(i);
    }


}
