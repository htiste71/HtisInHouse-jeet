package com.htistelecom.htisinhouse.activity.WFMS.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htistelecom.htisinhouse.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CommonFunctions {

    private ProgressDialog dialog;
    private String TAG;
    Context _context;


    Boolean _permisson = false;
    String mCurrentPhotoPath;

    public Boolean openSettingsDialog(Context context, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                Activity activity = (Activity) context;

                activity.startActivityForResult(intent, requestCode);
                _permisson = true;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                _permisson = false;
            }
        });
        builder.show();
        return _permisson;
    }

    public boolean permissioncheck(Context context, int requestCode) {
        _permisson = false;
        Activity activity = (Activity) context;
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // _permisson=true;
                        if (report.areAllPermissionsGranted()) {
                            _permisson = true;
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            _permisson = false;//openSettingsDialog(context,requestCode);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        _permisson = false;
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(activity, "Some Error! ", Toast.LENGTH_SHORT).show();
                        _permisson = false;
                    }
                })
                .onSameThread()
                .check();
        return _permisson;
    }

    String msg;

    public String open_camera(Context context, int request_code) {
        msg = "";
        if (permissioncheck(context, request_code)) {
            msg = _open_camera(context, request_code);
        } else {
            //go to check permission
            _permisson = openSettingsDialog(context, request_code);
            if (_permisson) {
                msg = _open_camera(context, request_code);
            } else {
                msg = "Error: permissino denied";
            }
        }
        return msg;
    }

    private String _open_camera(Context context, int request_code) {
        Activity activity = (Activity) context;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile("temp_capture");
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (IOException ex) {
                Log.i(TAG, "IOException");
            }

            if (photoFile != null) {
                try {
                    Uri imageUri = FileProvider.getUriForFile(
                            context,
                            "com.htistelecom.htisinhouse.provider", //(use your app signature + ".provider" )
                            photoFile);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
            try {
                activity.startActivityForResult(cameraIntent, request_code);
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return mCurrentPhotoPath;
    }

    public String open_gallery(Context context, int request_code) {
        msg = "";
        if (permissioncheck(context, request_code)) {
            msg = _open_gallery(context, request_code);
        } else {
            //go to check permission
            _permisson = openSettingsDialog(context, request_code);
            if (_permisson) {
                msg = _open_gallery(context, request_code);
            } else {
                msg = "Error: permissino denied";
            }
        }
        return msg;
    }

    private String _open_gallery(Context context, int request_code) {
        Activity activity = (Activity) context;
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        try {
            activity.startActivityForResult(chooserIntent, request_code);
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return mCurrentPhotoPath;
    }

   /* public String getFile(Context context)
    {
        String mCurrentPhotoPath="";
        Bitmap photo = null;
        String fName=  compressImage(mCurrentPhotoPath,context);
        fName="file:" +fName;
        try {
            photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(fName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath=fName;
        return mCurrentPhotoPath;
    }*/

    public static File createImageFile(String folderName) throws IOException {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + folderName + "/");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis());
        File image = File.createTempFile(
                mImageName,  // prefix
                ".jpg",         // suffix
                mediaStorageDir      // directory
        );

        return image;
    }

    public Bitmap getBitMapFromPath(Context conext, String path) {
        Bitmap photo = null;
        //mCurrentPhotoPath="file:" +mCurrentPhotoPath;
        try {
            //photo = MediaStore.Images.Media.getBitmap(conext.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            photo = handleSamplingAndRotationBitmap(conext, Uri.parse(mCurrentPhotoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;


            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public void openCropActivity(Context context, Uri sourceUri, Uri destinationUri, Fragment fragment) {
        Activity activity = (Activity) context;
        File photoFile = null;
        try {
            photoFile = createImageFile("");
            mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
        } catch (IOException ex) {
            Log.i(TAG, "IOException");
        }
        destinationUri = Uri.fromFile(photoFile);
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(context, R.color.colorAccent));
        if (fragment != null) {
            UCrop.of(sourceUri, destinationUri)
                    .withMaxResultSize(512, 512)
                    .withAspectRatio(1, 1)
                    .start(activity, fragment, UCrop.REQUEST_CROP);
        } else {
            UCrop.of(sourceUri, destinationUri)
                    .withMaxResultSize(512, 512)
                    .withAspectRatio(1, 1)
                    .start(activity, UCrop.REQUEST_CROP);
        }
    }




    void callActivityMethod(Object obj, Boolean isError, String ApiResponseMethod) {
        Activity activity = (Activity) _context;
        try {

            Method method = activity.getClass().getDeclaredMethod(ApiResponseMethod, Object.class, Boolean.class);
            method.invoke(activity, obj, isError);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //activity.ApiResponse(obj);
    }


    Object res;
    JSONObject jsonObject;

    public Object formatApiResponse(Object obj, Boolean error) throws JSONException {
        if (error) {
            res = "Error";
            return res;
        }

        jsonObject = new JSONObject(obj.toString());
        res = jsonObject.get("Success");

        if ((Boolean) res == true) {
            res = jsonObject.get("Response");
            //ArrayList<Object> logs = new Gson().fromJson(res.toString(), new TypeToken<ArrayList<Object>>(){}.getType());
            res = new Gson().fromJson(res.toString(), new TypeToken<ArrayList<Object>>() {
            }.getType());
        } else {
            res = "Error";
        }
        return res;
    }


    public double distBetweenLatLng(Double lat1, Double lng1, Double lat2, Double lng2) {
        try {
            if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
                return 0.0;
            }

            Location startPoint = new Location("locationA");
            startPoint.setLatitude(lat1);
            startPoint.setLongitude(lng1);

            Location endPoint = new Location("locationA");
            endPoint.setLatitude(lat2);
            endPoint.setLongitude(lng2);

            return startPoint.distanceTo(endPoint);


        } catch (Exception e) {
            Log.e("SrvTracking", e.getLocalizedMessage());

        }
        return 0.0;
    }


    public static boolean getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date date = new Date();
        Date mDate = null, morningTime = null, eveningTime = null;
        String mCurrentDate = formatter.format(date);

        Calendar calCurrentTime = Calendar.getInstance();
        int mHour = Integer.parseInt(mCurrentDate.split(":")[0]);
        int mMin = Integer.parseInt(mCurrentDate.split(":")[1]);


        calCurrentTime.set(Calendar.HOUR_OF_DAY, mHour);
        calCurrentTime.set(Calendar.MINUTE, mMin);


        try {
            mDate = formatter.parse(formatter.format(calCurrentTime.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 30);

        try {
            morningTime = formatter.parse(formatter.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar1.set(Calendar.HOUR_OF_DAY, 18);
        calendar1.set(Calendar.MINUTE, 30);
        try {
            eveningTime = formatter.parse(formatter.format(calendar1.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (mDate.before(morningTime) || mDate.after(eveningTime)) {
            return false;
        } else {
            return true;
        }


    }


}