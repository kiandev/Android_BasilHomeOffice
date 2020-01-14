package com.basilhome.basilhome_office.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.basilhome.basilhome_office.classes.Report_Admin;
import com.basilhome.basilhome_office.classes.Report_Admin_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Report_Admin_List extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView;
    LinearLayout nodata;
    SearchView searchView;
    ArrayList<Report_Admin> os_version = new ArrayList<>();
    Report_Admin_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_report_admin_list);

        searchView = findViewById(R.id.report_admin_list_searchview);
        recyclerView = findViewById(R.id.report_admin_list_rv);
        nodata = findViewById(R.id.report_admin_list_nodata);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Report_Admin_Adapter(os_version);

        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loadReport();
        }

//        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Report_Admin> filteredModelList = filter(os_version, query);
                mAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Report_Admin> filteredModelList = filter(os_version, newText);
                mAdapter.setFilter(filteredModelList);
                return true;
            }

        });
    }

    private ArrayList<Report_Admin> filter(ArrayList<Report_Admin> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Report_Admin> filteredModelList = new ArrayList<>();

        for (Report_Admin model : models) {

            final String text = model.getAdvisor().toLowerCase();
            final String text2 = model.getText().toLowerCase();
            final String text3 = model.getDate().toLowerCase();
            if (text.contains(search_txt)) {
                filteredModelList.add(model);
            } else if (text2.contains(search_txt)) {
                filteredModelList.add(model);
            } else if (text3.contains(search_txt)) {
                filteredModelList.add(model);
            }

        }
        return filteredModelList;
    }

    private void loadReport() {
        String URL = HttpUrl.geturl + "report_admin_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject report_Admin = array.getJSONObject(i);
                                os_version.add(new Report_Admin(
                                        report_Admin.getString("text"),
                                        report_Admin.getString("advisor"),
                                        report_Admin.getString("date"),
                                        report_Admin.getString("img")
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
                        Toast.makeText(Report_Admin_List.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void report_admin_list_back_click(View v) {
        finish();
    }

    public void report_admin_list_cv_click(View v) {
        searchView.setIconified(false);
    }
}
