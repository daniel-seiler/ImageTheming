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
                    "\tEither the path or the url of an image.\n\n" +
                    "Options:\n" +
                    "\t-h, --help\t\t\tshow this help message and exit\n\n" +
                    "\t-t=<theme name> or\t\tset a theme by file name from https://github.com/Mayccoll/Gogh/tree/master/themes\n" +
                    "\t--theme=<theme name>\t\tfor example: --theme=nord\n"
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