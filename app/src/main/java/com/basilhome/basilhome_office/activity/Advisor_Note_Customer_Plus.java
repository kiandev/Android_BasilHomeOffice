package com.basilhome.basilhome_office.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Advisor_Note_Customer_Plus extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    TextInputEditText txtname, txtnote;
    ProgressDialog progressDialog;
    String get_advisor_user, get_advisor_name, get_customerid, get_customername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_advisor_note_customer_plus);

        txtname = findViewById(R.id.advisor_note_customer_plus_name);
        txtnote = findViewById(R.id.advisor_note_customer_plus_note);
        progressDialog = new ProgressDialog(Advisor_Note_Customer_Plus.this);

        Intent intent = getIntent();
        get_customerid = intent.getStringExtra("customer_id");
        get_customername = intent.getStringExtra("customer_name");
        Log.d(TAG, "onCreate: " + get_customerid + " / " + get_customername);
        txtname.setText(get_customername);

        get_advisor_user = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
        get_advisor_name = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.name, "");
    }

    public void advisor_note_customer_plus_back_click(View view) {
        finish();
    }

    public void advisor_note_customer_plus_btnok_click(View view) {
        if (txtnote.getText().toString().trim().isEmpty()) {
            Toast.makeText(Advisor_Note_Customer_Plus.this, "لطفا متن یادداشت را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال ثبت یادداشت ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "advisor_note_customer_plus.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    fcm sendnotif = new fcm();
                                    sendnotif.sendNotification("/topics/admin_customer_note","یادداشت برای مشتری" + " " +  get_customername,
                                            get_advisor_name + " : " + txtnote.getText().toString());
                                }catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                Toast.makeText(Advisor_Note_Customer_Plus.this, "یادداشت شما با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Advisor_Note_Customer_Plus.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
//                        String get_advisor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                        params.put("customer_id", get_customerid);
                        params.put("note", txtnote.getText().toString());
                        params.put("advisor", get_advisor_user);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Advisor_Note_Customer_Plus.this);
                requestQueue.add(stringRequest);
            }
        }
    }
}
