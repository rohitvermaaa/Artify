package com.example.myapplication

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import kotlinx.android.synthetic.main.dialog_color_choose.*

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
}