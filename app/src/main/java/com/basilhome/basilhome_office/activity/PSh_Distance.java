package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PSh_Distance extends AppCompatActivity {

    TextView metro, metrobus, aerodrome, mall, highway;
    String project_id;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psh_distance);
        metro = findViewById(R.id.psh_distance_metro);
        metrobus = findViewById(R.id.psh_distance_metrobus);
        aerodrome = findViewById(R.id.psh_distance_aerodrome);
        mall = findViewById(R.id.psh_distance_mall);
        highway = findViewById(R.id.psh_distance_highway);

        Intent i = getIntent();
        project_id = i.getStringExtra("project_id");
        load();
    }

    public void load(){
        String URL = HttpUrl.geturl + "get_project_distance.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                metro.setText(jsonobject.getString("metro"));
                                metrobus.setText(jsonobject.getString("metrobus"));
                                aerodrome.setText(jsonobject.getString("aerodrome"));
                                mall.setText(jsonobject.getString("mall"));
                                highway.setText(jsonobject.getString("highway"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("project_id", project_id);
                return params;
            }
        };
        Volley.newRequestQueue(PSh_Distance.this).add(stringRequest);
    }
}
