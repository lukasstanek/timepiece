package timepiece

import java.io.File
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.*

const val ACTIVITY_INACTIVE = "INACTIVE"

const val logFile = "./window.log"
const val windowTitleInterval: Long = 60 * 1000
const val timeUntilInactivity = 60 * 5 // in secs

var timestampLastInput: Instant = Instant.now()

fun main(args: Array<String>){
    if (args.size > 1 && args[0].equals("report")){
        reportOnTimeTracked()
    }else{
        trackActivity()
    }

}

fun trackActivity() {
    GlobalScope.launch {
        listenForInputEvents()
    }
    periodicallyCheckWindowTitle()
}


fun periodicallyCheckWindowTitle() {
    Timer().schedule(100, windowTitleInterval) {
        "xdotool getwindowfocus getwindowname".runCommand()?.trim()?.apply {
            val file = File(logFile)
            file.exists()
            val activeWindowRecord = ActiveWindowRecord(Instant.now(), this)

            var activity = activeWindowRecord.windowTitle
            if(Duration.between(timestampLastInput,  Instant.now()).toSeconds() > timeUntilInactivity){
                activity = ACTIVITY_INACTIVE
            }

            val record = "${activeWindowRecord.date},$activity\n"
            print(record)
            file.appendText(record)
        }

    }
}

suspend fun listenForInputEvents() {
    val process = ProcessBuilder("xinput", "test-xi2", "--root")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()

    return suspendCoroutine {
        process.inputStream.bufferedReader().forEachLine {
            if(it.startsWith("EVENT type 3") || it.startsWith("EVENT type 6")){
                timestampLastInput = Instant.now()
                println(it)
            }
        }
    }
}

fun reportOnTimeTracked() {
    val now = Instant.now().truncatedTo(ChronoUnit.DAYS)

    File(logFile).readLines().map {
        val split = it.split(",")
        ActiveWindowRecord(Instant.parse(split[0]), split[1])
    }.filter {
        now == it.date.truncatedTo(ChronoUnit.DAYS)
    }.fold(mutableListOf<ActiveWindowPeriod>()){
            list, it ->
        val lastOrNull = list.lastOrNull()

        if(lastOrNull != null && lastOrNull.windowTitle.equals(it.windowTitle)){
            lastOrNull.endTime = it.date.truncatedTo(ChronoUnit.SECONDS)
            lastOrNull.duaration = Duration.between(lastOrNull.startTime, lastOrNull.endTime).toSeconds()
        }else{
            list.add(ActiveWindowPeriod(it.date.truncatedTo(ChronoUnit.SECONDS), it.date.truncatedTo(ChronoUnit.SECONDS), Duration.ZERO.toSeconds(), it.windowTitle))
        }
        list
    }.also {
        println(it.joinToString("\n"))
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
