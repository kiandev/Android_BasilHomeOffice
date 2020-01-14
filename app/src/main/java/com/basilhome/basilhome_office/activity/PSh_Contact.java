package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class PSh_Contact extends AppCompatActivity {

    TextView txttel, txtmobile, txtsite, txtemail;
    String project_id;
    String tel, mobile, site, email;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psh_contact);
        txttel = findViewById(R.id.psh_contact_teltxt);
        txtmobile = findViewById(R.id.psh_contact_mobiletxt);
        txtsite = findViewById(R.id.psh_contact_sitetxt);
        txtemail = findViewById(R.id.psh_contact_emailtxt);

        Intent i = getIntent();
        project_id = i.getStringExtra("project_id");
        load();
    }

    public void load(){
        String URL = HttpUrl.geturl + "get_project_contact.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                tel = jsonobject.getString("tel");
                                mobile = jsonobject.getString("mobile");
                                site = jsonobject.getString("site");
                                email = jsonobject.getString("email");

                                txttel.setText(jsonobject.getString("tel"));
                                txtmobile.setText(jsonobject.getString("mobile"));
                                txtsite.setText(jsonobject.getString("site"));
                                txtemail.setText(jsonobject.getString("email"));
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
        Volley.newRequestQueue(PSh_Contact.this).add(stringRequest);
    }

    public void psh_contact_btntelclick (View v){
        if (tel != null){
            if (tel.equals("")) {
                Toast.makeText(PSh_Contact.this, "متاسفانه اطلاعاتی موجود نیست", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tel));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(PSh_Contact.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void psh_contact_btnmobileclick (View v){
        if (mobile != null){
            if (mobile.equals("")) {
                Toast.makeText(PSh_Contact.this, "متاسفانه اطلاعاتی موجود نیست", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobile));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(PSh_Contact.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void psh_contact_btnsiteclick (View v){
        if (site != null){
            try{
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(site));
                startActivity(intent);
            } catch (Exception e){
                Toast.makeText(this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void psh_contact_btnemailclick (View v){
        if (email != null){
            if (email.equals("")) {
                Toast.makeText(PSh_Contact.this, "متاسفانه اطلاعاتی موجود نیست", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "پیام");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(PSh_Contact.this, "متاسفانه اپلیکیشن مناسب جهت برقراری ارسال ایمیل یافت نشد", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }
}
