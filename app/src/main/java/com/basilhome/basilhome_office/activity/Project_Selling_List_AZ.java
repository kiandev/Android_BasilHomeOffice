package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Project_Selling_List_AZ extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView;
    LinearLayout nodata;
    String region;
    SearchView searchView;
    ArrayList<Project> os_version = new ArrayList<>();
    Project_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_project_selling_list_az);

        searchView = findViewById(R.id.project_selling_list_az_searchview);
        recyclerView = findViewById(R.id.project_selling_list_az_rv);
        nodata = findViewById(R.id.project_selling_list_az_nodata);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        region = "0";
        Intent i = getIntent();
        region = i.getStringExtra("region");
        Log.d(TAG, "onCreate: " + region);

        mAdapter = new Project_Adapter(os_version);

        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (region.equals("0")) {
                loadProject();
            } else {
                load_by_region();
            }
        }

//        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Project> filteredModelList = filter(os_version, query);
                mAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Project> filteredModelList = filter(os_version, newText);
                mAdapter.setFilter(filteredModelList);
                return true;
            }

        });
    }

    private ArrayList<Project> filter(ArrayList<Project> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Project> filteredModelList = new ArrayList<>();

        for (Project model : models) {

            final String text = model.getName().toLowerCase();
            if (text.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void loadProject() {
        String URL = HttpUrl.geturl + "project_selling_list_az.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject project = array.getJSONObject(i);
                                os_version.add(new Project(
                                        project.getString("project_id"),
                                        project.getString("name"),
                                        project.getString("region")
                                ));
                            }
                            recyclerView.setAdapter(mAdapter);
                            if (mAdapter.getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                nodata.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Project_Selling_List_AZ.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void load_by_region() {
        String URL = HttpUrl.geturl + "project_selling_list_az_by_region.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject project = array.getJSONObject(i);
                                os_version.add(new Project(
                                        project.getString("project_id"),
                                        project.getString("name"),
                                        project.getString("region")
                                ));
                            }
                            recyclerView.setAdapter(mAdapter);
                            if (mAdapter.getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                nodata.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Project_Selling_List_AZ.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("region", region);
                return params;
            }
        };
        Volley.newRequestQueue(Project_Selling_List_AZ.this).add(stringRequest);

    }

    public void project_selling_list_az_back_click(View v) {
        finish();
    }

    public void project_selling_list_az_cv_click (View v) {
        searchView.setIconified(false);
    }
}
