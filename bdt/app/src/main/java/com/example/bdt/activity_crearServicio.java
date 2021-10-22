package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_crearServicio extends AppCompatActivity {
    private RequestQueue requestQueue;
    private JSONObject responseUsuario;
    private ViewSwitcher vs;
    private View vInfo;
    private View vFace;
    private View vDocs;
    private EditText etContacEmail;
    private EditText etCategory;
    private EditText etActivityName;
    private EditText etDescription;
    private EditText ethorary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_servicio);

        etContacEmail = findViewById(R.id.etContacEmail);
        etCategory = findViewById(R.id.etCategory);
        etActivityName = findViewById(R.id.etActivityName);
        etDescription = findViewById(R.id.etDescription);
        ethorary = findViewById(R.id.ethorary);

    }

    public void crearServicio(View view){
        EditText etContacEmail = findViewById(R.id.etContacEmail);
        EditText etCategory = findViewById(R.id.etCategory);
        EditText etActivityName = findViewById(R.id.etCategory);
        EditText etDescription = findViewById(R.id.etDescription);
        EditText ethorary = findViewById(R.id.ethorary);


        Map<String, String> postParam= new HashMap<String, String>();

        postParam.put("correo", etContacEmail.getText().toString());
        postParam.put("categoria", etCategory.getText().toString());
        postParam.put("descripcion", etDescription.getText().toString());
        postParam.put("horario", ethorary.getText().toString());
        postParam.put("nombre", etActivityName.getText().toString());

        JSONObject jsonBody2 = new JSONObject(postParam);
        Log.println(Log.ERROR, "Tag2", String.valueOf(jsonBody2));

        String url = "https://api-bdt.azurewebsites.net/crearServicio";
        int method = Request.Method.POST;




    }
}