package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.User_Info;

public class User_Settings extends AppCompatActivity {

    SwitchCompat switchCompat;
    private Boolean get_fingerprint;
    TextView txtfingerprint;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_settings);
        switchCompat = findViewById(R.id.user_settings_switch);
        txtfingerprint = findViewById(R.id.user_settings_txtfingerprint);
        get_fingerprint = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(User_Info.fingerprint,false);
        if (get_fingerprint == true){
            switchCompat.setChecked(true);
            txtfingerprint.setText("فعال است");
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(User_Info.fingerprint,switchCompat.isChecked()).apply();
                    txtfingerprint.setText("فعال است");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(User_Info.fingerprint, switchCompat.isChecked()).apply();
                    txtfingerprint.setText("غیرفعال است");
                }
            }
        });
    }

    public void user_settings_back_click (View v) {
        finish();
    }

    public void user_settings_changepass_click (View v) {
        Intent i = new Intent(User_Settings.this,Change_User_Password.class);
        startActivity(i);
    }

}
