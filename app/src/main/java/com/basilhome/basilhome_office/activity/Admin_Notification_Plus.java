package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;
import com.basilhome.basilhome_office.classes.User_Info;
import com.basilhome.basilhome_office.classes.fcm;

import java.util.HashMap;
import java.util.Map;

public class Admin_Notification_Plus extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    TextInputEditText txttitle, txttext;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_admin_notification_plus);

        txttitle = findViewById(R.id.admin_notification_plus_title);
        txttext = findViewById(R.id.admin_notification_plus_text);
        progressDialog = new ProgressDialog(Admin_Notification_Plus.this);

    }

    public void admin_notification_plus_back_click(View view) {
        finish();
    }

    public void admin_notification_plus_btnok_click(View view) {
        if (txttitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(Admin_Notification_Plus.this, "لطفا عنوان اطلاعیه را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else if (txttext.getText().toString().trim().isEmpty()){
            Toast.makeText(Admin_Notification_Plus.this, "لطفا متن اطلاعیه را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال ثبت و ارسال اطلاعیه ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "admin_notification_plus.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    fcm sendnotif = new fcm();
                                    sendnotif.sendNotification("/topics/general",txttitle.getText().toString(),txttext.getText().toString());
                                }catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                Toast.makeText(Admin_Notification_Plus.this, "اطلاعیه با موفقیت ثبت و ارسال گردید", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Admin_Notification_Plus.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("title", txttitle.getText().toString());
                        params.put("text", txttext.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Admin_Notification_Plus.this);
                requestQueue.add(stringRequest);
            }
        }
    }
}
