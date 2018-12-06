package timepiece.util

import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(5, TimeUnit.SECONDS)
        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}