package com.htistelecom.htisinhouse.activity.WFMS.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import com.htistelecom.htisinhouse.BuildConfig;
import com.htistelecom.htisinhouse.utilities.Utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CameraActivity extends Activity {
    //for image upload
    File myDirectory;
    String selectedImagePath;
    private Uri muri;

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    String timeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime());

        initViews();
        openDialog();
        Intent intent = getIntent();
    }

    private void initViews() {
        myDirectory = new File(Environment.getExternalStorageDirectory(), "HTIS");
        try {
            if (myDirectory.exists()) {
            } else {
                myDirectory.mkdir();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                galleryIntent();
                                break;
                            case 1:
                                cameraIntent();
                        }
                    }
                });
        pictureDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        });
        pictureDialog.show();
    }

    private void galleryIntent() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, SELECT_FILE);
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String image_name = timeStamp + ".jpeg";

            File photoUploadFile = new File(myDirectory, image_name);
            muri = Uri.fromFile(photoUploadFile);
            selectedImagePath = muri.getPath();
            Uri photoURI = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoUploadFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI);
        } else {
            String image_name = timeStamp;

            File photoUploadFile = new File(myDirectory, image_name);
            muri = Uri.fromFile(photoUploadFile);
            selectedImagePath = muri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, muri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                try {
                    Uri uri = data.getData();
                    String filePath = Utilities.compressImage(this, uri.toString(), timeStamp + ".jpeg");
                    Intent intent = new Intent();
                    intent.putExtra("file", filePath);
                    setResult(RESULT_OK, intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA) {
                Uri uri = Uri.parse(selectedImagePath);

                String filePath = Utilities.compressImage(this, uri.toString(), timeStamp + ".jpeg");
                Intent intent = new Intent();
                intent.putExtra("file", filePath);
                setResult(RESULT_OK, intent);
                finish();
            }

        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
