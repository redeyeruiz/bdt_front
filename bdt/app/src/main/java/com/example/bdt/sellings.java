package com.example.bdt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class sellings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellings);
    }
    public void goBack(View view) {
        //Intent intent = new Intent(this, checkout.class);
        //startActivity(intent);
    }
    public void goToQR(View view) {
        Intent intent = new Intent(this, QR.class);
        startActivity(intent);
    }
    public void goToBalance(View view) {
        Intent intent = new Intent(this, balance.class);
        startActivity(intent);
    }
    public void goToMovs(View view) {
        //Intent intent = new Intent(this, checkout.class);
        //startActivity(intent);
    }
    public void goToConfig(View view) {
        //Intent intent = new Intent(this, checkout.class);
        //startActivity(intent);
    }
    public void goToShopping(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }

}