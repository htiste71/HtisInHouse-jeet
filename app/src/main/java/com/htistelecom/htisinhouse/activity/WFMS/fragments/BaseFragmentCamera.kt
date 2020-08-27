package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ImageFileUtils
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.DocumentDirectoryFragmentWFMS
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

open class BaseFragmentCamera:Fragment() {


    private var selectedImagePath: String = ""
    private lateinit var muri: Uri
    private lateinit var mVideoPath: String
    private val FOR_MEDIA_FILE = 1001
    private val FOR_REQUEST_CAMERA = 1002
    lateinit var act: Activity

    private lateinit var mFileType: String
    lateinit var mDir: File
    private lateinit var mFragmentName: String
    public fun openPhotoDialog(type: String) {
        mFragmentName = type
        mDir = File(Environment.getExternalStorageDirectory(), "WFMS")
        if (!mDir.exists()) {
            mDir.mkdir()
        }

        var pictureDialogItems: Array<String>
        mFileType = type



        pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")


        val pictureDialog = AlertDialog.Builder(act


        )
        pictureDialog.setTitle("Select Action")

        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> {


                    galleryImage()


                }
                1 -> {


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
            selectedImagePath = muri.getPath()
            val photoURI=FileProvider.getUriForFile(act,act.getApplicationContext().getPackageName()+ ".htisinhouse.provider", photoUploadFile)

            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI)
        }
        else {
            val image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(mDir, image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.getPath()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, muri)
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", true)
        startActivityForResult(intent, FOR_REQUEST_CAMERA)
    }

    public fun
            checkPermissions( type:String,activity: Activity) {
        mFragmentName=type
        act=activity
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            openPhotoDialog(type)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FOR_MEDIA_FILE) {

                val uri = data?.getData()

                val filePath = ImageFileUtils.compressImage(uri!!.toString(),act)
                val file = File(filePath)
                sendImage(file)

            } else if (requestCode == FOR_REQUEST_CAMERA) {

                val uri = Uri.parse(selectedImagePath)
                val filePath = ImageFileUtils.compressImage(uri.toString(),act)
                val file = File(filePath)
                sendImage(file)


            }
        }


    }

    private fun sendImage(file: File) {
        if (mFragmentName.equals("com.htistelecom.htisinhouse.activity.NewKnjrkhana.fragments.ProfileFragmentWFMS")) {
            val fragName: ProfileFragmentWFMS = this as ProfileFragmentWFMS
            fragName.imagePath(file)

        }
        else if(mFragmentName.equals("com.htistelecom.htisinhouse.activity.NewKnjrkhana.document_directory.DocumentDirectoryFragmentWFMS"))
        {
            val fragName: DocumentDirectoryFragmentWFMS = this as DocumentDirectoryFragmentWFMS
            fragName.imagePath(file)
        }
    }
}