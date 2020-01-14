package com.basilhome.basilhome_office.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.basilhome.basilhome_office.classes.Customer_Full;
import com.basilhome.basilhome_office.classes.Customer_IT_List_Adapter;
import com.basilhome.basilhome_office.classes.Customer_IT_Selector_Adapter;
import com.basilhome.basilhome_office.classes.Customer_it;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Customer_IT_List extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView;
    LinearLayout nodata;
    SearchView searchView;
    TextView txtcount;
    ArrayList<Customer_Full> os_version = new ArrayList<>();
    Customer_IT_List_Adapter mAdapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_it_list);

        searchView = findViewById(R.id.customer_it_list_searchview);
        nodata = findViewById(R.id.customer_it_list_nodata);
        txtcount = findViewById(R.id.customer_it_list_txtcount);
        recyclerView = findViewById(R.id.customer_it_list_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());


        mAdapter = new Customer_IT_List_Adapter(os_version);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Customer_Full> filteredModelList = filter(os_version, query);
                mAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                int visibleItemCount = mAdapter.getItemCount();
                Log.d(TAG, "visibleItemCount: " + visibleItemCount);
                txtcount.setText(String.valueOf(visibleItemCount));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Customer_Full> filteredModelList = filter(os_version, newText);
                mAdapter.setFilter(filteredModelList);
                int visibleItemCount = mAdapter.getItemCount();
                Log.d(TAG, "visibleItemCount: " + visibleItemCount);
                txtcount.setText(String.valueOf(visibleItemCount));
                return true;
            }

        });
    }

    @Override
    protected void onResume() {
        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mAdapter.wipeData();
            loadCustomer();
        }
        super.onResume();
    }


    private ArrayList<Customer_Full> filter(ArrayList<Customer_Full> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Customer_Full> filteredModelList = new ArrayList<>();
        for (Customer_Full model : models) {

            final String text = model.getName().toLowerCase();
            final String text2 = model.getAdvisor().toLowerCase();
            final String text3 = model.getCreate().toLowerCase();
            final String text4 = model.getReaded().toLowerCase();
            final String text5 = model.getTel().toLowerCase();
            if (text.contains(search_txt)) {
                filteredModelList.add(model);
            } else if (text2.contains(search_txt)){
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
        String URL = HttpUrl.geturl + "customer_it_list_full.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject customer = array.getJSONObject(i);
                                os_version.add(new Customer_Full(
                                        customer.getString("customer_id"),
                                        customer.getString("name"),
                                        customer.getString("grade"),
                                        customer.getString("advisor"),
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
                        Toast.makeText(Customer_IT_List.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void customer_it_list_back_click(View v) {
        finish();
    }

    public void customer_it_list_cv_click(View v) {
        searchView.setIconified(false);
    }
}
