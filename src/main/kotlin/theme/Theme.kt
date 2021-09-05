package theme

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.abs

class Theme(colors: MutableList<Color>) {
    private val MAX_VAL: Double = 255.0
    private val SECOND_FACTOR: Int = 2
    private val GRAY: Int = 30;
    private val redList: MutableList<Color> = mutableListOf()
    private val greenList: MutableList<Color> = mutableListOf()
    private val blueList: MutableList<Color> = mutableListOf()
    private val grayList: MutableList<Color> = mutableListOf()

    init {
        colors.forEach { c ->
            val cVal = getNum(c)
            val scaleFactor : Double = MAX_VAL / getCol(c, cVal.first)
            var cScaled = Color((c.red * scaleFactor).toInt()
                , (c.green * scaleFactor).toInt()
                , (c.blue * scaleFactor).toInt())
            if (isGray(cScaled)) {
                grayList.add(cScaled)
            } else {
                when (cVal.first) {
                    ColorVal.RED -> redList.add(cScaled)
                    ColorVal.GREEN -> greenList.add(cScaled)
                    ColorVal.BLUE -> blueList.add(cScaled)
                }
            }
        }
    }

    private fun getClosest(c: Color) : Color {
        val cVal = getNum(c)
        val list = getList(cVal.first)
        //TODO scale each individual color
        var diff = Integer.MAX_VALUE
        var bestColor = Color(0)
        list.forEach { tempColor ->
            val tempDiff = calcDiff(tempColor, c, cVal)
            if (tempDiff < diff) {
                diff = tempDiff
                bestColor = tempColor
            }
        }
        return bestColor
    }

    private fun isGray(c: Color) : Boolean {
        return abs(c.red - c.green) < GRAY
                && abs(c.green - c.blue) < GRAY
                && abs(c.red - c.blue) < GRAY
    }

    private fun getCol(c: Color, cVal: ColorVal) : Int {
        return when (cVal) {
            ColorVal.RED -> c.red
            ColorVal.GREEN -> c.green
            ColorVal.BLUE -> c.blue
        }
    }

    private fun getList(cVal: ColorVal) : MutableList<Color> {
        return when (cVal) {
            ColorVal.RED -> redList
            ColorVal.GREEN -> greenList
            ColorVal.BLUE -> blueList
        }
    }

    private fun getNum(c: Color) : Triple<ColorVal, ColorVal, ColorVal> {
        return if (c.red <= c.green) {
            if (c.green <= c.blue) {
                Triple(ColorVal.BLUE, ColorVal.GREEN, ColorVal.RED)
            } else {
                if (c.red <= c.blue) {
                    Triple(ColorVal.GREEN, ColorVal.BLUE, ColorVal.RED)
                } else {
                    Triple(ColorVal.GREEN, ColorVal.RED, ColorVal.BLUE)
                }
            }
        } else {
            if (c.green <= c.blue) {
                if (c.red <= c.blue) {
                    Triple(ColorVal.BLUE, ColorVal.RED, ColorVal.GREEN)
                } else {
                    Triple(ColorVal.RED, ColorVal.BLUE, ColorVal.GREEN)
                }
            } else {
                Triple(ColorVal.RED, ColorVal.GREEN, ColorVal.BLUE)
            }
        }
    }

    private enum class ColorVal {
        RED,
        GREEN,
        BLUE
    }
}