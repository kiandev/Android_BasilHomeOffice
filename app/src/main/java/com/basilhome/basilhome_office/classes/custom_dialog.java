package com.basilhome.basilhome_office.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.basilhome.basilhome_office.R;

public class custom_dialog extends AlertDialog {

    private String title, message;

    public custom_dialog(Context context) {
        super(context);
    }

    protected custom_dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        TextView title_text = findViewById(R.id.custom_dialog_title);
        TextView message_text = findViewById(R.id.custom_dialog_message);
        Button btnok = findViewById(R.id.custom_dialog_btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        title_text.setText(getTitle());
        message_text.setText(getMessage());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
