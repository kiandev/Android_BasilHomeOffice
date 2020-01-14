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

public class Admin_Settings extends AppCompatActivity {

    SwitchCompat sf_advisor_report, sf_customer_note,sf_customer_plus, sf_customer_read, sf_project_note, sf_fingerprint;
    private Boolean get_advisor_report, get_customer_note,get_customer_plus, get_customer_read, get_project_note, get_fingerprint;
    TextView txtadvisor_report, txtcustomer_note, txtproject_note, txtcustomer_plus, txtcustomer_read, txtfingerprint;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_admin_settings);
        sf_advisor_report = findViewById(R.id.admin_settings_switchofadvisorreport);
        sf_customer_note = findViewById(R.id.admin_settings_switchofcustomernote);
        sf_project_note = findViewById(R.id.admin_settings_switchofprojectnote);
        sf_fingerprint = findViewById(R.id.admin_settings_switchoffingerprint);
        sf_customer_plus = findViewById(R.id.admin_settings_switchofcustomerplus);
        sf_customer_read = findViewById(R.id.admin_settings_switchofcustomerread);

        txtadvisor_report = findViewById(R.id.admin_settings_txtadvisorreport);
        txtcustomer_note = findViewById(R.id.admin_settings_txtcustomernote);
        txtcustomer_plus = findViewById(R.id.admin_settings_txtcustomerplus);
        txtcustomer_read = findViewById(R.id.admin_settings_txtcustomerread);
        txtproject_note = findViewById(R.id.admin_settings_txtprojectnote);
        txtfingerprint = findViewById(R.id.admin_settings_txtfingerprint);

        get_advisor_report = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Topic_Info.advisor_report,false);
        get_customer_note = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Topic_Info.customer_note,false);
        get_customer_plus = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Topic_Info.customer_plus,false);
        get_customer_read = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Topic_Info.customer_admin_read,false);
        get_project_note = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Topic_Info.project_note,false);
        get_fingerprint = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(User_Info.fingerprint,false);

        if (get_fingerprint == true){
            sf_fingerprint.setChecked(true);
            txtfingerprint.setText("فعال است");
        }
        if (get_advisor_report == true){
            sf_advisor_report.setChecked(true);
            txtadvisor_report.setText("فعال است");
        }
        if (get_customer_note == true){
            sf_customer_note.setChecked(true);
            txtcustomer_note.setText("فعال است");
        }
        if (get_customer_plus == true){
            sf_customer_plus.setChecked(true);
            txtcustomer_plus.setText("فعال است");
        }
        if (get_customer_read == true){
            sf_customer_read.setChecked(true);
            txtcustomer_read.setText("فعال است");
        }
        if (get_project_note == true){
            sf_project_note.setChecked(true);
            txtproject_note.setText("فعال است");
        }
        sf_fingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(User_Info.fingerprint,sf_fingerprint.isChecked()).apply();
                    txtfingerprint.setText("فعال است");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(User_Info.fingerprint,sf_fingerprint.isChecked()).apply();
                    txtfingerprint.setText("غیرفعال است");
                }
            }
        });

        sf_advisor_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.advisor_report,sf_advisor_report.isChecked()).apply();
                    txtadvisor_report.setText("فعال است");
                    FirebaseMessaging.getInstance().subscribeToTopic("admin_advisor_report").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "subscribe To Topic admin_advisor_report finish");
                        }
                    });
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.advisor_report, sf_advisor_report.isChecked()).apply();
                    txtadvisor_report.setText("غیرفعال است");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("admin_advisor_report").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "unsubscribe To Topic admin_advisor_report finish");
                        }
                    });
                }
            }
        });

        sf_customer_note.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_note,sf_customer_note.isChecked()).apply();
                    txtcustomer_note.setText("فعال است");
                    FirebaseMessaging.getInstance().subscribeToTopic("admin_customer_note").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "subscribe To Topic admin_customer_note finish");
                        }
                    });
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_note, sf_customer_note.isChecked()).apply();
                    txtcustomer_note.setText("غیرفعال است");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("admin_customer_note").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "unsubscribe To Topic admin_customer_note finish");
                        }
                    });
                }
            }
        });

        sf_customer_plus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_plus,sf_customer_plus.isChecked()).apply();
                    txtcustomer_plus.setText("فعال است");
                    FirebaseMessaging.getInstance().subscribeToTopic("admin_customer_plus").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "subscribe To Topic admin_customer_plus finish");
                        }
                    });
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_plus, sf_customer_plus.isChecked()).apply();
                    txtcustomer_plus.setText("غیرفعال است");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("admin_customer_plus").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "unsubscribe To Topic admin_customer_plus finish");
                        }
                    });
                }
            }
        });

        sf_customer_read.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_admin_read,sf_customer_read.isChecked()).apply();
                    txtcustomer_read.setText("فعال است");
                    FirebaseMessaging.getInstance().subscribeToTopic("admin_customer_read").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "subscribe To Topic admin_customer_read finish");
                        }
                    });
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.customer_admin_read, sf_customer_read.isChecked()).apply();
                    txtcustomer_read.setText("غیرفعال است");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("admin_customer_read").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "unsubscribe To Topic admin_customer_read finish");
                        }
                    });
                }
            }
        });

        sf_project_note.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.project_note,sf_project_note.isChecked()).apply();
                    txtproject_note.setText("فعال است");
                    FirebaseMessaging.getInstance().subscribeToTopic("admin_project_note").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "subscribe To Topic admin_project_note finish");
                        }
                    });
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(Topic_Info.project_note, sf_project_note.isChecked()).apply();
                    txtproject_note.setText("غیرفعال است");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("admin_project_note").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + "unsubscribe To Topic admin_project_note finish");
                        }
                    });
                }
            }
        });
    }

    public void admin_settings_back_click (View v) {
        finish();
    }

    public void admin_settings_changepass_click (View v) {
        Intent i = new Intent(Admin_Settings.this,Change_User_Password.class);
        startActivity(i);
    }

}
