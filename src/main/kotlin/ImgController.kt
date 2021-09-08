import theme.GoghMatcher
import theme.Theme
import ui.Options
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class ImgController(args: Array<String>) {
    private var img: MutableMap<BufferedImage, File?> = mutableMapOf()
    private var theme: Theme? = null

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

    fun getImg(path: String) {
        val file = File(path)
        if (file.isFile) {
            img[ImageIO.read(file)] = file
        } else {//TODO implement images from dir
            img[ImageIO.read(URL(path))] = File(System.getProperty("user.dir") + "/" + URL(path).host)
        }
    }

    private fun isValid() : Boolean {
        return img.isNotEmpty() && theme != null
    }

    private fun createImg() {

    }
}