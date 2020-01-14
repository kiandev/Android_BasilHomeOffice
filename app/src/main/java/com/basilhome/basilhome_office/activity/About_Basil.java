package com.basilhome.basilhome_office.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.HttpUrl;
import com.basilhome.basilhome_office.classes.InternetTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class About_Basil extends AppCompatActivity {

    TextView textmatn;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    VideoView videoView;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_basil);
        textmatn = findViewById(R.id.about_basil_text);
        videoView = findViewById(R.id.about_show_video);
        requestQueue = Volley.newRequestQueue(About_Basil.this);
        progressDialog = new ProgressDialog(About_Basil.this);
        if (!InternetTest.ok(getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            getdata();
        }

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.basil_video);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public void getdata() {
        String URL = HttpUrl.geturl + "get_about_basil.php";
        progressDialog.setMessage("در حال فراخوانی ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                Log.d(TAG, "onResponse: " + jsonobject);
                                String text = jsonobject.getString("text");
                                textmatn.setText(text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onErrorResponse: " + volleyError);
                        Toast.makeText(About_Basil.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(About_Basil.this);
        requestQueue.add(stringRequest);
    }

    public void about_basil_back_click (View v) {
        finish();
    }
}
