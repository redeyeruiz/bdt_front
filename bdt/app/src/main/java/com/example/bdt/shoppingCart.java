package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class shoppingCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
    }
    public void goBack(View view) {
        //Intent intent = new Intent(this, checkout.class);
        //startActivity(intent);
    }
    public void goToBalance(View view) {
        Intent intent = new Intent(this, balance.class);
        startActivity(intent);
    }
    public void goToMovs(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }
    public void goToConfig(View view) {
        Intent intent = new Intent(this, Myprofile.class);
        startActivity(intent);
    }
    public void goToSellings(View view) {
        Intent intent = new Intent(this, sellings.class);
        startActivity(intent);
    }
    public void goToScan(View view) {
        Intent intent = new Intent(this, scan.class);
        startActivity(intent);
    }
    public void goToFilter(View view) {
        Intent intent = new Intent(this, Filtros.class);
        startActivity(intent);
    }


}