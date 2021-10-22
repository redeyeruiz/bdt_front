package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class completarConfirmacion extends AppCompatActivity {
    String btnText;
    String titleText;
    String detailsText;

    TextView tvConfirmTitle;
    TextView tvConfirmDetails;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_confirmacion);

        // 1. Get previous data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            btnText = extras.getString("btnText");
            titleText = extras.getString("titleText");
            detailsText = extras.getString("detailsText");
            tvConfirmTitle.setText(titleText);
            tvConfirmDetails.setText(detailsText);
            btnConfirm.setText(btnText);
        }


    }

    public void goHome(View view) {
        Intent intent = new Intent(this, home2.class);
        startActivity(intent);
    }
}