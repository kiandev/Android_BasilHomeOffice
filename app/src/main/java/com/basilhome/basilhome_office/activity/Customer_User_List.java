package com.basilhome.basilhome_office.activity;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.Customer;
import com.basilhome.basilhome_office.classes.Customer_User_Adapter;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;
import com.basilhome.basilhome_office.classes.User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customer_User_List extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView;
    LinearLayout nodata;
    SearchView searchView;
    TextView txtcount;
    ArrayList<Customer> os_version = new ArrayList<>();
    Customer_User_Adapter mAdapter;


    public RecyclerView.LayoutManager mLayoutManager;
    Parcelable state;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_user_list);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                mAdapter.wipeData();
                loadCustomer();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        txtcount = findViewById(R.id.customer_list_txtcount);
        searchView = findViewById(R.id.customer_user_list_searchview);
        recyclerView = findViewById(R.id.customer_user_list_rv);
        nodata = findViewById(R.id.customer_user_list_nodata);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new Customer_User_Adapter(os_version);

        loadCustomer();

//        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Customer> filteredModelList = filter(os_version, query);
                mAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                int visibleItemCount = mAdapter.getItemCount();
                Log.d(TAG, "visibleItemCount: " + visibleItemCount);
                txtcount.setText(String.valueOf(visibleItemCount));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Customer> filteredModelList = filter(os_version, newText);
                mAdapter.setFilter(filteredModelList);
                int visibleItemCount = mAdapter.getItemCount();
                Log.d(TAG, "visibleItemCount: " + visibleItemCount);
                txtcount.setText(String.valueOf(visibleItemCount));
                return true;
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = mLayoutManager.onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
//            mAdapter.wipeData();
            if (state != null) {
                mLayoutManager.onRestoreInstanceState(state);
            }

        }
        super.onResume();
    }

    private ArrayList<Customer> filter(ArrayList<Customer> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Customer> filteredModelList = new ArrayList<>();

        for (Customer model : models) {

            final String text = model.getName().toLowerCase();
            final String text3 = model.getCreate().toLowerCase();
            final String text4 = model.getReaded().toLowerCase();
            final String text5 = model.getTel().toLowerCase();
            if (text.contains(search_txt)) {
                filteredModelList.add(model);
            } else if (text3.contains(search_txt)){
                filteredModelList.add(model);
            } else if (text4.contains(search_txt)){
                filteredModelList.add(model);
            } else if (text5.contains(search_txt)){
                filteredModelList.add(model);
            }

        }
        return filteredModelList;
    }

    private void loadCustomer() {
        String URL = HttpUrl.geturl + "customer_user_list_2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject customer = array.getJSONObject(i);
                                os_version.add(new Customer(
                                        customer.getString("customer_id"),
                                        customer.getString("name"),
                                        customer.getString("grade"),
                                        customer.getString("create_time"),
                                        customer.getString("read_time"),
                                        customer.getString("tel"),
                                        customer.getInt("is_read")
                                ));
                            }
                            recyclerView.setAdapter(mAdapter);
                            if (mAdapter.getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                nodata.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.GONE);
                                int visibleItemCount = mAdapter.getItemCount();
                                Log.d(TAG, "visibleItemCount: " + visibleItemCount);
                                txtcount.setText(String.valueOf(visibleItemCount));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Customer_User_List.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String user_fromPM = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                params.put("advisor", user_fromPM);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void customer_user_list_back_click(View v) {
        finish();
    }

    public void customer_user_list_cv_click(View v) {
        searchView.setIconified(false);
    }

    @Override
    public void onRefresh() {
    }
}
