package com.example.bdt;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class QR extends AppCompatActivity {

    // Image attr ***************

    private static String file_url = "https://api.qrserver.com/v1/create-qr-code/?data=jhgjhfgncoDeTiempo&size=100x100";
    private ImageView img;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    // **************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.println(Log.ASSERT, "ouou", "Permission granted!");

                // initialize image loader before using
                imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

                // initialize imageview from activity_main.xml
                img = findViewById(R.id.imgQR);

                // with below method we are setting display option for our image..
                options = new DisplayImageOptions.Builder()

                        // stub image will display when your image is loading
                        .showStubImage(R.drawable.ic_launcher_foreground)

                        // below image will be displayed when the image url is empty
                        .showImageForEmptyUri(R.drawable.ic_launcher_background)

                        // cachememory method will caches the image in users external storage
                        .cacheInMemory()

                        // cache on disc will caches the image in users internal storage
                        .cacheOnDisc()

                        // build will build the view for displaying image..
                        .build();
                // below method will display image inside our image view..
                imageLoader.displayImage(file_url, img, options, null);
            }
        });
    }

    public void goToShoppingCart(View view) {
        Intent intent = new Intent(this, shoppingCart.class);
        startActivity(intent);
    }


}
