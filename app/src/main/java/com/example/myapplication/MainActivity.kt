package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import kotlinx.android.synthetic.main.dialog_color_choose.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_View.setSizeForBrush(20.toFloat())
        ibBrush.setOnClickListener{
            showBrushSizeChooserDialog()
        }
        ibColorPallete.setOnClickListener{
            showColorChooserDialog()
        }

        ibGallery.setOnClickListener {
            if (isReadStorageAllowed()){
                val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhotoIntent, GALLERY)
            }
            else{
                requestStoragePermission()
            }
        }
        ibUndo.setOnClickListener {
            drawing_View.onClickUndo()
        }

        ibSave.setOnClickListener {this
            BitmapAsyncTask(getBitmapFromView(flDrawingViewContainer)).execute()
        }
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size : ")
        val smallBtn = brushDialog.imageBtnSmallBrush
        smallBtn.setOnClickListener {
            drawing_View.setSizeForBrush(10.0f)
            brushDialog.dismiss()
        }
        val mediumBtn = brushDialog.imageBtnMediumBrush
        mediumBtn.setOnClickListener {
            drawing_View.setSizeForBrush(20.0f)
            brushDialog.dismiss()
        }
        val largeBtn = brushDialog.imageBtnLargeBrush
        largeBtn.setOnClickListener {
            drawing_View.setSizeForBrush(30.0f)
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    private fun showColorChooserDialog() {
        val colorDialog = Dialog(this)
        colorDialog.setContentView(R.layout.dialog_color_choose)
        colorDialog.setTitle("Choose Color : ")
        colorDialog.show()

        val colorButtons = listOf(
            colorDialog.blackColorIB,
            colorDialog.orangeColorIB,
            colorDialog.blueColorIB,
            colorDialog.cyanColorIB,
            colorDialog.redColorIB,
            colorDialog.whiteColorIB,
            colorDialog.greenColorIB,
            colorDialog.magentaColorIB,
            colorDialog.yellowColorIB,
            colorDialog.indigoColorIB
        )

        for (button in colorButtons) {
            button.setOnClickListener {
                val color = when (button) {
                    colorDialog.blackColorIB -> Color.BLACK
                    colorDialog.orangeColorIB -> Color.parseColor("#FFA500")
                    colorDialog.blueColorIB -> Color.BLUE
                    colorDialog.cyanColorIB -> Color.parseColor("#00FFFF")
                    colorDialog.redColorIB -> Color.RED
                    colorDialog.whiteColorIB -> Color.WHITE
                    colorDialog.greenColorIB -> Color.GREEN
                    colorDialog.magentaColorIB -> Color.MAGENTA
                    colorDialog.yellowColorIB -> Color.YELLOW
                    colorDialog.indigoColorIB -> Color.parseColor("#4B0082")
                    else -> Color.BLACK
                }
                drawing_View.setColor(color)
                colorDialog.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == STORAGE_PERMISSION_CODE) {
                try {
                    if (data != null && data.data != null) {
                        ivBackground.visibility = View.VISIBLE
                        ivBackground.setImageURI(data.data)
                    } else {
                        Toast.makeText(this, "Image Corrupted", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun requestStoragePermission() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"  // You can specify a MIME type or */* for all types
        startActivityForResult(intent, STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted" , Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Permission Denied" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isReadStorageAllowed() : Boolean{
        val result = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun getBitmapFromView(view : View) : Bitmap{
        val returnedBitmap = Bitmap.createBitmap(view.width , view.height , Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background

        if (bgDrawable !=null){
            bgDrawable.draw(canvas)
        }
        else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private inner class BitmapAsyncTask(val mBitmap: Bitmap) : AsyncTask<Any,Void,String>(){
        override fun doInBackground(vararg p0: Any?): String {
            var result = ""
            try {
                val bytes = ByteArrayOutputStream()
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                val f = File(externalCacheDir!!.absoluteFile.toString() + File.separator + "Artify_" + System.currentTimeMillis()/1000 + ".png")
                val fos = FileOutputStream(f)
                fos.write(bytes.toByteArray())
                fos.close()
                result = f.absolutePath
            }
            catch (e:java.lang.Exception){
                result=""
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, "File Saved Successfully: $result", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Something Went Wrong While Saving the File", Toast.LENGTH_SHORT).show()
            }
            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result),null){
                path , uri -> val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.type = "image/png"
                startActivity(Intent.createChooser(shareIntent, "Share"))
            }
        }
    }

    companion object{
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }
}