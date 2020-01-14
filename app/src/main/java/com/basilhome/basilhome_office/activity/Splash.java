package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.MyFirebaseMessagingService;
import com.basilhome.basilhome_office.classes.Tokenid_Info;
import com.basilhome.basilhome_office.classes.User_Info;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {

    private String tokenId;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    get_token_id_vahideh();
                    get_token_id_amir();
                    get_token_id_samira();
                    get_token_id_masi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        subscribeUserToParse();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                i = new Intent(Splash.this, Login_User.class);
                startActivity(i);
                finish();
            }
        }, 1000);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String general = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("subscribeToGeneral", "No");
                if (general.equals("No")) {
                    FirebaseMessaging.getInstance().subscribeToTopic("general").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("subscribeToGeneral", "Yes").apply();
                        }
                    });
                }

            }
        });
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

    private void get_token_id_vahideh() {
        String httpurl = HttpUrl.geturl + "get_tokenid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")){

                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d(TAG, "onResponse: " + response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.d(TAG, "vahide_tokenid: " + jsonObject);
                                    String get_tokenid = jsonObject.getString("tokenid");
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(Tokenid_Info.vahideh,get_tokenid).apply();
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advisor", "vahideh");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
        requestQueue.add(stringRequest);
    }

    private void get_token_id_amir() {
        String httpurl = HttpUrl.geturl + "get_tokenid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")){

                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d(TAG, "onResponse: " + response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.d(TAG, "amir_tokenid: " + jsonObject);
                                    String get_tokenid = jsonObject.getString("tokenid");
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(Tokenid_Info.amir,get_tokenid).apply();
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advisor", "amir");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
        requestQueue.add(stringRequest);
    }

    private void get_token_id_masi() {
        String httpurl = HttpUrl.geturl + "get_tokenid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")){

                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d(TAG, "onResponse: " + response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.d(TAG, "masi_tokenid: " + jsonObject);
                                    String get_tokenid = jsonObject.getString("tokenid");
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(Tokenid_Info.masi,get_tokenid).apply();
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advisor", "masi");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
        requestQueue.add(stringRequest);
    }

    private void get_token_id_samira() {
        String httpurl = HttpUrl.geturl + "get_tokenid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")){

                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d(TAG, "onResponse: " + response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.d(TAG, "samira_tokenid: " + jsonObject);
                                    String get_tokenid = jsonObject.getString("tokenid");
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(Tokenid_Info.samira,get_tokenid).apply();
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advisor", "samira");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
        requestQueue.add(stringRequest);
    }
}
