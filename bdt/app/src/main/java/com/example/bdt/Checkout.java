package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }
    public void confirmarPedido(View view) {
        Intent intent = new Intent(this, ConfirmedOrder.class);
        startActivity(intent);
    }
    public void noConfirmarPedido(View view) {
        Intent intent = new Intent(this, checkoutFail.class);
        startActivity(intent);
    }
}