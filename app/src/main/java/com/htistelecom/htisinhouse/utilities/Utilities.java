package com.htistelecom.htisinhouse.utilities;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.htistelecom.htisinhouse.R;
import com.htistelecom.htisinhouse.interfaces.GetDateTime;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.media.ExifInterface.TAG_ORIENTATION;


public class Utilities {
    static SimpleDateFormat simpleDateFormat;
    static ProgressDialog progressDialog;
    static Dialog pDialog;
    public static Geocoder geocoder;
    Context activity;


    public Utilities(Context ac) {
        activity = ac;
    }


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getCurrentDate() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        }

        Date date = new Date();

        return simpleDateFormat.format(date);


    }

    public static String getCurrentDateInMonth() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        }

        Date date = new Date();

        return simpleDateFormat.format(date);


    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static Boolean emailPatterns(String email) {
        String EmailPatterns = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email != null) {
            if (email.matches(EmailPatterns)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static void showDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public static void dismissDialog() {
        progressDialog.dismiss();
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ignored) {
            ignored.getMessage();
        }
    }


    // new dialog for hole screen
    public static void initializeAndShow(Context context) {
        initializeProgressDialog(context);
        showProgressDialog();
    }

    public static void showProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();


        } else {
            pDialog.show();
        }

    }

    public static void initializeProgressDialog(Context context) {

        try {

            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(R.layout.progress_dialog);
            Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ProgressWheel wheel = new ProgressWheel(context);
            wheel.setBarColor(Color.RED);
            pDialog.setCancelable(false);
            pDialog.show();

        } catch (Exception e) {
            Log.e("show", "" + e);
        }

    }

    public static void hideProgressDialog() {

        if (pDialog != null && pDialog.isShowing()) {
            try {
                pDialog.dismiss();
                pDialog = null;
            } catch (Exception e) {
                Log.e("hide", "" + e);
            }
        }
    }


    public static String getMonthFormat(int month) {
        // String monthName;
        switch (month) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";

        }
        return "";
    }


    public static int getMonthFromString(String month) {
        if (month.equalsIgnoreCase("jan")) {
            return 0;
        } else if (month.equalsIgnoreCase("feb")) {
            return 1;
        }
        if (month.equalsIgnoreCase("mar")) {
            return 2;
        } else if (month.equalsIgnoreCase("apr")) {
            return 3;
        }
        if (month.equalsIgnoreCase("may")) {
            return 4;
        } else if (month.equalsIgnoreCase("jun")) {
            return 5;
        }
        if (month.equalsIgnoreCase("jul")) {
            return 6;
        } else if (month.equalsIgnoreCase("aug")) {
            return 7;
        }
        if (month.equalsIgnoreCase("sep")) {
            return 8;
        } else if (month.equalsIgnoreCase("oct")) {
            return 9;
        }
        if (month.equalsIgnoreCase("nov")) {
            return 10;
        } else if (month.equalsIgnoreCase("dec")) {
            return 11;
        }

        return 0;
    }

    public static int getMonthIntoInt(String month) {
        if (month.equalsIgnoreCase("jan")) {
            return 1;
        } else if (month.equalsIgnoreCase("feb")) {
            return 2;
        }
        if (month.equalsIgnoreCase("mar")) {
            return 3;
        } else if (month.equalsIgnoreCase("apr")) {
            return 4;
        }
        if (month.equalsIgnoreCase("may")) {
            return 5;
        } else if (month.equalsIgnoreCase("jun")) {
            return 6;
        }
        if (month.equalsIgnoreCase("jul")) {
            return 7;
        } else if (month.equalsIgnoreCase("aug")) {
            return 8;
        }
        if (month.equalsIgnoreCase("sep")) {
            return 9;
        } else if (month.equalsIgnoreCase("oct")) {
            return 10;
        }
        if (month.equalsIgnoreCase("nov")) {
            return 11;
        } else if (month.equalsIgnoreCase("dec")) {
            return 12;
        }

        return 0;
    }


    public static List<Address> getAddressFromLatLong(Context context, double LATITUDE, double LONGITUDE) {
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (Geocoder.isPresent()) {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    return addresses;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void alertDialog() {
        AlertDialog.Builder Builder = new AlertDialog.Builder(activity)
                .setMessage(R.string.location_settings_desktop)
                .setTitle(R.string.location_warning)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(viewIntent);
                        dialog.dismiss();

                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                });
        Dialog dialog = Builder.create();
        dialog.show();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public static String compressImage(Context context, String imageUri, String data) {
        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);


        new File(imageUri.toString()).delete();
//        if(file.exists())
//        {
//            Log.e("Exists","yes");
//        }
//        else
//        {
//            Log.e("Exists","no");
//
//        }


        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }


            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename(data);
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename(String data) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "HTIS");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + data);
        return uriSting;

    }

    public static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static boolean existPath(String url) throws IOException {
        URL url1 = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
        huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
        huc.connect();
        int code = huc.getResponseCode();

        if (code == 200)
            return true;
        else
            return false;
    }

    public static void selectTime(Context context, GetDateTime intfc, int TYPE, String mInTime) {


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {


                        if (TYPE == 1) {
                            String[] arrayTime = mInTime.split(":");
                            int mHourInt = Integer.parseInt(arrayTime[0]);
                            int mMinutesInt = Integer.parseInt(arrayTime[1]);
                            Calendar datetime = Calendar.getInstance();
                            Calendar c = Calendar.getInstance();
                            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            datetime.set(Calendar.MINUTE, minute);

                            c.set(Calendar.HOUR_OF_DAY, mHourInt);
                            c.set(Calendar.MINUTE, mMinutesInt);

                            if (datetime.getTimeInMillis() <= c.getTimeInMillis()) {
                                Utilities.showToast(context, "Out Time should be greater than In Time");
                            } else {
                                String mHour = "";
                                String mMinutes = "";
                                if (hourOfDay <= 9)

                                    mHour = "0" + hourOfDay + "";

                                else

                                    mHour = hourOfDay + "";

                                if (minute <= 9)

                                    mMinutes = "0" + minute + "";
                                else
                                    mMinutes = minute + "";


                                intfc.getDateTime("", mHour + ":" + mMinutes);
                            }

                        } else {
                            String mHour = "";
                            String mMinutes = "";
                            if (hourOfDay <= 9)

                                mHour = "0" + hourOfDay + "";

                            else

                                mHour = hourOfDay + "";

                            if (minute <= 9)

                                mMinutes = "0" + minute + "";
                            else
                                mMinutes = minute + "";


                            intfc.getDateTime("", mHour + ":" + mMinutes);
                        }


                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    public static void getDate(Context context, int year, int month, int day, long timeInMillis, GetDateTime intrfc) {


        DatePickerDialog picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mDay = "";
                        if (dayOfMonth <= 9)
                            mDay = "0" + dayOfMonth + "";
                        else
                            mDay = dayOfMonth + "";
                        String mDate = mDay + "-" + Utilities.getMonthFormat(monthOfYear + 1) + "-" + year;
                        intrfc.getDateTime(mDate, "");
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(timeInMillis);
        picker.show();


    }

}
