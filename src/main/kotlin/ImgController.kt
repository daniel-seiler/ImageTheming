import theme.GoghMatcher
import theme.Theme
import ui.Options
import java.awt.image.BufferedImage

class ImgController(args: Array<String>) {
    var img: MutableList<BufferedImage> = mutableListOf()
    var outAppendix: String? = null
    var theme: Theme? = null

    init {
        args.forEach { arg ->
            Options.executeMatching(arg, this)
        }
        if (isValid()) {
            createImg()
        } else {
            throw UninitializedPropertyAccessException("Not all necessary options were set. Try -h / --help")
        }
    }

    fun setTheme(name: String) {
        theme = GoghMatcher("https://raw.githubusercontent.com/Mayccall/Gogh/master/themes/$name.sh").getTheme()
        if (outAppendix == null) {
            outAppendix = "-$name"
        }
    }

    fun setAppendix(outAppendix: String) {
        this.outAppendix = outAppendix
    }

    private fun isValid() : Boolean {
        return img.isNotEmpty() && theme != null
    }

    private fun createImg() {

    }
}