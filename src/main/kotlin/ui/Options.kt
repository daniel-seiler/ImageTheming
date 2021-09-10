package ui

import ImgController

enum class Options(val regex: Regex) {
    OUTPATH("(?:-o|--output)=(?<path>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            TODO("Not yet implemented")
        }
    },

    HELP("-h|--help".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            TODO("Not yet implemented")
        }
    },

    THEME("(?:-t|--theme)=(?<theme>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.setTheme(matcher.groups[1]!!.value)
        }
    },

    INPATH("[^-](?<path>.+)".toRegex()) {
        override fun execute(matcher: MatchResult, ctlr: ImgController) {
            ctlr.setImg(matcher.groups[0]!!.value)
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