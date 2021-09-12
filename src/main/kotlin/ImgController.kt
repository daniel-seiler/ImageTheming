import theme.GoghMatcher
import theme.Theme
import ui.Options
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

/**
 * Control the main functions of the whole program.
 *
 * @constructor
 * Evaluate all given arguments, load images, transform and save them later.
 *
 * @param args      program arguments from the main function
 */
class ImgController(args: Array<String>) {
    private var img: MutableMap<File, BufferedImage> = mutableMapOf()
    private var themeName: String? = null
    private var theme: Theme? = null
    private var isRunning: Boolean = true
    private var files: MutableList<String> = mutableListOf()
    var recursive: Boolean = false
    var outDir: File? = null

    init {
        args.forEach { arg ->
            Options.executeMatching(arg, this)
        }
        println("adding images...")
        files.forEach { file -> getImg(file) }
        if (isValid()) {
            saveNewImg()
        } else if (isRunning) {
            throw UninitializedPropertyAccessException("Not all necessary options were set. Try -h / --help")
        }
    }

    /**
     * Set a theme from Gogh.
     *
     * @param name  name of the theme for future reference
     */
    fun setTheme(name: String) {
        theme = GoghMatcher(name).getTheme()
        themeName = name
    }

    /**
     * Add a path as string to the files list. The list is evaluated after all args are set.
     *
     * @param path  path to a file/directory/url as string
     */
    fun setPath(path: String) {
        files.add(path)
    }

    /**
     * Retrieve an image from a path and write it to the map img.
     *
     * @param path  path which was set by the program arguments
     */
    private fun getImg(path: String) {
        val file = File(path)
        if (file.isFile) {
            println("$file")
            img[file] = ImageIO.read(file)
        } else if (file.isDirectory) {
            setFilesFromDir(file)
        } else {
            try {
                img[File(System.getProperty("user.dir")).resolve(URL(path).host + ".png")] = ImageIO.read(URL(path))
                println(path)
            } catch (e: Exception) {
                throw IllegalArgumentException("Couldn't load image from $path")
            }
        }
    }

    /**
     * Stop the program if the help argument was selected.
     */
    fun exit() {
        isRunning = false
    }

    /**
     * Search a directory for images. Either the exact directory or recursively.
     *
     * @param dir   directory to start searching from
     */
    private fun setFilesFromDir(dir: File) {
        if (dir.isDirectory) {
            dir.walk().forEach {
                if (it.isFile && (it.parentFile.equals(dir) || recursive)) {
                    try {
                        val bufferedImage = ImageIO.read(it)
                        img[it] = bufferedImage
                        println("$it")
                    } catch (e: Exception) {}
                }
            }
        }
    }

    /**
     * Create a path for the new image to be saved at. If a different directory is defined it'll be used. The name of
     * the new image consists of the old name with the theme name. If the image comes from an url the host will be used.
     *
     * @param file  path to the original file
     * @return      path to the new file
     */
    private fun setNewPath(file: File) : File {
        var newFile: File
        if (outDir != null && (outDir!!.isDirectory || !outDir!!.exists())) {
            newFile = outDir as File
            newFile.mkdirs()
        } else {
            newFile = file.parentFile
            if (newFile == null) {
                newFile = File(System.getProperty("user.dir"))
            }
        }
        return newFile.resolve("${file.nameWithoutExtension}-${themeName}.${file.extension}")
    }

    /**
     * Check if a theme and at least one image was set.
     *
     * @return      boolean value if the program is ready to start converting the images
     */
    private fun isValid() : Boolean {
        return img.isNotEmpty() && theme != null
    }

    /**
     * Save the new images at their new path.
     */
    private fun saveNewImg() {
        println("creating new images...")
        img.forEach { (file, bufferedImg) ->
            ImageIO.write(theme!!.transformImage(bufferedImg), file.extension, setNewPath(file))
            println("saved to ${setNewPath(file)}")
        }
    }
}