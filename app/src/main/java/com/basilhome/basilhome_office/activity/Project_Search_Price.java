package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;
import com.basilhome.basilhome_office.classes.Project;
import com.basilhome.basilhome_office.classes.Project_Adapter;
import com.basilhome.basilhome_office.classes.Project_Wit_Prepared_Adapter;
import com.basilhome.basilhome_office.classes.Project_With_Prepared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project_Search_Price extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    SearchView searchView;
    RecyclerView recyclerView;
    LinearLayout project_nodata;
    String price, filter;
    ProgressBar pb;
    String vahed_kol = "همه واحد ها";
    String vahed_0 = "0 + 1";
    String vahed_1 = "1 + 1";
    String vahed_2 = "2 + 1";
    String vahed_3 = "3 + 1";
    String vahed_4 = "4 + 1";
    String vahed_5 = "5 + 1";
    ArrayList<Project_With_Prepared> os_version = new ArrayList<>();
    Project_Wit_Prepared_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_project_search_price);
        searchView = findViewById(R.id.project_selling_searchprice_searchview);

        mAdapter = new Project_Wit_Prepared_Adapter(os_version);

        Intent i = getIntent();
        price = i.getStringExtra("price");
        filter = i.getStringExtra("filter");

        pb = findViewById(R.id.project_searchprice_pb);
        recyclerView = findViewById(R.id.project_searchprice_rv);
        project_nodata = findViewById(R.id.project_searchprice_nodata);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (filter.equals(vahed_kol)){
                loadProject_all();
            } else{
                loadProject_filter();
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Project_With_Prepared> filteredModelList = filter(os_version, query);
                mAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Project_With_Prepared> filteredModelList = filter(os_version, newText);
                mAdapter.setFilter(filteredModelList);
                return true;
            }

        });
    }

    private ArrayList<Project_With_Prepared> filter(ArrayList<Project_With_Prepared> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Project_With_Prepared> filteredModelList = new ArrayList<>();

        for (Project_With_Prepared model : models) {
            String get_preparedof = null;
            if (model.getPrepared() == 1) {
                get_preparedof = "در حال ساخت";
            } else if (model.getPrepared() == 2){
                get_preparedof = "آماده تحویل";
            } else if (model.getPrepared() == 3){
                get_preparedof = "دست دوم";
            }
            final String text = model.getName().toLowerCase();
            final String text2 = model.getRegion().toLowerCase();
            final String text3 = get_preparedof.toLowerCase();
            if (text.contains(search_txt)) {
                filteredModelList.add(model);
            }
            else if (text2.contains(search_txt)){
                filteredModelList.add(model);
            }
            else if (text3.contains(search_txt)){
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void loadProject_all() {
        String URL = HttpUrl.geturl + "get_project_search_by_price_all.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pb.setVisibility(View.GONE);
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject project = array.getJSONObject(i);
                                os_version.add(new Project_With_Prepared(
                                        project.getString("project_id"),
                                        project.getString("name"),
                                        project.getString("region"),
                                        project.getInt("prepared")
                                ));
                            }
                            recyclerView.setAdapter(mAdapter);
                            if (mAdapter.getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                project_nodata.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                project_nodata.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(Project_Search_Price.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("price", price);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void loadProject_filter() {
        String URL = HttpUrl.geturl + "get_project_search_by_price.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pb.setVisibility(View.GONE);
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject project = array.getJSONObject(i);
                                os_version.add(new Project_With_Prepared(
                                        project.getString("project_id"),
                                        project.getString("name"),
                                        project.getString("region"),
                                        project.getInt("prepared")
                                ));
                            }
                            recyclerView.setAdapter(mAdapter);
                            if (mAdapter.getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                project_nodata.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                project_nodata.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(Project_Search_Price.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (filter.equals(vahed_0)){
                    params.put("filter_one", "min_10");
                    params.put("filter_two", "max_10");
                } else if (filter.equals(vahed_1)){
                    params.put("filter_one", "min_11");
                    params.put("filter_two", "max_11");
                }else if (filter.equals(vahed_2)){
                    params.put("filter_one", "min_21");
                    params.put("filter_two", "max_21");
                }else if (filter.equals(vahed_3)){
                    params.put("filter_one", "min_31");
                    params.put("filter_two", "max_31");
                }else if (filter.equals(vahed_4)){
                    params.put("filter_one", "min_41");
                    params.put("filter_two", "max_41");
                }else if (filter.equals(vahed_5)){
                    params.put("filter_one", "min_51");
                    params.put("filter_two", "max_51");
                }
                params.put("price", price);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void project_searchprice_backclick (View v){
        finish();
    }

    public void project_selling_searchprice_cv_click (View v) {
        searchView.setIconified(false);
    }

}
