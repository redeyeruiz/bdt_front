package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class checkoutFail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_fail);
    }
    public void goToShoppingCart(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }
}