package timepiece.tracking.linux

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timepiece.*
import timepiece.data.ActiveWindowRecord
import timepiece.tracking.ActivityTracker
import timepiece.util.runCommand
import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.concurrent.schedule
import kotlin.coroutines.suspendCoroutine

class ActiveWindowTracker(private val inputDeviceTracker: InputDeviceTracker):
    ActivityTracker {

    override fun trackActivity() {
        GlobalScope.launch {
            periodicallyCheckWindowTitle()
        }
    }

    private suspend fun periodicallyCheckWindowTitle() {
        return suspendCoroutine {
            Timer().schedule(0, windowTitleInterval) {
                "xdotool getwindowfocus getwindowname".runCommand()?.trim()?.apply {
                    val file = File(logFile)

                    val activeWindowRecord = ActiveWindowRecord(Instant.now(), this)

                    var activity = activeWindowRecord.windowTitle

                    val record = "${activeWindowRecord.date},$activity\n"
                    file.appendText(record)
                }

            }
        }
    }
}