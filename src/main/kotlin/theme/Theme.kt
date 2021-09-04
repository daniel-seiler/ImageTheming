package theme

import java.awt.Color
import java.lang.Math.abs

class Theme(colors: MutableList<Color>) {
    private val MAX_VAL: Int = 255
    private val SECOND_FACTOR: Int = 2
    private val red: MutableList<Color> = mutableListOf()
    private val green: MutableList<Color> = mutableListOf()
    private val blue: MutableList<Color> = mutableListOf()

    init {
        //TODO scale all colors
        colors.forEach { c ->
            when (getNum(c).first) {
                ColorVal.RED -> red.add(c)
                ColorVal.GREEN -> green.add(c)
                ColorVal.BLUE -> blue.add(c)
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

    private fun calcDiff(c1: Color, c2: Color, cVal: Triple<ColorVal, ColorVal, ColorVal>) : Int {
        return SECOND_FACTOR * abs(getSecond(c1, cVal) - getSecond(c2, cVal)) + abs(getThird(c1, cVal) - getThird(c2, cVal))
    }

    private fun getSecond(c: Color, cVal: Triple<ColorVal, ColorVal, ColorVal>) : Int {
        return getCol(c, cVal.second)
    }

    private fun getThird(c: Color, cVal: Triple<ColorVal, ColorVal, ColorVal>) : Int {
        return getCol(c, cVal.third)
    }

    private fun getCol(c: Color, cVal: ColorVal) : Int {
        return when (cVal) {
            ColorVal.RED -> c.red
            ColorVal.GREEN -> c.green
            ColorVal.BLUE -> c.blue
        }
    }

    private fun getList(triple: Triple<ColorVal, ColorVal, ColorVal>) : MutableList<Color> {
        return when (triple.first) {
            ColorVal.RED -> red
            ColorVal.GREEN -> green
            ColorVal.BLUE -> blue
        }
    }

    private fun getList(cVal: ColorVal) : MutableList<Color> {
        return when (cVal) {
            ColorVal.RED -> red
            ColorVal.GREEN -> green
            ColorVal.BLUE -> blue
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