package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class PSh_Facilities extends AppCompatActivity {

    ImageView children, football, tenis, basketball,
            gym, inpool, outpool, guard, generator, sauna;

    TextView txtchildren, txtfootball, txttenis,
            txtbasketball, txtgym, txtinpool, txtoutpool,
            txtguard, txtgenerator, txtsauna;

    String project_id;
    public static final String TAG = MainActivity.TAG;

    ProgressBar pb;
    LinearLayout havedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psh_facilities);

        pb = findViewById(R.id.psh_facilities_pb);
        havedata = findViewById(R.id.psh_facilities_havedata);

        children = findViewById(R.id.psh_facilities_children);
        football = findViewById(R.id.psh_facilities_football);
        tenis = findViewById(R.id.psh_facilities_tenis);
        basketball = findViewById(R.id.psh_facilities_basketball);
        inpool = findViewById(R.id.psh_facilities_inpool);
        outpool = findViewById(R.id.psh_facilities_outpool);
        guard = findViewById(R.id.psh_facilities_guard);
        gym = findViewById(R.id.psh_facilities_gym);
        sauna = findViewById(R.id.psh_facilities_sauna);
        generator = findViewById(R.id.psh_facilities_generator);


        txtchildren = findViewById(R.id.psh_facilities_childrentext);
        txtfootball = findViewById(R.id.psh_facilities_footballtext);
        txttenis = findViewById(R.id.psh_facilities_tenistext);
        txtbasketball = findViewById(R.id.psh_facilities_basketballtext);
        txtgym = findViewById(R.id.psh_facilities_gymtext);
        txtinpool = findViewById(R.id.psh_facilities_inpooltext);
        txtoutpool = findViewById(R.id.psh_facilities_outpooltext);
        txtsauna = findViewById(R.id.psh_facilities_saunatext);
        txtguard = findViewById(R.id.psh_facilities_guardtext);
        txtgenerator = findViewById(R.id.psh_facilities_generatortext);

        Intent i = getIntent();
        project_id = i.getStringExtra("project_id");
        load();
    }

    public void load(){
        String URL = HttpUrl.geturl + "get_project_facilities.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                pb.setVisibility(View.GONE);
                                havedata.setVisibility(View.VISIBLE);
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                Log.d(TAG, "onResponse: " + jsonobject);
                                if (jsonobject.getString("children").equals("0")){
                                    children.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtchildren.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("football").equals("0")){
                                    football.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtfootball.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("tenis").equals("0")){
                                    tenis.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txttenis.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("basketball").equals("0")){
                                    basketball.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtbasketball.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("gym").equals("0")){
                                    gym.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtgym.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("in_pool_icon").equals("0")){
                                    inpool.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtinpool.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("out_pool_icon").equals("0")){
                                    outpool.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtoutpool.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("sauna").equals("0")){
                                    sauna.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtsauna.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("guard").equals("0")){
                                    guard.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtguard.setTextColor(getResources().getColor(R.color.colorGray300));
                                }
                                if (jsonobject.getString("generator").equals("0")){
                                    generator.setColorFilter(new ColorMatrixColorFilter(matrix));
                                    txtgenerator.setTextColor(getResources().getColor(R.color.colorGray300));
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
        Volley.newRequestQueue(PSh_Facilities.this).add(stringRequest);
    }
}
