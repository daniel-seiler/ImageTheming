package ui

import ImgController
import java.io.File

enum class Options(val regex: Regex) {

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

    THEME("(?:-t|--theme)=(?<theme>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.setTheme(matcher.groups[1]!!.value)
        }
    },

    INPATH("[^-](?<path>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.setPath(matcher.groups[0]!!.value)
        }
    },

    RECURSIVE("-r|--recursive".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.recursive = true
        }
    },

    OUTDIR("(?:-o|--outputDir)=(?<outDir>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.outDir = File(matcher.groups[1]!!.value)
        }
    };

    abstract fun execute(matcher: MatchResult, ctlr: ImgController)

    companion object {
        fun executeMatching(input: String, ctlr: ImgController) {
            values().forEach { temp ->
                if (temp.regex.matches(input)) {
                    temp.regex.matchEntire(input)?.let { temp.execute(it, ctlr) }
                }
            }
        }
    }
}