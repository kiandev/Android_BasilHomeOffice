package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.basilhome.basilhome_office.classes.MyFirebaseMessagingService;
import com.basilhome.basilhome_office.classes.User_Info;
import com.basilhome.basilhome_office.classes.custom_dialog;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_User extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    TextInputEditText getuser, getpass;
    ProgressDialog progressDialog;
    private String tokenId;
    Button login_btnok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_user);
        getuser = findViewById(R.id.login_getuser);
        getpass = findViewById(R.id.login_getpass);
        login_btnok = findViewById(R.id.login_btnok);
        progressDialog = new ProgressDialog(Login_User.this);
        subscribeUserToParse();

        login_btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btnok.setEnabled(false);
                login_user();
            }
        });

    }

    public void login_user() {
        if (getuser.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "لطفا نام کاربری خود را وارد نمایید", Toast.LENGTH_SHORT).show();
            login_btnok.setEnabled(true);

        } else if (getpass.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "لطفا کلمه عبور خود را وارد نمایید", Toast.LENGTH_SHORT).show();
            login_btnok.setEnabled(true);

        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                login_btnok.setEnabled(true);

            } else {
                progressDialog.setMessage("در حال بررسی جهت ورود به سامانه");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "user_login.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (response.equals("0")) {
                                    getuser.setText("");
                                    getpass.setText("");
                                    login_btnok.setEnabled(true);
                                    custom_dialog dialog = new custom_dialog(Login_User.this);
                                    dialog.setTitle("عملیات ناموفق !");
                                    dialog.setMessage("کاربری با این مشخصات یافت نشد");
                                    dialog.show();

                                } else {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String name_fromhost = jsonObject.getString("name");
                                            String part_fromhost = jsonObject.getString("part");
                                            String title_fromhost = jsonObject.getString("title");
                                            String img_fromhost = jsonObject.getString("img");
                                            String block_fromhost = jsonObject.getString("block");
                                            if (block_fromhost.equals("1")) {
                                                custom_dialog dialog = new custom_dialog(Login_User.this);
                                                dialog.setTitle("عملیات ناموفق !");
                                                dialog.setMessage("در حال حاضر دسترسی شما به سامانه مسدود گردیده است");
                                                dialog.show();
                                                login_btnok.setEnabled(true);
                                            } else {
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.user, getuser.getText().toString()).apply();
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.name, name_fromhost).apply();
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.pass, getpass.getText().toString()).apply();
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.part, part_fromhost).apply();
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.title, title_fromhost).apply();
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.image, img_fromhost).apply();
                                                if (part_fromhost.equals("فروش")) {
                                                    Intent intent = new Intent(Login_User.this, Panel_Selling.class);
                                                    Log.d(TAG, "data: " + jsonObject);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (part_fromhost.equals("مدیریت")) {
                                                    Intent intent = new Intent(Login_User.this, Panel_Admin.class);
                                                    Log.d(TAG, "data: " + jsonObject);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (part_fromhost.equals("انفورماتیک")){
                                                    Intent intent = new Intent(Login_User.this, Panel_IT.class);
                                                    Log.d(TAG, "data: " + jsonObject);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Login_User.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                                login_btnok.setEnabled(true);
                                getuser.setText("");
                                getpass.setText("");
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        String user_fromtext = getuser.getText().toString();
                        String pass_fromtext = getpass.getText().toString();
                        params.put("user", user_fromtext);
                        params.put("pass", pass_fromtext);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }
    }

    public void login_btncancelclick(View view) {
        finish();
    }

    public void login_user_fingerprintclick(View view) {
        Boolean get_fingerprint = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(User_Info.fingerprint,false);
        if (get_fingerprint == true){
            Intent i;
            i = new Intent(Login_User.this, Fingerprint.class);
            startActivity(i);
        } else {
            custom_dialog dialog = new custom_dialog(Login_User.this);
            dialog.setTitle("عملیات ناموفق !");
            dialog.setMessage("جهت ورود از طریق اثر انگشت ، پس از ورود به سامانه از قسمت تنظیمات ، گزینه ورود با اثر انگشت را فعال نمایید");
            dialog.show();
        }
    }

    private void subscribeUserToParse() {
        tokenId = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(tokenId)) {
            Intent intent = new Intent(this, MyFirebaseMessagingService.class);
            startService(intent);
            return;
        }
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.tokenid, tokenId).apply();
    }

}
