package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ImageFileUtils
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.DocumentDirectoryFragmentWFMS
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class BaseFragmentCamera: Fragment() {


    private var image_name: String=""
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

             image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(act.getExternalFilesDir(null), image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.path.toString()
            val photoURI=FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".htisinhouse.provider", photoUploadFile)

            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI)
        }
        else {
            val image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(act.getExternalFilesDir(null), image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.path.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, muri)
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", true)
        startActivityForResult(intent, FOR_REQUEST_CAMERA)
    }

    public fun checkPermissions(type: String, activity: Activity) {
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


                var cursor: Cursor? = null
                 try {
                    val proj = arrayOf(MediaStore.Images.Media.DATA)
                    cursor = context!!.contentResolver.query(uri!!, proj, null, null, null)
                    val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor!!.moveToFirst()
                    cursor.getString(column_index)
                } finally {
                    if (cursor != null) {
                        cursor.close()
                    }
                }

//                val filePath = ImageFileUtils.compressImage(uri!!.toString(), act)
//                val file = File(filePath)
               // sendImage(file)

            } else if (requestCode == FOR_REQUEST_CAMERA) {

               // val uri = Uri.parse(selectedImagePath)
               val uri= Uri.fromFile(File(selectedImagePath))
                val bitmap = MediaStore.Images.Media.getBitmap(act.contentResolver, uri)
                val file=saveScaledPhotoToFile(bitmap, selectedImagePath)
                //val filePath = ImageFileUtils.compressImage(uri.toString(),act)
//                val file = File(selectedImagePath)
             sendImage(file)


            }
        }




    }

    private fun sendImage(file: File) {
        if (mFragmentName.equals("com.htistelecom.htisinhouse.activity.fragments.ProfileFragmentWFMS")) {
            val fragName: ProfileFragmentWFMS = this as ProfileFragmentWFMS
            fragName.imagePath(file)

        }
        else if(mFragmentName.equals("com.htistelecom.htisinhouse.activity.NewKnjrkhana.document_directory.DocumentDirectoryFragmentWFMS"))
        {
            val fragName: DocumentDirectoryFragmentWFMS = this as DocumentDirectoryFragmentWFMS
            fragName.imagePath(file)
        }
    }

    open fun saveScaledPhotoToFile(photoBm: Bitmap, imgPath: String):File {
        //get its orginal dimensions
        var photoBm = photoBm
        val bmOriginalWidth = photoBm.width
        val bmOriginalHeight = photoBm.height
        val originalWidthToHeightRatio = 1.0 * bmOriginalWidth / bmOriginalHeight
        val originalHeightToWidthRatio = 1.0 * bmOriginalHeight / bmOriginalWidth
        //choose a maximum height
        val maxHeight = 1024
        //choose a max width
        val maxWidth = 1024
        //call the method to get the scaled bitmap
        photoBm = getScaledBitmap(photoBm, bmOriginalWidth, bmOriginalHeight,
                originalWidthToHeightRatio, originalHeightToWidthRatio,
                maxHeight, maxWidth)



        var exif: ExifInterface
        try {
            exif = ExifInterface(imgPath)
            var orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            var matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            photoBm = Bitmap.createBitmap(photoBm!!, 0, 0,
                    photoBm!!.width, photoBm.height, matrix,
                    true)
        } catch (e: IOException) {
            e.printStackTrace()
        }








        /**********THE REST OF THIS IS FROM Prabu's answer */
        //create a byte array output stream to hold the photo's bytes
        val bytes = ByteArrayOutputStream()
        //compress the photo's bytes into the byte array output stream
        photoBm.compress(Bitmap.CompressFormat.JPEG, 40, bytes)





        //construct a File object to save the scaled file to
        val f =File(act.getExternalFilesDir(null), image_name)
        //create the file
        f.createNewFile()





        //create an FileOutputStream on the created file
        val fo = FileOutputStream(f)
        //write the photo's bytes to the file
        fo.write(bytes.toByteArray())

        //finish by closing the FileOutputStream
        fo.close()

        return f
    }

     open fun getScaledBitmap(bm: Bitmap, bmOriginalWidth: Int, bmOriginalHeight: Int, originalWidthToHeightRatio: Double, originalHeightToWidthRatio: Double, maxHeight: Int, maxWidth: Int): Bitmap {
        var bm = bm
        if (bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
            //Log.v(TAG, format("RESIZING bitmap FROM %sx%s ", bmOriginalWidth, bmOriginalHeight))
            if (bmOriginalWidth > bmOriginalHeight) {
                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio)
            } else if (bmOriginalHeight > bmOriginalWidth) {
                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio)
            }
          //  Log.v(TAG, format("RESIZED bitmap TO %sx%s ", bm.width, bm.height))
        }
        return bm
    }

     open fun scaleDeminsFromHeight(bm: Bitmap, maxHeight: Int, bmOriginalHeight: Int, originalWidthToHeightRatio: Double): Bitmap {
        var bm = bm
        val newHeight = Math.max(maxHeight.toDouble(), bmOriginalHeight * .55).toInt()
        val newWidth = (newHeight * originalWidthToHeightRatio).toInt()
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true)
        return bm
    }

     open fun scaleDeminsFromWidth(bm: Bitmap, maxWidth: Int, bmOriginalWidth: Int, originalHeightToWidthRatio: Double): Bitmap {
        //scale the width
        var bm = bm
        val newWidth = Math.max(maxWidth.toDouble(), bmOriginalWidth * .75).toInt()
        val newHeight = (newWidth * originalHeightToWidthRatio).toInt()
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true)
        return bm
    }
}