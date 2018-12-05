package timepiece

import java.io.File
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


val logFile = "./window.log"
val interval: Long = 5 * 60 * 1000

fun main(args: Array<String>){
    if(args.size > 0 && args[0].equals("evaluate")) {
        evaluateTodaysUsage()
    }else{
        println("Start tracking windows")
        println()
        periodicallyCheckWindowTitle()
    }
}

fun evaluateTodaysUsage() {
    val now = Instant.now().truncatedTo(ChronoUnit.DAYS)
    val list =
    File(logFile).readLines().map {
        val split = it.split(",")
        ActiveWindowRecord(Instant.parse(split[0]), split[1])
    }.filter {
        now == it.date.truncatedTo(ChronoUnit.DAYS)
    }.fold(mutableListOf<ActiveWindowPeriod>()){
        list, it ->
        val lastOrNull = list.lastOrNull()
        if(lastOrNull == null){
            list.add(ActiveWindowPeriod(it.date.truncatedTo(ChronoUnit.SECONDS), it.date.truncatedTo(ChronoUnit.SECONDS), Duration.ZERO.toSeconds(), it.windowTitle))
            return@fold list
        }

        lastOrNull.endTime = it.date.truncatedTo(ChronoUnit.SECONDS)
        lastOrNull.duaration = Duration.between(lastOrNull.startTime, lastOrNull.endTime).toSeconds()

        if(!lastOrNull.windowTitle.equals(it.windowTitle)){
            list.add(ActiveWindowPeriod(it.date.truncatedTo(ChronoUnit.SECONDS), it.date.truncatedTo(ChronoUnit.SECONDS), Duration.ZERO.toSeconds(), it.windowTitle))

        }
        list
    }.also {
        println(it.joinToString("\n"))
    }

}

fun periodicallyCheckWindowTitle() {
    Timer().schedule(100, interval) {
        "xdotool getwindowfocus getwindowname".runCommand()?.trim()?.apply {
            val file = File(logFile)
            file.exists()
            val activeWindowRecord = ActiveWindowRecord(Instant.now(), this)
            println(activeWindowRecord)
            file.appendText("${activeWindowRecord.date},${activeWindowRecord.windowTitle}\n")
        }

    }
}


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
