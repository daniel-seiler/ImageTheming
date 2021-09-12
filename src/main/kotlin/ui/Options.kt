package ui

import ImgController
import java.io.File

/**
 * Evaluate the program arguments.
 *
 * @property regex  regular expression for all possible arguments
 */
enum class Options(val regex: Regex) {

    /**
     * Display the help message with all possible options.
     */
    HELP("-h|--help".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            println("Usage: java -jar ImageTheming.jar [path] [options]\n\n" +
                    "Path:\n" +
                    "Either the path to an image or to a directory. An URL is also possible.\n\n" +
                    "Options:\n" +
                    "-h, --help\n" +
                    "show this message and exit\n\n" +
                    "-t=<theme name> or --theme=<theme name>\n" +
                    "set a theme by file name from https://github.com/Mayccoll/Gogh/tree/master/themes for example: --theme=nord. This option needs to be set.\n\n" +
                    "-r, --recursive\n" +
                    "if a path has been selected, usually only images from this path will be loaded. This option allows to search for images recursively.\n\n" +
                    "-o=<path to directory> or --outputDir=<path to directory>\n" +
                    "select a directory where the new images should go"
            )
            ctlr.exit()
        }
    },

    /**
     * Select a theme by name.
     */
    THEME("(?:-t|--theme)=(?<theme>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.setTheme(matcher.groups[1]!!.value)
        }
    },

    /**
     * Set the image path.
     */
    INPATH("[^-](?<path>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.setPath(matcher.groups[0]!!.value)
        }
    },

    /**
     * Decide if images should be loaded recursive if a directory was specified as path.
     */
    RECURSIVE("-r|--recursive".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.recursive = true
        }
    },

    /**
     * Select a different directory to save the new image.
     */
    OUTDIR("(?:-o|--outputDir)=(?<outDir>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.outDir = File(matcher.groups[1]!!.value)
        }
    };

    /**
     * Execute the given argument on the image controller.
     *
     * @param matcher   the result of the match
     * @param ctlr      the image controller to execute the changes from the argument on
     */
    abstract fun execute(matcher: MatchResult, ctlr: ImgController)

    companion object {
        /**
         * Go through all enum values and match them with the given argument.
         *
         * @param input argument from the main function
         * @param ctlr  the class that controls the program for the main part
         */
        fun executeMatching(input: String, ctlr: ImgController) {
            values().forEach { temp ->
                if (temp.regex.matches(input)) {
                    temp.regex.matchEntire(input)?.let { temp.execute(it, ctlr) }
                }
            }
        }
    }
}