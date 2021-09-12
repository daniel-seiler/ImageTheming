package theme

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.abs

/**
 * Contains all available colors and matches images to those colors.
 *
 * @constructor
 * Each color has to be scaled, so that at least one of its values is 255. Then the color added to one of the color
 * lists according to its most dominant value. The GRAY value decides the difference between the most and the least
 * dominant color value, so that colors that aren't exactly gray can still be considered as.
 *
 * @param colors    list of colors to add
 */
class Theme(colors: MutableList<Color>) {
    private val MAX_VAL: Double = 255.0
    private val SECOND_FACTOR: Int = 2
    private val GRAY: Int = 30
    private val redList: MutableList<Color> = mutableListOf()
    private val greenList: MutableList<Color> = mutableListOf()
    private val blueList: MutableList<Color> = mutableListOf()
    private val grayList: MutableList<Color> = mutableListOf()

    init {
        colors.forEach { c ->
            val cVal = getNum(c)
            val scaleFactor : Double = MAX_VAL / getCol(c, cVal.first)
            val cScaled = Color((c.red * scaleFactor).toInt()
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
        println(toString())
    }

    /**
     * Create a new image by going through each pixel and matching the color to one of the available ones.
     *
     * @param img   old image to transform
     * @return      new image with colors from the theme
     */
    fun transformImage(img: BufferedImage) : BufferedImage {
        for (y in 0 until img.height) {
            for (x in 0 until img.width) {
                img.setRGB(x, y, getClosest(Color(img.getRGB(x, y))).rgb)
            }
        }
        return img
    }

    /**
     * Find the closest matching color from the theme for a given color.
     *
     * @param c     old color from the original image
     * @return      new color from the theme
     */
    private fun getClosest(c: Color) : Color {
        //check which color value is the highest
        val cVal = getNum(c)

        //select the relevant color list
        val list = if (isGray(c)) {
            grayList
        } else {
            getList(cVal.first)
        }

        //prevent division by 0 with complete black
        var first = getCol(c, cVal.first)
        if (first == 0) {
            first = 1
        }

        //find the factor to scale the color to max brightness
        val scaleFactor : Double = MAX_VAL / first

        //scale the color up
        val cScaled = Color((c.red * scaleFactor).toInt()
            , (c.green * scaleFactor).toInt()
            , (c.blue * scaleFactor).toInt())

        //set values for the comparison to max
        var diff = Integer.MAX_VALUE
        var cBestMatch = Color(0)

        //compare color with list from theme
        list.forEach { tempColor ->
            //calculate diff with second and third prominent color value
            val tempDiff = SECOND_FACTOR * abs(getCol(tempColor, cVal.second) - getCol(cScaled, cVal.second)) + abs(getCol(tempColor, cVal.third) - getCol(cScaled, cVal.third))

            //check match
            if (tempDiff < diff) {
                diff = tempDiff
                cBestMatch = tempColor
            }
        }

        //scale the new color according to the old one
        return Color((cBestMatch.red / scaleFactor).toInt()
            , (cBestMatch.green / scaleFactor).toInt()
            , (cBestMatch.blue / scaleFactor).toInt())
    }

    /**
     * Decide if a color can be considered as gray.
     *
     * @param c     color to be checked
     * @return      boolean value if gray or not
     */
    private fun isGray(c: Color) : Boolean {
        return abs(c.red - c.green) < GRAY
                && abs(c.green - c.blue) < GRAY
                && abs(c.red - c.blue) < GRAY
    }

    /**
     * Get the color value defined as enum.
     *
     * @param c     input color
     * @param cVal  enum to decide which value to return
     * @return      color value as integer
     */
    private fun getCol(c: Color, cVal: ColorVal) : Int {
        return when (cVal) {
            ColorVal.RED -> c.red
            ColorVal.GREEN -> c.green
            ColorVal.BLUE -> c.blue
        }
    }

    /**
     * Get the list where the main value equals the enum.
     *
     * @param cVal  enum to decide which list to return
     * @return      color list with the correct main value
     */
    private fun getList(cVal: ColorVal) : MutableList<Color> {
        return when (cVal) {
            ColorVal.RED -> redList
            ColorVal.GREEN -> greenList
            ColorVal.BLUE -> blueList
        }
    }

    /**
     * Sort the individual color values from highest to lowest.
     *
     * @param c     color to be checked
     * @return      triple with enum values from highest to lowest
     */
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

    override fun toString() : String {
        var output = "red colors:\n"
        redList.forEach { color -> output += "${color}\n" }
        output += "\ngreen colors:\n"
        greenList.forEach { color -> output += "${color}\n" }
        output += "\nblue colors:\n"
        blueList.forEach { color -> output += "${color}\n" }
        output += "\ngray colors:\n"
        grayList.forEach { color -> output += "${color}\n" }
        return output
    }

    /**
     * Enum to select individual color values.
     */
    private enum class ColorVal {
        RED,
        GREEN,
        BLUE
    }
}