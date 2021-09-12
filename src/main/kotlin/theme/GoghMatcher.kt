package theme

import java.awt.Color
import java.lang.Integer.parseInt
import java.net.URL
import java.nio.charset.Charset

/**
 * Load a color scheme from Gogh. Uses regex for future use with config files.
 *
 * @property name   name of theme file without .sh
 */
class GoghMatcher(val name: String) {
    private val regex: Regex = "COLOR(?:_\\d{2})?=\"#(?<color>[\\dabcdefABCDEF]{6})\"".toRegex()
    private val theme = URL("https://raw.githubusercontent.com/Mayccoll/Gogh/master/themes/${name}.sh")
        .readText(Charset.defaultCharset())

    /**
     * Find all colors from the config file and write them to a string list.
     *
     * @return      list of all colors
     */
    private fun match() : List<String> {
        val matches = regex.findAll(theme)
        var outputList = ArrayList<String>()
        matches.forEach { temp -> outputList.add(temp.groupValues[1]) }
        return outputList
    }

    /**
     * Add all colors to the theme class.
     *
     * @return      a new theme class with the matched colors
     */
    fun getTheme() : Theme {
        val stringList = match()
        val colorList = ArrayList<Color>()
        stringList.forEach { temp ->
            colorList.add(Color(parseInt(temp, 16)))
        }
        return Theme(colorList)
    }
}