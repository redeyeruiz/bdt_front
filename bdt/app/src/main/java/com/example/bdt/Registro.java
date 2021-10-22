package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private RequestQueue requestQueue;
    private TextView tvError;
    private EditText etCorreo;
    private EditText etContrasena;
    private EditText etEdad;
    private EditText etNombre;
    private EditText etMunicipio;
    private EditText etColonia;
    private EditText etGenero;
    private EditText etCelular;
    private Button btnIniciaSesion;
    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // 1. Sacar campos
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        etEdad = findViewById(R.id.etEdad);
        etNombre = findViewById(R.id.etNombre);
        etMunicipio = findViewById(R.id.etMunicipio);
        etColonia = findViewById(R.id.etColonia);
        etCelular = findViewById(R.id.etCelular2);
        etGenero = findViewById(R.id.etGenero2);
        tvError = findViewById(R.id.tvError);
        btnIniciaSesion = findViewById(R.id.btnIniciaSesion);
        btnContinuar = findViewById(R.id.btnContinuar);
        tvError.setVisibility(View.INVISIBLE);
        btnIniciaSesion.setVisibility(View.INVISIBLE);


        // Crear request queue
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

    }

    public void checkUser(View view) {
        //******************************************************************************
        /// GET REQUEST

        ///////////////////////////////////////////////////////////////////////////////////


        // 4. Definir la URL y el Metodo
        String url = "https://api-bdt.azurewebsites.net/usuario/"+etCorreo.getText().toString();
        int method = Request.Method.GET;

        // 5. Crear la llamada
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url,null, new Response.Listener<JSONObject>() {

                    // RESULTADO EN CASO DE EXITO! (si todo sale bien esto se ejecuta) Codigo es bueno (200, 201)
                    @Override
                    public void onResponse(JSONObject response) {
                        tvError.setVisibility(View.VISIBLE);
                        btnIniciaSesion.setVisibility(View.VISIBLE);
                        btnContinuar.setVisibility(View.INVISIBLE);
                    }



                }, new Response.ErrorListener() {

                    // RESULTADO EN CASO DE ERROR! Codigo es malo (404, 409, 503, 550)
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        goImagePicker();
                    }
                });
        // Add the realibility on the connection.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 4, 1));
        // Executar la llamada (Enviar)
        requestQueue.add(jsonObjectRequest);



    }

    public void goImagePicker() {
        Intent i = new Intent(this, ImagePicker.class);
        // Send current parameters
        i.putExtra("correo", etCorreo.getText().toString());
        i.putExtra("contrasena", etContrasena.getText().toString());
        i.putExtra("nombre", etNombre.getText().toString());
        i.putExtra("edad", etEdad.getText().toString());
        i.putExtra("municipio", etMunicipio.getText().toString());
        i.putExtra("colonia", etColonia.getText().toString());
        i.putExtra("genero", etGenero.getText().toString());
        i.putExtra("celular", etCelular.getText().toString());

        startActivity(i);
    }


}