package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity {


    private RequestQueue requestQueue;
    private JSONObject responseUsuario;
    final Handler handler = new Handler();


    final Runnable r = new Runnable() {
        public void run() {
            goToHome();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tv = findViewById(R.id.tvResult);
        tv.setText("Olvidaste tu contraseña?");

        // Crear request queue
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());


    }

    public void logInUser(View view) {

        // 1. Sacar campos
        EditText etEmail = findViewById(R.id.etEmail2);
        EditText etPsw = findViewById(R.id.etPsw);


         //******************************************************************************
         /// POST REQUEST

         ///////////////////////////////////////////////////////////////////////////////////

         // 2. Crear BODY JSON
         Map<String, String> postParam= new HashMap<String, String>();

         postParam.put("correo", etEmail.getText().toString());
         postParam.put("contrasena", etPsw.getText().toString());

         // Correo exito: "carlos88@gmail.com"
         // Psw exito: 858

        // 3. Convertirlo a JSONObject
         JSONObject jsonBody2 = new JSONObject(postParam);
         Log.println(Log.ERROR, "Tag2", String.valueOf(jsonBody2));

         // 4. Definir la URL y el Metodo
         String url = "https://api-bdt.azurewebsites.net/acceso";
         int method = Request.Method.POST;

         // 5. Crear la llamada
         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
         (method, url,jsonBody2, new Response.Listener<JSONObject>() {

        // RESULTADO EN CASO DE EXITO! (si todo sale bien esto se ejecuta) Codigo es bueno (200, 201)
            @Override
            public void onResponse(JSONObject response) {
                try {

                    // 6. Redeclarar componentes graficos
                    TextView tv = findViewById(R.id.tvResult);

                    // 7. Hacer con la respuesta
                    // response es la respuesta
                    // response.getString("lo_que_quieran_sacar");

                    tv.setText(response.getString("mensaje"));

                    // 7.1 Cambiar de pantalla
                    (handler).postDelayed(r, 2000);

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
        try {
            // json es la respuesta
            // json.getString("lo_que_quieran_sacar");
            Log.println(Log.ERROR, "myTag", json.getString("mensaje"));

            // 8. Redeclarar componentes graficos
            TextView tv = findViewById(R.id.tvResult);

            // 9. Hacemos algo con la respuesta
            tv.setText(json.getString("mensaje"));
        } catch (JSONException e) {
        e.printStackTrace();
        }

        }
        });
        // Add the realibility on the connection.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
         // Executar la llamada (Enviar)
         requestQueue.add(jsonObjectRequest);

    }

    public void goToHome() {
        Intent intent = new Intent(login.this, home2.class);
        startActivity(intent);
    }

    public void goToSignUpNow(View view) {
        Intent intent = new Intent(login.this, Registro.class);
        startActivity(intent);
    }




}