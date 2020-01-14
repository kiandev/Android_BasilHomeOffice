package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class Project_Show_Info extends AppCompatActivity {

    TextView name, region, prepared;
    String project_name, project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_project_show_info);

        name = findViewById(R.id.project_show_info_name);
        region = findViewById(R.id.project_show_info_region);
        prepared = findViewById(R.id.project_show_info_prepared);

        Intent i = getIntent();
        name.setText(i.getStringExtra("name"));
        project_name = i.getStringExtra("name");
        region.setText(i.getStringExtra("region"));
        project_id = i.getStringExtra("id");
        load();

    }
    public void load(){
        String URL = HttpUrl.geturl + "get_project_prepared.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String get_prepared = jsonobject.getString("prepared");
                                if (get_prepared.equals("1")){
                                    prepared.setText("در حال ساخت");
                                } else if (get_prepared.equals("2")){
                                    prepared.setText("آماده تحویل");
                                } else if (get_prepared.equals("3")){
                                    prepared.setText("دست دوم");
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
        Volley.newRequestQueue(Project_Show_Info.this).add(stringRequest);
    }

    public void project_show_info_base_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Base.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_advanced_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Advanced.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_contact_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Contact.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_distance_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Distance.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_facilities_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Facilities.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_price_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Price.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_sale_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, PSh_Sale.class);
        intent.putExtra("project_id",project_id);
        startActivity(intent);
    }

    public void project_show_info_note_click (View view){
        Intent intent = new Intent(Project_Show_Info.this, Project_Note_List.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_name",project_name);
        startActivity(intent);
    }

    public void project_show_info_location_click (View view){
        Toast.makeText(this, "به زودی ...", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(Project_Show_Info.this, PSh_Sale.class);
//        intent.putExtra("project_id",project_id);
//        startActivity(intent);
    }
}
