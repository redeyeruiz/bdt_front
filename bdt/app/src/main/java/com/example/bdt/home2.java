package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class home2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
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
    public void goToShopping(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }
}