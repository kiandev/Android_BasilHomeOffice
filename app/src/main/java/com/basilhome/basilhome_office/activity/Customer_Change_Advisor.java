package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.basilhome.basilhome_office.classes.Tokenid_Info;
import com.basilhome.basilhome_office.classes.fcm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Customer_Change_Advisor extends AppCompatActivity {

    TextInputEditText name;
    Spinner advisor_spin;
    String please_select;
    ProgressDialog progressDialog;
    public static final String TAG = MainActivity.TAG;
    Button button_plus;
    private String get_customer_id, set_advisor, get_customer_name;
    private String masi, amir, samira, vahideh;
    private String set_tokenid;
    private String get_masi_tokenid, get_samira_tokenid, get_amir_tokenid, get_vahideh_tokenid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_change_advisor);

        get_masi_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.masi,"");
        get_samira_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.samira,"");
        get_amir_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.amir,"");
        get_vahideh_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.vahideh,"");

        masi = "معصومه فرجی";
        amir = "امیر اصفهانی";
        samira = "سمیرا نظری";
        vahideh = "وحیده هدایتی";

        Intent intent = getIntent();
        get_customer_id = intent.getStringExtra("customer_id");
        Log.d(TAG, "onCreate: " + get_customer_id);
        get_customer_name = intent.getStringExtra("name");

        please_select = "انتخاب کنید";
        progressDialog = new ProgressDialog(Customer_Change_Advisor.this);

        button_plus = findViewById(R.id.customer_change_btnok);
        name = findViewById(R.id.customer_change_fullname);

        name.setText(get_customer_name);

        advisor_spin = findViewById(R.id.customer_change_advisor_spinner);

        String[] items2 = new String[]{please_select, masi, amir, samira, vahideh};
        ArrayAdapter<String> adapter_advisor = new ArrayAdapter<>(this, R.layout.row_spn, items2);
        adapter_advisor.setDropDownViewResource(R.layout.row_spn_dropdown);
        advisor_spin.setAdapter(adapter_advisor);

        advisor_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (advisor_spin.getSelectedItemId() == 0) {
                    button_plus.setEnabled(false);
                    button_plus.setBackground(getResources().getDrawable(R.drawable.button_disable));
                    button_plus.setTextColor(getResources().getColor(R.color.colorGray500));
                } else {
                    button_plus.setEnabled(true);
                    button_plus.setBackground(getResources().getDrawable(R.drawable.button_ok));
                    button_plus.setTextColor(getResources().getColor(R.color.colorWhite));
                    if (advisor_spin.getSelectedItem().equals(masi)){
                        set_advisor = masi;
                        set_tokenid = get_masi_tokenid;

                    } else if (advisor_spin.getSelectedItem().equals(samira)){
                        set_advisor = samira;
                        set_tokenid = get_samira_tokenid;

                    } else if (advisor_spin.getSelectedItem().equals(amir)){
                        set_advisor = amir;
                        set_tokenid = get_amir_tokenid;

                    } else if (advisor_spin.getSelectedItem().equals(vahideh)){
                        set_advisor = vahideh;
                        set_tokenid = get_vahideh_tokenid;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    public void customer_change_btnok_click(View view) {
        if (advisor_spin.getSelectedItemId() == 0) {
            Toast.makeText(Customer_Change_Advisor.this, "لطفا کارشناس مربوطه را انتخاب نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال تغییر کارشناس مربوطه ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "customer_change_advisor.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    fcm sendnotif = new fcm();
                                    sendnotif.sendNotification(set_tokenid, "مشتری جدید :" + " " + name.getText().toString(), "لطفا جهت مشاهده اطلاعات مشتری مورد نظر به قسمت لیست مشتریان خود بروید سپس روی دکمه مشتری مورد نظر کلیک نمایید");
                                    sendnotif.sendNotification("/topics/admin_customer_plus", "تغییر کارشناس مربوطه", "اطلاعات مشتری " + " " + name.getText().toString() +"برای کارشناس " + " " + set_advisor + " " + "ثبت مجدد و ارسال گردید");
                                    progressDialog.dismiss();
                                    Toast.makeText(Customer_Change_Advisor.this, "کارشنس مربوطه با موفقیت تغییر یافت", Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Customer_Change_Advisor.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("customer_id", get_customer_id);
                        if (advisor_spin.getSelectedItem().equals(masi)) {
                            params.put("advisor", "masi");
                        }
                        if (advisor_spin.getSelectedItem().equals(amir)) {
                            params.put("advisor", "amir");
                        }
                        if (advisor_spin.getSelectedItem().equals(samira)) {
                            params.put("advisor", "samira");
                        }
                        if (advisor_spin.getSelectedItem().equals(vahideh)) {
                            params.put("advisor", "vahideh");
                        }
                        params.put("is_read", "0");
                        params.put("read_time", "");
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Customer_Change_Advisor.this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public void customer_change_back_click(View view) {
        finish();
    }

}
