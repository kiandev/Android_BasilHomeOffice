package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.basilhome.basilhome_office.classes.User_Info;
import com.basilhome.basilhome_office.classes.custom_dialog;
import com.basilhome.basilhome_office.classes.fcm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Customer_Plus extends AppCompatActivity {

    TextInputEditText name, tel, budget, explanation;
    Spinner acquaintance, request_title, request_sub, advisor_spinner;
    String please_select, getprogress;
    AppCompatSeekBar seekBar;
    TextView seekbar_text;
    ProgressDialog progressDialog;
    private String masi, amir, samira, vahideh;
    public static final String TAG = MainActivity.TAG;
    Button button_plus;
    private String set_advisor, set_tokenid;
    private String get_masi_tokenid, get_samira_tokenid, get_amir_tokenid, get_vahideh_tokenid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_plus);

        get_masi_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.masi,"");
        get_samira_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.samira,"");
        get_amir_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.amir,"");
        get_vahideh_tokenid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Tokenid_Info.vahideh,"");

        masi = "معصومه فرجی";
        amir = "امیر اصفهانی";
        samira = "سمیرا نظری";
        vahideh = "وحیده هدایتی";

        getprogress = "0";

        please_select = "انتخاب کنید";
        progressDialog = new ProgressDialog(Customer_Plus.this);

        button_plus = findViewById(R.id.customer_plus_btnok);
        name = findViewById(R.id.customer_plus_fullname);
        tel = findViewById(R.id.customer_plus_tel);
        budget = findViewById(R.id.customer_plus_budget);
        explanation = findViewById(R.id.customer_plus_explanation);

        advisor_spinner = findViewById(R.id.customer_plus_advisor_spinner);
        acquaintance = findViewById(R.id.customer_plus_acquaintance);
        request_title = findViewById(R.id.customer_plus_request_title);
        request_sub = findViewById(R.id.customer_plus_request_sub);
        seekBar = findViewById(R.id.customer_plus_seekbar);
        seekbar_text = findViewById(R.id.customer_plus_seekbar_text);

        String[] items = new String[]{please_select, "اینستاگرام", "موتور جستجوی گوگل", "سایت بازیل هوم", "تبلیغات محیطی", "تبلیغات شبکه جم"};
        ArrayAdapter<String> adapter_acquaintance = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter_acquaintance.setDropDownViewResource(R.layout.row_spn_dropdown);
        acquaintance.setAdapter(adapter_acquaintance);

        String[] items2 = new String[]{please_select, masi, amir, samira, vahideh};
        ArrayAdapter<String> adapter_advisor = new ArrayAdapter<>(this, R.layout.row_spn, items2);
        adapter_advisor.setDropDownViewResource(R.layout.row_spn_dropdown);
        advisor_spinner.setAdapter(adapter_advisor);

        final DataBaseFilter ada = new DataBaseFilter(this);
        ArrayAdapter<String> adapter_title = new ArrayAdapter<>(this, R.layout.row_spn, ada.getOstan());
        adapter_title.setDropDownViewResource(R.layout.row_spn_dropdown);
        request_title.setAdapter(adapter_title);

        ArrayAdapter<String> adapter_sub = new ArrayAdapter<>(Customer_Plus.this, R.layout.row_spn, ada.getcity(0));
        adapter_sub.setDropDownViewResource(R.layout.row_spn_dropdown);
        request_sub.setAdapter(adapter_sub);
        request_title.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter_city = new ArrayAdapter<>(Customer_Plus.this, R.layout.row_spn, ada.getcity(position + 0));
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

        advisor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (advisor_spinner.getSelectedItemId() == 0) {
                    button_plus.setEnabled(false);
                    button_plus.setBackground(getResources().getDrawable(R.drawable.button_disable));
                    button_plus.setTextColor(getResources().getColor(R.color.colorGray500));
                } else {
                    button_plus.setEnabled(true);
                    button_plus.setBackground(getResources().getDrawable(R.drawable.button_ok));
                    button_plus.setTextColor(getResources().getColor(R.color.colorWhite));
                    if (advisor_spinner.getSelectedItem().equals(masi)){
                        set_advisor = masi;
                        set_tokenid = get_masi_tokenid;

                    } else if (advisor_spinner.getSelectedItem().equals(samira)){
                        set_advisor = samira;
                        set_tokenid = get_samira_tokenid;

                    } else if (advisor_spinner.getSelectedItem().equals(amir)){
                        set_advisor = amir;
                        set_tokenid = get_amir_tokenid;

                    } else if (advisor_spinner.getSelectedItem().equals(vahideh)){
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

    public void customer_plus_btnok_click(View view) {
        if (name.getText().toString().trim().isEmpty()) {
            Toast.makeText(Customer_Plus.this, "لطفا نام و نام خانوادگی مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();

        } else if (tel.getText().toString().trim().isEmpty()) {
            Toast.makeText(Customer_Plus.this, "لطفا شماره تماس مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else if (acquaintance.getSelectedItemId() == 0) {
            Toast.makeText(Customer_Plus.this, "لطفا نحوه آشنایی مشتری با مجموعه را مشخص نمایید", Toast.LENGTH_SHORT).show();
        } else if (request_title.getSelectedItemId() == 0) {
            Toast.makeText(Customer_Plus.this, "لطفا نوع خدمات درخواست مشتری را مشخص نمایید", Toast.LENGTH_SHORT).show();
        } else if (budget.getText().toString().trim().isEmpty()) {
            Toast.makeText(Customer_Plus.this, "لطفا میزان بودجه مشتری را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else if (advisor_spinner.getSelectedItemId() == 0) {
            Toast.makeText(Customer_Plus.this, "لطفا کارشناس مربوطه جهت پاسخ گویی را مشخص نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال ثبت اطلاعات ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "customer_plus.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    fcm sendnotif = new fcm();
                                    sendnotif.sendNotification(set_tokenid, "مشتری جدید :" + " " + name.getText().toString(), "لطفا جهت مشاهده اطلاعات مشتری مورد نظر به قسمت لیست مشتریان خود بروید سپس روی دکمه مشتری مورد نظر کلیک نمایید");
                                    sendnotif.sendNotification("/topics/admin_customer_plus", "ثبت مشتری جدید", "اطلاعات مشتری" + " " + name.getText().toString() + " " + "برای کارشناس" + " " + set_advisor + " " + "ثبت و ارسال گردید");
                                    progressDialog.dismiss();
                                    Toast.makeText(Customer_Plus.this, "اطلاعات مشتری با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Customer_Plus.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
//                        String get_advisor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                        final Calendar myCalendar = Calendar.getInstance();
                        String myFormat1 = "MMddyyyyHHmmss";
                        String myFormat2 = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                        params.put("customer_id", sdf1.format(myCalendar.getTime()));
                        params.put("name", name.getText().toString());
                        params.put("tel", tel.getText().toString());
                        params.put("acquaintance", acquaintance.getSelectedItem().toString());
                        params.put("request", request_sub.getSelectedItem().toString());
                        params.put("explanation", explanation.getText().toString());
                        params.put("budget", budget.getText().toString());
                        params.put("grade", getprogress);
                        params.put("create_time", sdf2.format(myCalendar.getTime()));
                        if (advisor_spinner.getSelectedItem().equals(masi)) {
                            params.put("advisor", "masi");
                        }
                        if (advisor_spinner.getSelectedItem().equals(amir)) {
                            params.put("advisor", "amir");
                        }
                        if (advisor_spinner.getSelectedItem().equals(samira)) {
                            params.put("advisor", "samira");
                        }
                        if (advisor_spinner.getSelectedItem().equals(vahideh)) {
                            params.put("advisor", "vahideh");
                        }
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Customer_Plus.this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public void customer_plus_back_click(View view) {
        finish();
    }

}
