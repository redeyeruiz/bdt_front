package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;

public class ImagePicker extends AppCompatActivity {
    String correo;
    String contrasena;
    String nombre;
    String edad;
    String municipio;
    String colonia;
    String celular;
    String genero;
    String img1_id;
    String endpoint;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    TextView tvWarning;
    ImageView ivFrame;
    Button btnContinue;
    int FILE_SELECT_CODE = 111;
    BlobServiceClient storageClient;
    BlobContainerClient blobContainerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

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
        }

        // 2. Initialize image loader before using
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this.getApplicationContext()));
        ivFrame = findViewById(R.id.ivFrame);
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setVisibility(View.INVISIBLE);
        tvWarning = findViewById(R.id.tvWarning);
        tvWarning.setVisibility(View.INVISIBLE);


        // 3. Initialize image frame
        CircularImageView circularImageView = findViewById(R.id.ivFrame);

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

    }

    public void LoadImage(View view) {
        // with below method we are setting display option for our image..
        options = new DisplayImageOptions.Builder()

                // stub image will display when your image is loading
                .showStubImage(R.drawable.ic_launcher_foreground)

                // below image will be displayed when the image url is empty
                .showImageForEmptyUri(R.drawable.default_profile)

                // cachememory method will caches the image in users external storage
                .cacheInMemory()

                // cache on disc will caches the image in users internal storage
                .cacheOnDisc()

                // build will build the view for displaying image..
                .build();
        // below method will display image inside our image view..

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.putExtra("browseCoa", itemToBrowse);
                Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                try {
                    //startActivityForResult(chooser, FILE_SELECT_CODE);
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String mimeType = getContentResolver().getType(uri);
                    String filename;
                    if (mimeType == null) {
                        String path = getPath(this, uri);
                        File file = new File(path);
                        filename = file.getName();
                    }
                    else {
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
                    copyFileStream(new File(sourcePath + "/" + filename), uri,this);


                    Log.d("PATH", sourcePath + "/" + filename);
                    String fileName = "file://" + sourcePath + "/" + filename;
                    LoadImage("file://" + sourcePath + "/" + filename);

                    // Generate UUID
                    UUID uuid = UUID.randomUUID();
                    String uuidAsString = uuid.toString();

                    // Upload to Azure
                    img1_id = "PERFIL-"+uuidAsString+System.currentTimeMillis()+".jpg";
                    BlobClient blobClient = blobContainerClient.getBlobClient(img1_id);
                    blobClient.uploadFromFile(sourcePath + "/" + filename);

                    Log.println(Log.ERROR, "URL 1", endpoint + img1_id);

                    btnContinue.setVisibility(View.VISIBLE);
                    tvWarning.setVisibility(View.VISIBLE);

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

    public void LoadImage(String path) {
        imageLoader.displayImage(path, ivFrame, options, null);
    }

    public void goToFaceScan(View view) {
        Intent i = new Intent(this, FaceScan.class);
        // Send current parameters
        i.putExtra("correo", correo);
        i.putExtra("contrasena", contrasena);
        i.putExtra("nombre", nombre);
        i.putExtra("edad", edad);
        i.putExtra("municipio", municipio);
        i.putExtra("colonia", colonia);
        i.putExtra("img1_id", img1_id);
        i.putExtra("genero", genero);
        i.putExtra("celular", celular);
        startActivity(i);
    }
}