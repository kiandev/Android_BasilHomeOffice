package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.TextViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PSh_Price extends AppCompatActivity {

    TextView min0, max0, min1, max1, min2, max2, min3, max3, min4, max4, min5, max5;
    String project_id;
    LinearLayout layout0, layout1, layout2, layout3, layout4, layout5;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psh_price);
        min0 = findViewById(R.id.psh_price_min0);
        max0 = findViewById(R.id.psh_price_max0);
        min1 = findViewById(R.id.psh_price_min1);
        max1 = findViewById(R.id.psh_price_max1);
        min2 = findViewById(R.id.psh_price_min2);
        max2 = findViewById(R.id.psh_price_max2);
        min3 = findViewById(R.id.psh_price_min3);
        max3 = findViewById(R.id.psh_price_max3);
        min4 = findViewById(R.id.psh_price_min4);
        max4 = findViewById(R.id.psh_price_max4);
        min5 = findViewById(R.id.psh_price_min5);
        max5 = findViewById(R.id.psh_price_max5);

        layout0 = findViewById(R.id.psh_price_layout0);
        layout1 = findViewById(R.id.psh_price_layout1);
        layout2 = findViewById(R.id.psh_price_layout2);
        layout3 = findViewById(R.id.psh_price_layout3);
        layout4 = findViewById(R.id.psh_price_layout4);
        layout5 = findViewById(R.id.psh_price_layout5);

        TextViewUtils.separateGroups(min0);
        TextViewUtils.separateGroups(max0);
        TextViewUtils.separateGroups(min1);
        TextViewUtils.separateGroups(max1);
        TextViewUtils.separateGroups(min2);
        TextViewUtils.separateGroups(max2);
        TextViewUtils.separateGroups(min3);
        TextViewUtils.separateGroups(max3);
        TextViewUtils.separateGroups(min4);
        TextViewUtils.separateGroups(max4);
        TextViewUtils.separateGroups(min5);
        TextViewUtils.separateGroups(max5);

        Intent i = getIntent();
        project_id = i.getStringExtra("project_id");
        load();
    }

    public void load(){
        String URL = HttpUrl.geturl + "get_project_price.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                min0.setText(jsonobject.getString("min_10"));
                                max0.setText(jsonobject.getString("max_10"));
                                min1.setText(jsonobject.getString("min_11"));
                                max1.setText(jsonobject.getString("max_11"));
                                min2.setText(jsonobject.getString("min_21"));
                                max2.setText(jsonobject.getString("max_21"));
                                min3.setText(jsonobject.getString("min_31"));
                                max3.setText(jsonobject.getString("max_31"));
                                min4.setText(jsonobject.getString("min_41"));
                                max4.setText(jsonobject.getString("max_41"));
                                min5.setText(jsonobject.getString("min_51"));
                                max5.setText(jsonobject.getString("max_51"));
                                if (!min0.getText().toString().equals("0")){
                                    layout0.setVisibility(View.VISIBLE);
                                }
                                if (!min1.getText().toString().equals("0")){
                                    layout1.setVisibility(View.VISIBLE);
                                }
                                if (!min2.getText().toString().equals("0")){
                                    layout2.setVisibility(View.VISIBLE);
                                }
                                if (!min3.getText().toString().equals("0")){
                                    layout3.setVisibility(View.VISIBLE);
                                }
                                if (!min4.getText().toString().equals("0")){
                                    layout4.setVisibility(View.VISIBLE);
                                }
                                if (!min5.getText().toString().equals("0")){
                                    layout5.setVisibility(View.VISIBLE);
                                }
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
        Volley.newRequestQueue(PSh_Price.this).add(stringRequest);
    }

}
