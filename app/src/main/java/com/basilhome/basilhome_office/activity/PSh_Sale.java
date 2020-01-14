package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class PSh_Sale extends AppCompatActivity {

    TextView percent, month;
    String project_id;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psh_sale);
        percent = findViewById(R.id.psh_sale_percent);
        month = findViewById(R.id.psh_sale_month);

        Intent i = getIntent();
        project_id = i.getStringExtra("project_id");
        load();
    }

    public void load(){
        String URL = HttpUrl.geturl + "get_project_sale.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+ response);
                        if (response.equals("0")){
                            percent.setText("-");
                            month.setText("-");
                        } else{
                            try {
                                JSONArray jsonarray = new JSONArray(response);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    percent.setText(jsonobject.getString("percent") + " %");
                                    month.setText(jsonobject.getString("month") +  " ماه");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        Volley.newRequestQueue(PSh_Sale.this).add(stringRequest);
    }
}
