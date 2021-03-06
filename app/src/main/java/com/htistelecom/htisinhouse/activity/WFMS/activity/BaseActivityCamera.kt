package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ImageFileUtils
import com.htistelecom.htisinhouse.activity.WFMS.claims.AddClaimActivityWFMS
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File


open class BaseActivityCamera : AppCompatActivity() {
    private var imageFrom: Int=-1
    private var selectedImagePath: String = ""
    private lateinit var muri: Uri
    private lateinit var mVideoPath: String
    private val FOR_MEDIA_FILE = 1001
    private val FOR_REQUEST_CAMERA = 1002
    lateinit var act: AppCompatActivity

    private lateinit var mFileType: String
    lateinit var mDir: File
    private lateinit var mActivityType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    public fun openPhotoDialog(type: String) {
        mActivityType = type
        mDir = File(Environment.getExternalStorageDirectory(), "WFMS")
        if (!mDir.exists()) {
            mDir.mkdir()
        }

        var pictureDialogItems: Array<String>
        mFileType = type



        pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")


        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")

        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> {

                    imageFrom=0
                    galleryImage()


                }
                1 -> {

                    imageFrom=1

                    cameraPhoto()
                }

            }
        }
        pictureDialog.show()
    }


    private fun galleryImage() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK)
        openGalleryIntent.type = "image/*"

        startActivityForResult(openGalleryIntent, FOR_MEDIA_FILE)
    }


    private fun cameraPhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(mDir, image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.path.toString()
            val photoURI = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName()+ ".htisinhouse.provider",
                    photoUploadFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI)
        } else {
            val image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(mDir, image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.path.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, muri)
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", true)
        startActivityForResult(intent, FOR_REQUEST_CAMERA)
    }

    public fun checkPermissions(type: String) {
        mActivityType = type
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            onSelectImageClick()

                            // openPhotoDialog(type)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()


    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//         super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == FOR_MEDIA_FILE) {
//
//                val uri = data?.getData()
//
//                val filePath = ImageFileUtils.compressImage(uri!!.toString(), this)
//                val file = File(filePath)
//                sendImage(file)
//
//            } else if (requestCode == FOR_REQUEST_CAMERA) {
//
//                val uri = Uri.parse(selectedImagePath)
//                val filePath = ImageFileUtils.compressImage(uri.toString(), this)
//                val file = File(filePath)
//                sendImage(file)
//
//
//            }
//        }
//
//
//    }

    private fun sendImage(file: File) {
        if (mActivityType.equals("com.htistelecom.htisinhouse.activity.WFMS.activity.PerformActivityWFMS")) {
            val actName: PerformActivityWFMS = this as PerformActivityWFMS
            actName.imagePath(file)

        } else if (mActivityType.equals("com.htistelecom.htisinhouse.activity.WFMS.claims.AddClaimActivityWFMS")) {
            val actName: AddClaimActivityWFMS = this as AddClaimActivityWFMS
            actName.imagePath(file)
        }
//        else if(mActivityType.equals("com.htistelecom.htisinhouse.activity.NewKnjrkhana.activity.PerformActivityNew"))
//        {
//            val actName: PerformActivityNew = this as PerformActivityNew
//            actName.imagePath(file.absolutePath,imageFrom)
//        }
    }

    fun onSelectImageClick() {
        CropImage.startPickImageActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri = CropImage.getPickImageResultUri(this, data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
               // mCropImageUri = imageUri
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri)
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                var s = result.getUri()
                sendImage(File(File(result.uri.path).absolutePath))

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this)
    }
}