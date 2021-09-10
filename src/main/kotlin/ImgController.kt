import theme.GoghMatcher
import theme.Theme
import ui.Options
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class ImgController(args: Array<String>) {
    private var img: MutableMap<File, BufferedImage> = mutableMapOf()
    private var themeName: String? = null
    private var imgName: String? = null//TODO implement for custom names
    private var theme: Theme? = null
    private var isRunning: Boolean = true

    init {
        args.forEach { arg ->
            Options.executeMatching(arg, this)
        }
        if (isValid()) {
            saveNewImg()
        } else if (isRunning) {
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

    fun exit() {
        isRunning = false
    }

    private fun setNewPath(file: File) : File {
        val newFile = file.parentFile
        return newFile.resolve("${file.nameWithoutExtension}-${themeName}.${file.extension}")
        //TODO implement images to dir
    }

    private fun isValid() : Boolean {
        return img.isNotEmpty() && theme != null
    }

    private fun saveNewImg() {
        img.forEach { (file, bufferedImg) ->
            ImageIO.write(theme!!.transformImage(bufferedImg), file.extension, setNewPath(file))
        }
    }
}