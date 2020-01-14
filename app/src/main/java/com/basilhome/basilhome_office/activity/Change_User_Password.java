package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class Change_User_Password extends AppCompatActivity {

    TextInputEditText txtnow, txtnew, txtagain;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_user_password);

        txtnow = findViewById(R.id.change_user_pass_now);
        txtnew = findViewById(R.id.change_user_pass_new);
        txtagain = findViewById(R.id.change_user_pass_again);
        progressDialog = new ProgressDialog(Change_User_Password.this);

    }

    public void change_user_pass_back_click (View view) {
        finish();
    }

    public void change_user_pass_btnok_click(View view) {
        String get_pass = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.pass,"");
        if (txtnow.getText().toString().trim().isEmpty()) {
            Toast.makeText(Change_User_Password.this, "لطفا کلمه عبور فعلی را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else if (txtnew.getText().toString().trim().isEmpty()){
            Toast.makeText(Change_User_Password.this, "لطفا کلمه عبور جدید را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else if (txtagain.getText().toString().trim().isEmpty()){
            Toast.makeText(Change_User_Password.this, "لطفا کلمه عبور جدید را تکرار نمایید", Toast.LENGTH_SHORT).show();
        } else if (txtnew.getText().length() < 2){
            Toast.makeText(Change_User_Password.this, "کلمه عبور جدید وارد شده کوتاه می باشد", Toast.LENGTH_SHORT).show();
        } else if (!txtagain.getText().toString().equals(txtnew.getText().toString())){
            Toast.makeText(Change_User_Password.this, "کلمه عبور جدید وارد شده با تکرار آن برابر نیست", Toast.LENGTH_SHORT).show();
        } else if (!txtnow.getText().toString().equals(get_pass)){
            Toast.makeText(Change_User_Password.this, "کلمه عبور فعلی وارد شده اشتباه است", Toast.LENGTH_SHORT).show();
        } else {
            if (!InternetTest.ok(this.getApplicationContext())) {
                Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("در حال تغییر کلمه عبور ، لطفا منتظر بمانید ...");
                progressDialog.show();
                String httpurl = HttpUrl.geturl + "change_user_pass.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        httpurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(Change_User_Password.this, "کلمه عبور شما با موفقیت تغییر یافت", Toast.LENGTH_SHORT).show();
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(User_Info.pass,txtnew.getText().toString()).apply();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Change_User_Password.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        String get_user = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(User_Info.user,"");
                        params.put("user", get_user);
                        params.put("pass", txtnew.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Change_User_Password.this);
                requestQueue.add(stringRequest);
            }
        }
    }
}
