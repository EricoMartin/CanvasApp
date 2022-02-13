package com.basebox.edvoracanvas

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.basebox.edvoracanvas.databinding.ActivityMainBinding
import com.basebox.edvoracanvas.domain.CanvasView.Companion.brushColor
import com.basebox.edvoracanvas.domain.CanvasView.Companion.shapeType
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listOfButtons: ArrayList<View> = ArrayList<View>()
    var mutableListButtons = mutableListOf<View>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Clickable Views
        val pen: View = binding.pen
        val arrow: View = binding.arrow
        val rect: View = binding.rectangle
        val ellipse: View = binding.ellipse
        val palette: View = binding.palette

        mutableListButtons.add(pen)
        mutableListButtons.add(arrow)
        mutableListButtons.add(rect)
        mutableListButtons.add(ellipse)
        mutableListButtons.add(palette)

        shapeType.add("rectangle")
        shapeType.add("oval")
        shapeType.add("arrow")

        listOfButtons.addAll(mutableListButtons)

//      Click handlers
        for (i in listOfButtons.indices) {
            listOfButtons[i].setOnClickListener {
                binding.paletteButtons.visibility = View.GONE
                when (it) {
                    pen -> {
                        it.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.background_selector,
                            null
                        )
                        binding.pen.setColorFilter(
                            getResources().getColor(
                                R.color.black,
                                resources.newTheme()
                            )
                        )

                    }
                    arrow -> {
                        it.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.background_selector,
                            null
                        )
                        binding.arrow.setColorFilter(
                            getResources().getColor(
                                R.color.black,
                                resources.newTheme()
                            )
                        )
                        binding.pen.clearColorFilter()
                        changeShapeType("arrow")
                    }
                    rect -> {
                        it.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.background_selector,
                            null
                        )
                        binding.rectangle.setColorFilter(
                            getResources().getColor(
                                R.color.black,
                                resources.newTheme()
                            )
                        )
                        binding.arrow.clearColorFilter()
                        changeShapeType("rectangle")

                    }
                    ellipse -> {
                        it.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.background_selector,
                            null
                        )
                        binding.ellipse.setColorFilter(
                            getResources().getColor(
                                R.color.black,
                                resources.newTheme()
                            )
                        )
                        binding.rectangle.clearColorFilter()
                        changeShapeType("oval")
                    }
                    palette -> {
                        it.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.background_selector,
                            null
                        )
                        binding.palette.setColorFilter(
                            getResources().getColor(
                                R.color.black,
                                resources.newTheme()
                            )
                        )
                        binding.ellipse.clearColorFilter()
                        binding.paletteButtons.visibility = View.VISIBLE
                    }
                }
            }
        }

//        Color palette
        binding.red.setOnClickListener {
            paintColor.color = Color.RED
            changeColor(paintColor.color)
        }
        binding.green.setOnClickListener {
            paintColor.color = Color.GREEN
            changeColor(paintColor.color)
        }
        binding.blue.setOnClickListener {
            paintColor.color = Color.BLUE
            changeColor(paintColor.color)
        }
        binding.black.setOnClickListener {
            paintColor.color = Color.BLACK
            changeColor(paintColor.color)
        }

    }

    private fun changeColor(color: Int) {
        brushColor = color
        path = Path()
    }

    private fun changeShapeType(shape: String) {
        shapeType[0] = shape
    }

    companion object {
        var path = Path()
        var paintColor = Paint()

    }

}