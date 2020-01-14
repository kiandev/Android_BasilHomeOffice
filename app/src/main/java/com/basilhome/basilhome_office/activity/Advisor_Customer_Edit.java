package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.DataBaseFilter;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;
import com.basilhome.basilhome_office.classes.User_Info;

import java.util.HashMap;
import java.util.Map;

public class Advisor_Customer_Edit extends AppCompatActivity {

    TextInputEditText name, budget;
    String getprogress, get_customerid, get_customername, get_advisor_user;
    AppCompatSeekBar seekBar;
    TextView seekbar_text;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_advisor_customer_edit);

        progressDialog = new ProgressDialog(Advisor_Customer_Edit.this);

        name = findViewById(R.id.advisor_customer_edit_fullname);
        budget = findViewById(R.id.advisor_customer_edit_budget);

        get_advisor_user = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");

        seekBar = findViewById(R.id.advisor_customer_edit_seekbar);
        seekbar_text = findViewById(R.id.advisor_customer_edit_seekbar_text);

        Intent intent = getIntent();
        get_customerid = intent.getStringExtra("customer_id");
        get_customername = intent.getStringExtra("customer_name");
        getprogress = intent.getStringExtra("grade");
        name.setText(get_customername);

        final String[] percent = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekbar_text.setText("" + percent[progress] + "%");
                getprogress = percent[progress];
            }
        });
    }

    public void advisor_customer_edit_btnok_click(View view) {
        if (budget.getText().toString().trim().isEmpty()) {
            Toast.makeText(Advisor_Customer_Edit.this, "لطفا میزان بودجه مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال ویرایش اطلاعات ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "customer_edit.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(Advisor_Customer_Edit.this, "اطلاعات مشتری با موفقیت ویرایش گردید", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Advisor_Customer_Edit.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
//                        String get_advisor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                        params.put("customer_id", get_customerid);
                        params.put("name", name.getText().toString());
                        params.put("budget", budget.getText().toString());
                        params.put("grade", getprogress);
                        params.put("advisor", get_advisor_user);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Advisor_Customer_Edit.this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public void advisor_customer_edit_back_click(View view) {
        finish();
    }
}
