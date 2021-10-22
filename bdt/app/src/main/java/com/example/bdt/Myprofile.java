package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class Myprofile extends AppCompatActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        TextView tvName = findViewById(R.id.tvNameP);
        TextView tvEmail = findViewById(R.id.tvEmailP);
        TextView tvAge = findViewById(R.id.tvAgeP);
        TextView tvColonia = findViewById(R.id.tvColoniaP);
        TextView tvMunicipio = findViewById(R.id.tvMunicipioP);

        // Crear request queue
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());



        //******************************************************************************
        /// GET REQUEST

        ///////////////////////////////////////////////////////////////////////////////////


        // 4. Definir la URL y el Metodo
        String url = "https://api-bdt.azurewebsites.net/usuario/carlos88@gmail.com";
        int method = Request.Method.GET;

        // 5. Crear la llamada
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url,null, new Response.Listener<JSONObject>() {

                    // RESULTADO EN CASO DE EXITO! (si todo sale bien esto se ejecuta) Codigo es bueno (200, 201)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // 6. Redeclarar componentes graficos


                            // 7. Hacer con la respuesta
                            // 9. Hacemos algo con la respuesta
                            tvName.setText(response.getString("nombre"));
                            tvAge.setText(response.getString("edad"));
                            tvColonia.setText(response.getString("colonia"));
                            tvEmail.setText(response.getString("correo"));
                            tvMunicipio.setText(response.getString("municipio"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }, new Response.ErrorListener() {

                    // RESULTADO EN CASO DE ERROR! Codigo es malo (404, 409, 503, 550)
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // 6. Sacar el codigo
                        String statusCode = String.valueOf(error.networkResponse.statusCode);

                        //7. Sacar la respuesta
                        String response = new String(error.networkResponse.data);
                        JSONObject json = null;
                        try {

                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
        // Executar la llamada (Enviar)
        requestQueue.add(jsonObjectRequest);


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
        //Intent intent = new Intent(this, checkout.class);
        //startActivity(intent);
    }
    public void goToSellings(View view) {
        Intent intent = new Intent(this, sellings.class);
        startActivity(intent);
    }
    public void goToScan(View view) {
        Intent intent = new Intent(this, scan.class);
        startActivity(intent);
    }


}