package com.basilhome.basilhome_office.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.basilhome.basilhome_office.R;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "basil_home_office";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
    }
}
