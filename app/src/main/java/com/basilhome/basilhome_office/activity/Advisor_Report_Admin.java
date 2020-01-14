package com.basilhome.basilhome_office.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Advisor_Report_Admin extends AppCompatActivity {

    TextInputEditText txtdate, txttext;
    ProgressDialog progressDialog;
    String get_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_advisor_report_admin);

        txtdate = findViewById(R.id.advisor_report_date);
        txttext = findViewById(R.id.advisor_report_text);
        progressDialog = new ProgressDialog(Advisor_Report_Admin.this);
        get_username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.name, "");
        updateLabel();
    }

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtdate.setText(sdf.format(myCalendar.getTime()));
    }

    public void advisor_report_date_click(View view) {
        new DatePickerDialog(Advisor_Report_Admin.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void advisor_report_back_click(View view) {
        finish();
    }

    public void advisor_report_btnok_click(View view) {
        if (txttext.getText().toString().trim().isEmpty()) {
            Toast.makeText(Advisor_Report_Admin.this, "لطفا متن گزارش را وارد نمایید", Toast.LENGTH_SHORT).show();

        } else if (txttext.getText().length() < 25){
            Toast.makeText(Advisor_Report_Admin.this, "متن گزارش وارد شده کوتاه می باشد", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال ثبت گزارش ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "advisor_report_plus.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    fcm sendnotif = new fcm();
                                    sendnotif.sendNotification("/topics/admin_advisor_report","گزارش روزانه" + " " + get_username,txttext.getText().toString());
                                }catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                Toast.makeText(Advisor_Report_Admin.this, "گزارش روزانه شما با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Advisor_Report_Admin.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        String get_advisor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user, "");
                        params.put("text", txttext.getText().toString());
                        params.put("advisor", get_advisor);
                        params.put("date", txtdate.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Advisor_Report_Admin.this);
                requestQueue.add(stringRequest);
            }
        }
    }
}
