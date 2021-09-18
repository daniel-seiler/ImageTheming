package ui

import ImgController
import java.io.File

class Interactive(private val ctrl: ImgController) {

    init {
        setTheme()
        setInDir()
        setOutDir()
    }

    private fun setTheme() {
        print(
            "Setting the theme:\n" +
                    "- the path to a config file, e.g. \"../.config/colors\" or\n" +
                    "- the name of a theme from https://github.com/Mayccoll/Gogh/tree/master/themes, " +
                    "e.g. OneDark -> one-dark.sh -> input: one-dark\n" +
                    ">> "
        )
        readLine()?.let { ctrl.setTheme(it) }
    }

    private fun setInDir() {
        print(
            "Adding images:\n" +
                    "- url to an image\n" +
                    "- path to an image\n" +
                    "- path to a directory with images\n"
        )
        do {
            print(">> ")
            readLine()?.let { ctrl.inDir.add(it) }
            print("Add another image/path? ")
        } while (askNo())
        print("If you added a path, should the program search recursively? ")
        ctrl.recursive = askNo()
    }

    private fun setOutDir() {
        print("Do you want to set a custom output path? ")
        if (askNo()) {
            var outDir = File("")
            do {
                print(">> ")
                val input = readLine()
                input?.let { outDir = File(input) }
            } while (input != null && outDir.isFile)
            ctrl.outDir = outDir
        }
    }

    private fun askYes() : Boolean {
        do {
            print("[Y/n] ")
            val input = readLine()
            if (input != null) {
                if (input.isEmpty() || input[0].lowercaseChar() == 'y') {
                    return true
                } else if (input[0].lowercaseChar() == 'n') {
                    return false
                }
            }
        } while (true)
    }

    private fun askNo() : Boolean {
        do {
            print("[y/N] ")
            val input = readLine()
            if (input != null) {
                if (input.isEmpty() || input[0].lowercaseChar() == 'n') {
                    return false
                } else if (input[0].lowercaseChar() == 'y') {
                    return true
                }
            }
        } while (true)
    }
}