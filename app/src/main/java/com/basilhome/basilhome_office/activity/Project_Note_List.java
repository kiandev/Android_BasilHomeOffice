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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.Customer_Note;
import com.basilhome.basilhome_office.classes.Customer_Note_Adapter;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;
import com.basilhome.basilhome_office.classes.Project;
import com.basilhome.basilhome_office.classes.Project_Note;
import com.basilhome.basilhome_office.classes.Project_Note_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Project_Note_List extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView;
    LinearLayout nodata;
    TextView project_name;
    String project_id;
    SearchView searchView;
    ArrayList<Project_Note> os_version = new ArrayList<>();
    Project_Note_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_project_note_list);

        project_name = findViewById(R.id.project_note_list_projectname);
        searchView = findViewById(R.id.project_note_list_searchview);
        recyclerView = findViewById(R.id.project_note_list_rv);
        nodata = findViewById(R.id.project_note_list_nodata);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Project_Note_Adapter(os_version);

        Intent intent = getIntent();
        project_name.setText(intent.getStringExtra("project_name"));
        project_id= intent.getStringExtra("project_id");

        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loadNote();
        }

//        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Project_Note> filteredModelList = filter(os_version, query);
                mAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Project_Note> filteredModelList = filter(os_version, newText);
                mAdapter.setFilter(filteredModelList);
                return true;
            }

        });
    }

    private ArrayList<Project_Note> filter(ArrayList<Project_Note> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Project_Note> filteredModelList = new ArrayList<>();

        for (Project_Note model : models) {

            final String text = model.getNote().toLowerCase();
            final String text2 = model.getTime().toLowerCase();
            final String text3 = model.getAdvisor().toLowerCase();
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

    private void loadNote() {
        String URL = HttpUrl.geturl + "project_note_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject project_note = array.getJSONObject(i);
                                os_version.add(new Project_Note(
                                        project_note.getString("advisor"),
                                        project_note.getString("note"),
                                        project_note.getString("time")
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
                        Toast.makeText(Project_Note_List.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("project_id", project_id);
                Log.d(TAG, "getParams: " + project_id);
                return params;
            }
        };
        Volley.newRequestQueue(Project_Note_List.this).add(stringRequest);

    }

    public void project_note_list_back_click(View v) {
        finish();
    }

    public void project_note_list_cv_click(View v) {
        searchView.setIconified(false);
    }
}
