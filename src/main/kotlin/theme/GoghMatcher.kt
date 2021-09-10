package theme

import java.awt.Color
import java.lang.Integer.parseInt
import java.net.URL
import java.nio.charset.Charset

class GoghMatcher(val name: String) {
    private val regex: Regex = "COLOR(?:_\\d{2})?=\"#(?<color>[\\dabcdefABCDEF]{6})\"".toRegex()
    private val theme = URL("https://raw.githubusercontent.com/Mayccoll/Gogh/master/themes/${name}.sh").readText(Charset.defaultCharset())

    private fun match() : List<String> {
        val matches = regex.findAll(theme)
        var outputList = ArrayList<String>()
        matches.forEach { temp -> outputList.add(temp.groupValues[1]) }
        return outputList
    }

    fun getTheme() : Theme {
        val stringList = match()
        val colorList = ArrayList<Color>()
        stringList.forEach { temp ->
            colorList.add(Color(parseInt(temp, 16)))
        }
        return Theme(colorList)
    }
}