package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Archivos extends AppCompatActivity {
    RequestQueue requestQueue;
    Handler handler = new Handler();
    TextView tvRegistroResultado;
    int INE_CODE = 22;
    int CARTA_CODE = 33;
    int ACTA_CODE = 44;
    int CURP_CODE = 55;
    TextView tvINE;
    TextView tvACTA;
    TextView tvCURP;
    TextView tvCARTA;
    String correo;
    String contrasena;
    String nombre;
    String edad;
    String municipio;
    String colonia;
    String imagen1b64;
    String imagen2b64;
    String INE_b64;
    String CURP_b64;
    String ACTA_b64;
    String CARTA_b64;
    BlobServiceClient storageClient;
    BlobContainerClient blobContainerClient;
    String endpoint;

    final Runnable r = new Runnable() {
        public void run() {
            goLogin();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);


        // Crear request queue
        requestQueue = Volley.newRequestQueue(this);

        tvINE = findViewById(R.id.tvINE);
        tvACTA = findViewById(R.id.tvACTA);
        tvCURP = findViewById(R.id.tvCURP);
        tvCARTA = findViewById(R.id.tvCARTA);
        tvRegistroResultado = findViewById(R.id.tvRegistroResultado);


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


    }

    public void chooseINE(View view) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.putExtra("browseCoa", itemToBrowse);
                Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                try {
                    startActivityForResult(chooser, INE_CODE);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                }

            }
        });
    }

    public void chooseCURP(View view) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.putExtra("browseCoa", itemToBrowse);
                Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                try {
                    startActivityForResult(chooser, CURP_CODE);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                }

            }
        });
    }

    public void chooseACTA(View view) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.putExtra("browseCoa", itemToBrowse);
                Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                try {
                    startActivityForResult(chooser, ACTA_CODE);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                }

            }
        });
    }

    public void chooseCARTA(View view) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.putExtra("browseCoa", itemToBrowse);
                Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                try {
                    startActivityForResult(chooser, CARTA_CODE);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                }

            }
        });
    }

    public void goLogin() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INE_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String mimeType = getContentResolver().getType(uri);
                    String filename;
                    if (mimeType == null) {
                        String path = getPath(this, uri);
                        File file = new File(path);
                        filename = file.getName();
                    } else {
                        Uri returnUri = data.getData();
                        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        filename = returnCursor.getString(nameIndex);
                        String size = Long.toString(returnCursor.getLong(sizeIndex));
                    }
                    File fileSave = getExternalFilesDir(null);
                    String sourcePath = getExternalFilesDir(null).toString();
                    try {
                        copyFileStream(new File(sourcePath + "/" + filename), uri,this);
                        Log.d("PATH", sourcePath + "/" + filename);
                        String fileName = "file://" + sourcePath + "/" + filename;

                        // Generate UUID
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();

                        // Upload to Azure
                        blobContainerClient = storageClient.getBlobContainerClient("documentos/INE");

                        String id = uuidAsString+System.currentTimeMillis()+".pdf";
                        BlobClient blobClient = blobContainerClient.getBlobClient(id);
                        blobClient.uploadFromFile(sourcePath + "/" + filename);

                        Log.println(Log.ERROR, "URL INE", endpoint + "documentos/INE/" + id);

                        tvINE.setText(filename);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CARTA_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String mimeType = getContentResolver().getType(uri);
                    String filename;
                    if (mimeType == null) {
                        String path = getPath(this, uri);
                        File file = new File(path);
                        filename = file.getName();
                    } else {
                        Uri returnUri = data.getData();
                        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        filename = returnCursor.getString(nameIndex);
                        String size = Long.toString(returnCursor.getLong(sizeIndex));
                    }
                    File fileSave = getExternalFilesDir(null);
                    String sourcePath = getExternalFilesDir(null).toString();
                    try {
                        copyFileStream(new File(sourcePath + "/" + filename), uri,this);
                        Log.d("PATH", sourcePath + "/" + filename);
                        String fileName = "file://" + sourcePath + "/" + filename;

                        // Generate UUID
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();

                        // Upload to Azure
                        blobContainerClient = storageClient.getBlobContainerClient("documentos/Carta");

                        String id = uuidAsString+System.currentTimeMillis()+".pdf";
                        BlobClient blobClient = blobContainerClient.getBlobClient(id);
                        blobClient.uploadFromFile(sourcePath + "/" + filename);

                        String url = endpoint + "documentos/Carta/" + id;
                        Log.println(Log.ERROR, "URL Carta", url);

                        // Subir archivo a la base de datos

                        uploadDatabase("Carta", url);


                        tvCARTA.setText(filename);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == ACTA_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String mimeType = getContentResolver().getType(uri);
                    String filename;
                    if (mimeType == null) {
                        String path = getPath(this, uri);
                        File file = new File(path);
                        filename = file.getName();
                    } else {
                        Uri returnUri = data.getData();
                        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        filename = returnCursor.getString(nameIndex);
                        String size = Long.toString(returnCursor.getLong(sizeIndex));
                    }
                    File fileSave = getExternalFilesDir(null);
                    String sourcePath = getExternalFilesDir(null).toString();
                    try {
                        copyFileStream(new File(sourcePath + "/" + filename), uri,this);
                        Log.d("PATH", sourcePath + "/" + filename);
                        String fileName = "file://" + sourcePath + "/" + filename;

                        // Generate UUID
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();

                        // Upload to Azure
                        blobContainerClient = storageClient.getBlobContainerClient("documentos/Acta");

                        String id = uuidAsString+System.currentTimeMillis()+".pdf";
                        BlobClient blobClient = blobContainerClient.getBlobClient(id);
                        blobClient.uploadFromFile(sourcePath + "/" + filename);

                        Log.println(Log.ERROR, "URL Acta", endpoint + "documentos/Acta/" + id);

                        tvACTA.setText(filename);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CURP_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String mimeType = getContentResolver().getType(uri);
                    String filename;
                    if (mimeType == null) {
                        String path = getPath(this, uri);
                        File file = new File(path);
                        filename = file.getName();
                    } else {
                        Uri returnUri = data.getData();
                        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        filename = returnCursor.getString(nameIndex);
                        String size = Long.toString(returnCursor.getLong(sizeIndex));
                    }
                    File fileSave = getExternalFilesDir(null);
                    String sourcePath = getExternalFilesDir(null).toString();
                    try {
                        copyFileStream(new File(sourcePath + "/" + filename), uri,this);
                        Log.d("PATH", sourcePath + "/" + filename);
                        String fileName = "file://" + sourcePath + "/" + filename;

                        // Generate UUID
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();

                        // Upload to Azure
                        blobContainerClient = storageClient.getBlobContainerClient("documentos/CURP");

                        String id = uuidAsString+System.currentTimeMillis()+".pdf";
                        BlobClient blobClient = blobContainerClient.getBlobClient(id);
                        blobClient.uploadFromFile(sourcePath + "/" + filename);

                        Log.println(Log.ERROR, "URL CURP", endpoint + "documentos/CURP/" + id);

                        tvCURP.setText(filename);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }
    public static String getPath(Context context, Uri uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes,Base64.DEFAULT);
        return encoded;
    }
    public void goConfirmedScreen(View view) {
        Intent i = new Intent(this, completarConfirmacion.class);
        // Send current parameters
        i.putExtra("textBtn", "Bienvenido!");
        i.putExtra("textTitle", "Wuuuu!");
        i.putExtra("textDetails", "Hemos recibido tu información, asegúrate de enviar todos los documentos, ha iniciado tu proceso de admisión, si subiste todos tus docs tendrás una respuesta en los próximos días.");
        startActivity(i);
    }

    private void uploadDatabase(String tipo, String url_file) {
        // 2. Crear BODY JSON
        Map<String, Object> postParam= new HashMap<String, Object>();

        postParam.put("correo",correo);
        postParam.put("tipo",tipo);
        postParam.put("url", url_file);

        // 3. Convertirlo a JSONObject
        JSONObject jsonBody2 = new JSONObject(postParam);
        //Log.println(Log.ERROR, "Tag2", String.valueOf(jsonBody2));

        // 4. Definir la URL y el Metodo
        String url = "https://api-bdt.azurewebsites.net/subirDoc";
        int method = Request.Method.POST;

        // 5. Crear la llamada
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url,jsonBody2, new Response.Listener<JSONObject>() {

                    // RESULTADO EN CASO DE EXITO! (si todo sale bien esto se ejecuta) Codigo es bueno (200, 201)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.println(Log.ERROR, "ARCHIVO UPLOADED", response.getString("mensaje"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }, new Response.ErrorListener() {

                    // RESULTADO EN CASO DE ERROR! Codigo es malo (404, 409, 503, 550)
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR, "ARCHIVO NOT UPLOADED", ":(");
                    }
                });

        // Add the realibility on the connection.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        // Executar la llamada (Enviar)
        requestQueue.add(jsonObjectRequest);
    }
}