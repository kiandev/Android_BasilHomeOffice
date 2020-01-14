package com.basilhome.basilhome_office.classes;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.activity.Login_User;
import com.basilhome.basilhome_office.activity.Panel_Admin;
import com.basilhome.basilhome_office.activity.Panel_Selling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;

    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Toast.makeText(context, "خطای نامشخص !\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "احراز هویت ناموفق بود", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "خطای نامشخص !\n" + helpString, Toast.LENGTH_LONG).show();
    }

    @Override

    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        login_user();
    }

    public void login_user() {
        String httpurl = HttpUrl.geturl + "user_login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")) {
                            custom_dialog dialog = new custom_dialog(FingerprintHandler.this.context);
                            dialog.setTitle("عملیات ناموفق !");
                            dialog.setMessage("کاربری با این مشخصات یافت نشد");
                            dialog.show();
                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String part_fromhost = jsonObject.getString("part");
                                    String block_fromhost = jsonObject.getString("block");
                                    if (block_fromhost.equals("1")) {
                                        custom_dialog dialog = new custom_dialog(FingerprintHandler.this.context);
                                        dialog.setTitle("عملیات ناموفق !");
                                        dialog.setMessage("در حال حاضر دسترسی شما به سامانه مسدود گردیده است");
                                        dialog.show();
                                    } else {
                                        if (part_fromhost.equals("فروش")) {
                                            FingerprintHandler.this.context.startActivity(new Intent(FingerprintHandler.this.context, Panel_Selling.class));
                                        } else if (part_fromhost.equals("مدیریت")) {
                                            FingerprintHandler.this.context.startActivity(new Intent(FingerprintHandler.this.context, Panel_Admin.class));
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
                        Toast.makeText(FingerprintHandler.this.context, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String user_fromtext = PreferenceManager.getDefaultSharedPreferences(FingerprintHandler.this.context).getString(User_Info.user,"");
                String pass_fromtext = PreferenceManager.getDefaultSharedPreferences(FingerprintHandler.this.context).getString(User_Info.pass,"");
                params.put("user", user_fromtext);
                params.put("pass", pass_fromtext);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FingerprintHandler.this.context);
        requestQueue.add(stringRequest);
    }


}
