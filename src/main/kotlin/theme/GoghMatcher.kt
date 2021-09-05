package theme

import java.awt.Color
import java.net.URL
import java.nio.charset.Charset
import java.lang.Long

class GoghMatcher(val url: String) {
    private val regex: Regex = "COLOR(?:_\\d{2})?=\"#(?<color>[\\dabcdefABCDEF]{6})\"".toRegex()
    private val theme: String = URL(url).readText(Charset.defaultCharset())

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
            colorList.add(Color(Long.parseLong(temp, 16).toInt()))
        }
        return Theme(colorList)
    }
}