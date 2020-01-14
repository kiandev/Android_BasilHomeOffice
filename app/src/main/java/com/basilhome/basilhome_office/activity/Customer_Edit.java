package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.basilhome.basilhome_office.classes.fcm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Customer_Edit extends AppCompatActivity {

    TextInputEditText name, tel, budget, explanation;
    Spinner acquaintance, request_title, request_sub;
    String please_select, getprogress;
    AppCompatSeekBar seekBar;
    TextView seekbar_text;
    ProgressDialog progressDialog;
    public static final String TAG = MainActivity.TAG;
    Button button_plus;
    private String get_customer_id, get_customer_name, get_customer_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_edit);

        getprogress = "0";

        Intent intent = getIntent();
        get_customer_id = intent.getStringExtra("customer_id");
        get_customer_name = intent.getStringExtra("name");
        get_customer_tel = intent.getStringExtra("tel");

        please_select = "انتخاب کنید";
        progressDialog = new ProgressDialog(Customer_Edit.this);

        button_plus = findViewById(R.id.customer_edit_btnok);
        name = findViewById(R.id.customer_edit_fullname);
        tel = findViewById(R.id.customer_edit_tel);
        budget = findViewById(R.id.customer_edit_budget);
        explanation = findViewById(R.id.customer_edit_explanation);

        name.setText(get_customer_name);
        tel.setText(get_customer_tel);


        acquaintance = findViewById(R.id.customer_edit_acquaintance);
        request_title = findViewById(R.id.customer_edit_request_title);
        request_sub = findViewById(R.id.customer_edit_request_sub);
        seekBar = findViewById(R.id.customer_edit_seekbar);
        seekbar_text = findViewById(R.id.customer_edit_seekbar_text);

        String[] items = new String[]{please_select, "اینستاگرام", "موتور جستجوی گوگل", "سایت بازیل هوم", "تبلیغات محیطی", "تبلیغات شبکه جم"};
        ArrayAdapter<String> adapter_acquaintance = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter_acquaintance.setDropDownViewResource(R.layout.row_spn_dropdown);
        acquaintance.setAdapter(adapter_acquaintance);

        final DataBaseFilter ada = new DataBaseFilter(this);
        ArrayAdapter<String> adapter_title = new ArrayAdapter<>(this, R.layout.row_spn, ada.getOstan());
        adapter_title.setDropDownViewResource(R.layout.row_spn_dropdown);
        request_title.setAdapter(adapter_title);

        ArrayAdapter<String> adapter_sub = new ArrayAdapter<>(Customer_Edit.this, R.layout.row_spn, ada.getcity(0));
        adapter_sub.setDropDownViewResource(R.layout.row_spn_dropdown);
        request_sub.setAdapter(adapter_sub);
        request_title.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter_city = new ArrayAdapter<>(Customer_Edit.this, R.layout.row_spn, ada.getcity(position + 0));
                adapter_city.setDropDownViewResource(R.layout.row_spn_dropdown);
                request_sub.setAdapter(adapter_city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

    public void customer_edit_btnok_click(View view) {
        if (name.getText().toString().trim().isEmpty()) {
            Toast.makeText(Customer_Edit.this, "لطفا نام و نام خانوادگی مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();

        } else if (tel.getText().toString().trim().isEmpty()) {
            Toast.makeText(Customer_Edit.this, "لطفا شماره تماس مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else if (acquaintance.getSelectedItemId() == 0) {
            Toast.makeText(Customer_Edit.this, "لطفا نحوه آشنایی مشتری با مجموعه را مشخص نمایید", Toast.LENGTH_SHORT).show();
        } else if (request_title.getSelectedItemId() == 0) {
            Toast.makeText(Customer_Edit.this, "لطفا نوع خدمات درخواست مشتری را مشخص نمایید", Toast.LENGTH_SHORT).show();
        } else if (budget.getText().toString().trim().isEmpty()) {
            Toast.makeText(Customer_Edit.this, "لطفا میزان بودجه مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال ویرایش اطلاعات ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "customer_edit_by_it.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(Customer_Edit.this, "اطلاعات مشتری با موفقیت ویرایش گردید", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Customer_Edit.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("customer_id", get_customer_id);
                        params.put("name", name.getText().toString());
                        params.put("tel", tel.getText().toString());
                        params.put("acquaintance", acquaintance.getSelectedItem().toString());
                        params.put("request", request_sub.getSelectedItem().toString());
                        params.put("explanation", explanation.getText().toString());
                        params.put("budget", budget.getText().toString());
                        params.put("grade", getprogress);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Customer_Edit.this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public void customer_edit_back_click(View view) {
        finish();
    }

}
