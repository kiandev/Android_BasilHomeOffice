package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.Topic_Info;
import com.basilhome.basilhome_office.classes.User_Info;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class IT_Settings extends AppCompatActivity {

    SwitchCompat sf_fingerprint, sf_customerread;
    private Boolean get_fingerprint, get_customerread;
    TextView txtfingerprint, txtcustomerread;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_it_settings);
        sf_fingerprint = findViewById(R.id.it_settings_switchoffingerprint);
        sf_customerread = findViewById(R.id.it_settings_switchofcustomerread);
        txtfingerprint = findViewById(R.id.it_settings_txtfingerprint);
        txtcustomerread = findViewById(R.id.it_settings_txtcustomerread);

        get_fingerprint = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(User_Info.fingerprint,false);
        get_customerread = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Topic_Info.customer_read,false);

        if (get_fingerprint == true){
            sf_fingerprint.setChecked(true);
            txtfingerprint.setText("فعال است");
        }

        if (get_customerread == true){
            sf_customerread.setChecked(true);
            txtcustomerread.setText("فعال است");
        }

        sf_fingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(User_Info.fingerprint,sf_fingerprint.isChecked()).apply();
                    txtfingerprint.setText("فعال است");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(User_Info.fingerprint, sf_fingerprint.isChecked()).apply();
                    txtfingerprint.setText("غیرفعال است");
                }
            }
        });

        sf_customerread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_read,sf_customerread.isChecked()).apply();
                    txtcustomerread.setText("فعال است");
                    FirebaseMessaging.getInstance().subscribeToTopic("it_customer_read").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "subscribe To Topic it_customer_read finish");
                        }
                    });
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_read, sf_customerread.isChecked()).apply();
                    txtcustomerread.setText("غیرفعال است");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("it_customer_read").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "unsubscribe To Topic it_customer_read finish");
                        }
                    });
                }
            }
        });
    }

    public void it_settings_back_click (View v) {
        finish();
    }

    public void it_settings_changepass_click (View v) {
        Intent i = new Intent(IT_Settings.this,Change_User_Password.class);
        startActivity(i);
    }

}
