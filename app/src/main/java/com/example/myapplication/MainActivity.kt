package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_View.setSizeForBrush(20.toFloat())
        ibBrush.setOnClickListener{
            showBrushSizeChooserDialog()
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
}