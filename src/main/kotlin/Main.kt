fun main(args: Array<String>) {
    try {
        ImgController(args)
    } catch (e: Exception) {
        println(e.message)
    }
}
