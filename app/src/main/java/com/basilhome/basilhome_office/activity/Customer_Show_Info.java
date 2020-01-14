package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.basilhome.basilhome_office.classes.Project;
import com.basilhome.basilhome_office.classes.TextViewUtils;
import com.basilhome.basilhome_office.classes.User_Info;
import com.basilhome.basilhome_office.classes.fcm;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Customer_Show_Info extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    DecoView decoView;
    private String get_customerid, get_name, get_grade, get_tel, get_read, get_advisor_name;
    private String get_budget, get_acquaintance, get_request, get_explanation;
    TextView name, tel;
    TextInputEditText budget, acquaintance, request, explanation;
    private String get_name_from_app, get_user_from_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_show_info);

        get_advisor_name = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.name,"");

        name = findViewById(R.id.customer_show_info_name);
        tel = findViewById(R.id.customer_show_info_tel);
        budget = findViewById(R.id.customer_show_info_budget);
        acquaintance = findViewById(R.id.customer_show_info_acquaintance);
        request = findViewById(R.id.customer_show_info_request);
        explanation = findViewById(R.id.customer_show_info_explanation);

        TextViewUtils.separateGroups(budget);

        Intent i = getIntent();
        get_customerid = i.getStringExtra("customer_id");
        get_name = i.getStringExtra("name");
        get_grade = i.getStringExtra("grade");
        get_read = i.getStringExtra("is_read");
        name.setText(get_name);

        Log.d(TAG, "onCreate: " + get_read);

        get_user_from_app = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user,"");
        get_name_from_app = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.name,"");
        if (get_read.equals("0")){
            if (get_user_from_app.equals("admin") || get_user_from_app.equals("bahar")){
            } else {
                set_isread();
            }
        }

        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d(TAG, "onCreate: " + get_customerid);
            loadCustomer();
        }

        decoView = findViewById(R.id.customer_show_info_chart);
        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build());
        SeriesItem seriesItem = new SeriesItem.Builder(Color.argb(255, 103, 76, 153))
                .setRange(0, 100, 0)
                .setSeriesLabel(new SeriesLabel.Builder("%.0f%%")
                        .setColorBack(Color.argb(218, 0, 0, 0))
                        .setColorText(Color.argb(255, 255, 255, 255))
                        .build())
                .build();
        int series1Index = decoView.addSeries(seriesItem);
        decoView.addEvent(new DecoEvent.Builder(Float.parseFloat(get_grade)).setIndex(series1Index).setDelay(250).build());
    }

    private void loadCustomer() {
        String URL = HttpUrl.geturl + "get_customer_info.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                get_tel = jsonObject.getString("tel");
                                get_acquaintance = jsonObject.getString("acquaintance");
                                get_request = jsonObject.getString("request");
                                get_budget = jsonObject.getString("budget");
                                get_explanation = jsonObject.getString("explanation");


                                tel.setText(get_tel);
                                acquaintance.setText(get_acquaintance);
                                request.setText(get_request);
                                budget.setText(get_budget);
                                explanation.setText(get_explanation);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Customer_Show_Info.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", get_customerid);
                Log.d(TAG, "getParams: " + get_customerid);
                return params;
            }
        };
        Volley.newRequestQueue(Customer_Show_Info.this).add(stringRequest);

    }

    public void set_isread(){
        String URL = HttpUrl.geturl + "set_customer_is_read.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            fcm sendnotif = new fcm();
                            sendnotif.sendNotification("/topics/it_customer_read", "اطلاعات مشتری رویت شد","اطلاعات مشتری" + " " + get_name + " " + "توسط کارشناس مربوطه" + " " + get_advisor_name + " " + "رویت شد");
                            sendnotif.sendNotification("/topics/admin_customer_read", "رویت اطلاعات مشتری", "مشتری " + " " + get_name +"از طرف کارشناس " + " " + get_name_from_app + " " + "رویت و دریافت شد");
                        }catch (Exception exception) {
                            exception.printStackTrace();
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
                final Calendar myCalendar = Calendar.getInstance();
                String myFormat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", get_customerid);
                params.put("is_read", "1");
                params.put("read_time", sdf.format(myCalendar.getTime()));
                Log.d(TAG, "getParams: " + get_customerid);
                return params;
            }
        };
        Volley.newRequestQueue(Customer_Show_Info.this).add(stringRequest);
    }

    public void customer_show_info_back_click(View v) {
        finish();
    }

    public void customer_show_info_tel_click(View v) {
        if (get_tel.equals("")) {
            Toast.makeText(Customer_Show_Info.this, "متاسفانه اطلاعاتی موجود نیست", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + get_tel));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(Customer_Show_Info.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void customer_show_info_whatsapp_click(View v) {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, tel.getText())
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        startActivity(intent);

//        String newName = get_tel.substring(2, 14);
//        String whatsapp = "https://api.whatsapp.com/send?phone=" + newName;
//        if (whatsapp.equals("")) {
//            Toast.makeText(Customer_Show_Info.this, "متاسفانه اطلاعاتی موجود نیست", Toast.LENGTH_SHORT).show();
//        } else {
//            String url = "https://api.whatsapp.com/send?phone=" + whatsapp;
//            try {
//                PackageManager pm = getApplicationContext().getPackageManager();
//                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            } catch (PackageManager.NameNotFoundException e) {
//                Toast.makeText(Customer_Show_Info.this, "لطفا ابتدا اپلیکیشن واتساپ را نصب نمایید", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
    }

    public void customer_show_info_note_click(View v) {
        Intent intent = new Intent(Customer_Show_Info.this,Customer_Note_List.class);
        intent.putExtra("customer_name",get_name);
        intent.putExtra("customer_id",get_customerid);
//        intent.putExtra("advisor_name",get_advisor_name);
        startActivity(intent);
    }


}
