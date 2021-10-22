package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class FaceScan extends AppCompatActivity {
    int REQUEST_IMAGE_CAPTURE = 101;
    int REQUEST_IMAGE_WOW = 202;
    int REQUEST_IMAGE_EYE = 303;
    String correo;
    String contrasena;
    String nombre;
    String edad;
    String municipio;
    String colonia;
    String celular;
    String genero;
    String mCurrentPhotoPath;
    String img1_id, img2_id, img3_id, img4_id;
    String endpoint;
    ImageView ivFrame2, ivFrame3, ivFrame4;
    BlobContainerClient blobContainerClient;
    BlobServiceClient storageClient;
    RequestQueue requestQueue;
    TextView tvWarning222;
    Button btnGoImagePicker, btnGoArchivos, btnSendCaras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

        // Crear request queue
        requestQueue = Volley.newRequestQueue(this);

        // 1. Get previous data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            correo = extras.getString("correo");
            contrasena = extras.getString("contrasena");
            municipio = extras.getString("municipio");
            edad = extras.getString("edad");
            nombre = extras.getString("nombre");
            colonia = extras.getString("colonia");
            genero = extras.getString("genero");
            celular = extras.getString("celular");
            img1_id = extras.getString("img1_id");
        }
        ivFrame2 = findViewById(R.id.ivFrame2);
        ivFrame3 = findViewById(R.id.ivFrame3);
        ivFrame4 = findViewById(R.id.ivFrame4);
        tvWarning222 = findViewById(R.id.tvWarning222);

        btnSendCaras = findViewById(R.id.btnSendCaras);
        btnGoArchivos = findViewById(R.id.btnGoArchivos);
        btnGoImagePicker = findViewById(R.id.btnGoImagePicker);

        btnGoImagePicker.setVisibility(View.INVISIBLE);
        btnGoArchivos.setVisibility(View.INVISIBLE);


        // Connect to Azure Blob Storage
        String accountName = "filesmanager070901";
        String accountKey = "kxa6Kpkc4SgIvj0VgVtk1rHpYHPlLls+g30SSEpW4qky8LTVcwxukAaAQahACHCiGXQIZ+49MmyY52r1PeuUQA==";

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        /*
         * From the Azure portal, get your Storage account blob service URL endpoint.
         * The URL typically looks like this:
         /*
         */

        endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net/", accountName);
        Log.println(Log.ERROR, "endpoint", endpoint);
        storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential).buildClient();

        blobContainerClient = storageClient.getBlobContainerClient("imagenes/Cara");



        String[] permissions = {Manifest.permission.CAMERA};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        return;
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(FaceScan.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });



    }

    public void dispatchWOW(View view) {
        String[] permissions = {Manifest.permission.CAMERA};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        return;
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(FaceScan.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_WOW);
                    }
                }

            }
        });

    }

    public void dispatchEYE(View view) {
        String[] permissions = {Manifest.permission.CAMERA};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        return;
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(FaceScan.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_EYE);
                    }
                }

            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            Log.println(Log.ERROR, "PATH", mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            // Generate UUID
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();

            // Upload to Azure
            img2_id = "PLANO-"+uuidAsString+System.currentTimeMillis()+".jpg";
            BlobClient blobClient = blobContainerClient.getBlobClient(img2_id);
            blobClient.uploadFromFile(file.getPath());

            Log.println(Log.ERROR, "URL 2", endpoint + img2_id);

            try {
                InputStream ims = new FileInputStream(file);
                ivFrame2.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(FaceScan.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            Log.println(Log.ERROR, "UPLOADED!", "yeees!");
        }
        if (requestCode == REQUEST_IMAGE_WOW && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            Log.println(Log.ERROR, "PATH", mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            // Generate UUID
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();
            img3_id = "WOW-"+uuidAsString+System.currentTimeMillis()+".jpg";
            Log.println(Log.ERROR, "URL 3", endpoint + img3_id);

            // Upload to Azure
            BlobClient blobClient = blobContainerClient.getBlobClient(img3_id);
            blobClient.uploadFromFile(file.getPath());

            try {
                InputStream ims = new FileInputStream(file);
                ivFrame3.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(FaceScan.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            Log.println(Log.ERROR, "UPLOADED!", "yeees!");
        }
        if (requestCode == REQUEST_IMAGE_EYE && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            Log.println(Log.ERROR, "PATH", mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            // Generate UUID
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();
            img4_id = "EYE-"+uuidAsString+System.currentTimeMillis()+".jpg";
            Log.println(Log.ERROR, "URL 4", endpoint + img4_id);

            // Upload to Azure
            BlobClient blobClient = blobContainerClient.getBlobClient(img4_id);
            blobClient.uploadFromFile(file.getPath());


            try {
                InputStream ims = new FileInputStream(file);
                ivFrame4.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(FaceScan.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            Log.println(Log.ERROR, "UPLOADED!", "yeees!");
        }
    }

    public void sendCaras(View view) {
        btnSendCaras.setVisibility(View.INVISIBLE);
        // 2. Crear BODY JSON
        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("correo",correo);
        postParam.put("contrasena", contrasena);
        postParam.put("nombre", nombre);
        postParam.put("edad", Integer.valueOf(edad));
        postParam.put("municipio", municipio);
        postParam.put("colonia", colonia);
        postParam.put("genero", genero);
        postParam.put("celular", celular);
        postParam.put("imagen1_id", img1_id);
        postParam.put("imagen2_id", img2_id);
        postParam.put("imagen3_id", img3_id);
        postParam.put("imagen4_id", img4_id);

        // 3. Convertirlo a JSONObject
        JSONObject jsonBody2 = new JSONObject(postParam);
        //Log.println(Log.ERROR, "Tag2", String.valueOf(jsonBody2));

        // 4. Definir la URL y el Metodo
        String url = "https://api-bdt.azurewebsites.net/registro";
        int method = Request.Method.POST;

        // 5. Crear la llamada
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url,jsonBody2, new Response.Listener<JSONObject>() {

                    // RESULTADO EN CASO DE EXITO! (si todo sale bien esto se ejecuta) Codigo es bueno (200, 201)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            btnGoArchivos.setVisibility(View.VISIBLE);

                            tvWarning222.setText(response.getString("mensaje"));


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

                        btnGoImagePicker.setVisibility(View.VISIBLE);
                        tvWarning222.setText(response);

                    }
                });

        // Add the realibility on the connection.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1.0f));
        // Executar la llamada (Enviar)
        requestQueue.add(jsonObjectRequest);

    }

    public void goToArchivos(View view) {
        Intent i = new Intent(this, Archivos.class);
        // Send current parameters
        i.putExtra("correo", correo);
        i.putExtra("contrasena", contrasena);
        i.putExtra("nombre", nombre);
        i.putExtra("edad", edad);
        i.putExtra("municipio", municipio);
        i.putExtra("colonia", colonia);
        startActivity(i);
    }
}