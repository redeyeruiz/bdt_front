package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class balance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
    }
    public void goToMain(View view) {
        // Intent intent = new Intent(this, Main.class);
        // startActivity(intent);
    }
    public void goToBalance(View view) {
        Intent intent = new Intent(this, balance.class);
        startActivity(intent);
    }
    public void goToCheckout(View view) {
        // Intent intent = new Intent(this, Main.class);
        // startActivity(intent);
    }
    public void goToMovs(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }
}