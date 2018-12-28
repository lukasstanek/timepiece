package timepiece.tracking.linux

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timepiece.timeUntilInactivity
import timepiece.tracking.ActivityTracker
import java.io.File
import java.time.Duration
import java.time.Instant
import kotlin.coroutines.suspendCoroutine

class InputInterval(val start: Instant, val end: Instant, wasActive: Boolean)

class InputDeviceTracker: ActivityTracker {

    var timestampLastInput: Instant = Instant.now()
    val logfile = "input.log"

    override fun trackActivity(){
        GlobalScope.launch {
            listenForInputEvents()
        }
    }

    private suspend fun listenForInputEvents() {
        val process = ProcessBuilder("xinput", "test-xi2", "--root")
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .start()

        return suspendCoroutine {
            File(logfile).printWriter().use { printer ->
                process.inputStream.bufferedReader().forEachLine { event ->
                    if (event.startsWith("EVENT type 3") || event.startsWith("EVENT type 6")) {

                        val currentTimestamp = Instant.now()
                        if (Duration.between(
                                timestampLastInput,
                                currentTimestamp
                            ).toMillis() / 1000 > (timeUntilInactivity)
                        ) {
                            printer.println("$currentTimestamp,INACTIVE")
                        } else {
                            printer.println("$currentTimestamp,ACTIVE")
                        }
                    }
                }
            }
        }
    }
}