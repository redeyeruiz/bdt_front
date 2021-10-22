package com.example.bdt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Scan logic

        new IntentIntegrator(this).initiateScan();

    }
    public void goBack(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }

    public void goConfirm(View view) {
        Intent intent = new Intent(this, ConfirmedOrder.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

    }
}