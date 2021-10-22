package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class preLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

    }
    public void entrarApp(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    public void signUp(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}