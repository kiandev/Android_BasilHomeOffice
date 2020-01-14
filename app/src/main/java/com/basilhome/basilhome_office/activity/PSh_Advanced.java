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

public class PSh_Advanced extends AppCompatActivity {

    TextView born, measure, warming, residential, commercial, office, floor;
    String project_id;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psh_advanced);
        born = findViewById(R.id.psh_advanced_born);
        measure = findViewById(R.id.psh_advanced_measure);
        warming = findViewById(R.id.psh_advanced_warming);
        residential = findViewById(R.id.psh_advanced_residential);
        commercial = findViewById(R.id.psh_advanced_commercial);
        office = findViewById(R.id.psh_advanced_office);
        floor = findViewById(R.id.psh_advanced_floor);

        Intent i = getIntent();
        project_id = i.getStringExtra("project_id");
        load();
    }

    public void load(){
        String URL = HttpUrl.geturl + "get_project_advanced.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                born.setText(jsonobject.getString("born"));
                                measure.setText(jsonobject.getString("measure"));
                                warming.setText(jsonobject.getString("warming"));
                                residential.setText(jsonobject.getString("residential"));
                                commercial.setText(jsonobject.getString("commercial"));
                                office.setText(jsonobject.getString("office"));
                                floor.setText(jsonobject.getString("floor"));
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
        Volley.newRequestQueue(PSh_Advanced.this).add(stringRequest);
    }
}
