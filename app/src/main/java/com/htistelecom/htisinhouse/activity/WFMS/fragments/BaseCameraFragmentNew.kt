package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.DocumentDirectoryFragmentWFMS
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File

open class BaseCameraFragmentNew : Fragment() {


    lateinit var act: Activity
    private lateinit var mFragmentName: String

    public fun checkPermissions(type: String, activity: Activity) {
        mFragmentName = type
        act=activity
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            onSelectImageClick()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                var s = result.getUri()
                sendImage(File(File(result.uri.path).absolutePath))
                //  fragmentImage.setImageURI(s)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(act!!, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    fun callMethod(imageUri: Uri) {
        startCropImageActivity(imageUri)

    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(act!!, this)
    }

    private fun sendImage(file: File) {
        if (mFragmentName.equals("com.htistelecom.htisinhouse.activity.fragments.ProfileFragmentWFMS")) {
            val fragName: ProfileFragmentWFMS = this as ProfileFragmentWFMS
            fragName.imagePath(file)

        } else if (mFragmentName.equals("com.htistelecom.htisinhouse.activity.NewKnjrkhana.document_directory.DocumentDirectoryFragmentWFMS")) {
            val fragName: DocumentDirectoryFragmentWFMS = this as DocumentDirectoryFragmentWFMS
            fragName.imagePath(file)
        }
    }

    fun onSelectImageClick() {
        CropImage.startPickImageActivity(act!!)
    }
}