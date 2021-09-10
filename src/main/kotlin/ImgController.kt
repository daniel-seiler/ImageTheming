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
        theme = GoghMatcher(name).getTheme()
        themeName = name
    }

    fun setImg(path: String) {
        val file = File(path)
        if (file.isFile) {
            img[file] = ImageIO.read(file)
        } else {//TODO implement images from dir
            img[File(System.getProperty("user.dir")).resolve(URL(path).host + ".png")] = ImageIO.read(URL(path))
        }
    }

    private fun isValid() : Boolean {
        return img.isNotEmpty() && theme != null
    }

    private fun createImg() {

    }
}