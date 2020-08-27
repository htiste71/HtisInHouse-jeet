package com.htistelecom.htisinhouse.activity.WFMS.activity;


import android.content.Context;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.htistelecom.htisinhouse.config.TinyDB;



public abstract class BaseActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    private Boolean profileImage = false;
//
//    String openCam(Context context, ImageView imgView, String method) {
//        _context = context;
//        _imgView = imgView;
//        if (method.equalsIgnoreCase(CAMERA_METHOD)) {
//            mCurrentPhotoPath = comfun.open_camera(_context, MY_CAMERA_PERMISSION_CODE);
//        }
//
//        if (method.equalsIgnoreCase(GALLERY_METHOD)) {
//            mCurrentPhotoPath = comfun.open_gallery(_context, PICK_IMAGE);
//        }
//        if (method.equalsIgnoreCase(PROFILE_METHOD_GALLERY)) {
//            mCurrentPhotoPath = comfun.open_gallery(_context, PICK_IMAGE);
//            profileImage = true;
//        }
//        if (method.equalsIgnoreCase(PROFILE_METHOD_CAMERA)) {
//            mCurrentPhotoPath = comfun.open_camera(_context, MY_CAMERA_PERMISSION_CODE);
//            profileImage = true;
//        }
//
//
//        if (mCurrentPhotoPath != null && mCurrentPhotoPath.contains("Error")) {
//            Utilities.showToast(this, mCurrentPhotoPath);
//        }
//        return mCurrentPhotoPath;
//    }
//
//
//    private String mCurrentPhotoPath;
//    private int MY_CAMERA_PERMISSION_CODE = 100;
//    private int PICK_IMAGE = 101;
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == MY_CAMERA_PERMISSION_CODE && resultCode == RESULT_OK) {
//            Uri sourceUri = Uri.parse(mCurrentPhotoPath);
//            comfun.openCropActivity(_context, sourceUri, sourceUri, null);
//            /*Bitmap photo = commonFunctions.getBitMapFromPath(this, mCurrentPhotoPath);
//            if (photo != null) {
//                imgView.setImageBitmap(photo);
//            }*/
//        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//            Uri sourceUri = data.getData();
//            //mCurrentPhotoPath
//            comfun.openCropActivity(_context, sourceUri, sourceUri, null);
//        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
//
//            Bitmap photo = comfun.getBitMapFromPath(_context, mCurrentPhotoPath);
//            _imgView.setImageBitmap(photo);
//
//            try {
//                tinyDB = new TinyDB(BaseActivity.this);
//                //tinyDB = new TinyDB(_context);
//                Uri tempUri = getImageUri(_context, photo);
//                File finalFile = new File(getRealPathFromURI(tempUri));
//
//                if (profileImage) {
//                    if (Utilities.isNetConnected(_context)) {
//                        callApi(finalFile);
//                    } else {
//                        Utilities.showToast(_context, "Please check your Internet Connection");
//                    }
//                }
//                //for claim Images upload
//                else {
//                    String ClaimImages = tinyDB.getString("ClaimImages");
//                    ClaimImages = ClaimImages + "," + finalFile.getAbsolutePath();
//                    tinyDB.putString("ClaimImages", ClaimImages);
//                }
//
//            } catch (Exception ex) {
//                Log.e("ClaimImages", ex.getMessage());
//            }
//            // showUploadViews();
//        } else {
//            //UtilitiesWFMS.showToast(_context, "nothing");
//        }
//    }
//
//    private Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    private String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }
//
//    private Boolean isGPSEnabled;
//    private LocationManager locationManager;
//    private GPSTracker gps;
//    private double latitude = 0.0;
//    double longitude = 0.0;
//
//    Boolean requestLocationPermission(Context context) {
//        isGPSEnabled = false;
//        _context = context;
//        Activity activity = (Activity) _context;
//        Dexter.withActivity(activity)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        // permission is granted
//                        isGPSEnabled = true;
//                        locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
//                        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                        isGPSEnabled = checkGPSStatus();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        // check for permanent denial of permission
//                        if (response.isPermanentlyDenied()) {
//                            isGPSEnabled = showSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//        return isGPSEnabled;
//    }
//
//    private Boolean showSettingsDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
//        builder.setTitle("Need Permissions");
//        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
//        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                isGPSEnabled = openSettings();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                isGPSEnabled = false;
//            }
//        });
//        builder.show();
//        return isGPSEnabled;
//    }
//
//    // navigating user to app settings
//
//    private Boolean openSettings() {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//        builder.setTitle("Required Permissions");
//        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
//        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivityForResult(intent, 101);
//                isGPSEnabled = true;
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                isGPSEnabled = false;
//            }
//        });
//        builder.show();
//        return isGPSEnabled;
//    }
//
//    public void getLatLong(Context context, Double _latitude, Double _longitude) {
//        _context = context;
//        gps = new GPSTracker(_context);
//        if (gps.canGetLocation()) {
//            int i = 0;
//            while (latitude == 0) {
//                _latitude = gps.getLatitude();
//                _longitude = gps.getLongitude();
//
//                i = i + 1;
//                if (i > 3) {
//                    break;
//                }
//            }
//        } else {
//            gps.showSettingsAlert();
//        }
//    }
//
//    private Geocoder geocoder;
//
//    public List<Address> getAddressFromLatLong(Context context, double LATITUDE, double LONGITUDE) {
//        geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                return addresses;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Utilities.showToast(context, e.getMessage());
//        }
//        return null;
//    }
//
//
//    private void callApi(File finalFile) {
//        String ProfileImage = "";
//        ProfileImage = finalFile.getAbsolutePath();
//        tinyDB.putString("ClaimImages", ProfileImage);
//        ///File file = new File(ProfileImage);
//        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), finalFile);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", finalFile.getName(), mFile);
//        String Id = tinyDB.getString("EmpId");
//        RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), Id);
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<Object> call = apiService.uploadImage(fileToUpload, mUSerId);
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                try {
//                    String data = new Gson().toJsonTree(response.body()).toString();
//                    JSONObject jsonObject = new JSONObject(data);
//                    if (jsonObject.getBoolean("Success")) {
//                        String imageName = jsonObject.getString("ImageName");
//                        imageName = PROFILE_IMAGE_PATH + imageName;
//                        tinyDB.putString("ProfileImg", imageName);
//
//                        Glide.with(_context)
//                                .load(imageName)
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                                        return false;
//                                    }
//                                })
//                                .into(_imgView);
//                        Utilities.showToast(_context, "Profile pic uploaded successfully");
//                    } else {
//                        Utilities.showToast(_context, jsonObject.get("Error").toString());
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                Utilities.showToast(_context, "image not uploaded");
//            }
//        });
//    }
//
//    private boolean checkGPSStatus() {
//        int locationMode = 0;
//        String locationProviders;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(_context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
//        } else {
//            locationProviders = Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            return !TextUtils.isEmpty(locationProviders);
//        }
//    }
//
//    public void
//    openDialog(iImagePath intrfc) {
//        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(BaseActivity.this);
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Select photo from gallery",
//                "Capture photo from camera"};
//        pictureDialog.setItems(pictureDialogItems,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                if (comfun.permissioncheck(BaseActivity.this, PERMISSION_REQUEST_CODE)) {
//
//
//                                    mCurrentPhotoPath = comfun.open_gallery(_context, PICK_IMAGE);
//                                    intrfc.imagePath(mCurrentPhotoPath);
//
//                                } else {
//
//                                }
//                                break;
//                            case 1:
//                                if (comfun.permissioncheck(BaseActivity.this, PERMISSION_REQUEST_CODE)) {
//                                    mCurrentPhotoPath = comfun.open_camera(_context, MY_CAMERA_PERMISSION_CODE);
//                                    intrfc.imagePath(mCurrentPhotoPath);
//
//                                } else {
//
//                                }
//                        }
//                    }
//                });
//        pictureDialog.show();
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}