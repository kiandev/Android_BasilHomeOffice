package com.basilhome.basilhome_office.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.basilhome.basilhome_office.R;
import com.basilhome.basilhome_office.classes.InternetTest;

import java.text.DecimalFormat;


public class Get_Num_Search_Price extends AppCompatActivity {

    TextInputEditText number;
    Spinner spinner;
    String vahed_kol = "همه واحد ها";
    String vahed_0 = "0 + 1";
    String vahed_1 = "1 + 1";
    String vahed_2 = "2 + 1";
    String vahed_3 = "3 + 1";
    String vahed_4 = "4 + 1";
    String vahed_5 = "5 + 1";
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_num_search_price);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        spinner = findViewById(R.id.get_num_searchprice_spin);

        String[] items = new String[]{vahed_kol, vahed_0, vahed_1, vahed_2, vahed_3, vahed_4, vahed_5};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn);
        spinner.setAdapter(adapter);

        number = findViewById(R.id.get_num_searchprice_text);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!input.isEmpty()) {
                    input = input.replace(",", "");
                    DecimalFormat format = new DecimalFormat("#,###,###");
                    String newPrice = format.format(Double.parseDouble(input));
                    number.removeTextChangedListener(this); //To Prevent from Infinite Loop
                    number.setText(newPrice);
                    number.setSelection(newPrice.length()); //Move Cursor to end of String
                    number.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
            }
        });

    }

    public void get_num_searchprice_btnclick (View v){
        if (!InternetTest.ok(this.getApplicationContext())) {
            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (number.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "لطفا میزان بودجه را وارد نمایید", Toast.LENGTH_SHORT).show();
            }
        }
        String input = number.getText().toString();
        input = input.replace(",", "");
        Intent intent = new Intent(Get_Num_Search_Price.this,Project_Search_Price.class);
        intent.putExtra("price",input);
        intent.putExtra("filter",spinner.getSelectedItem().toString());
        startActivity(intent);
//        finish();
    }

}
