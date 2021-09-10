package ui

enum class Options(r: Regex) {
    INPATH("".toRegex()),

    OUTPATH("".toRegex()),

    URL("".toRegex()),

    GRAY("".toRegex()),

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