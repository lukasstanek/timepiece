package timepiece.tracking.linux

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timepiece.tracking.ActivityTracker
import java.time.Instant
import kotlin.coroutines.suspendCoroutine

class InputDeviceTracker: ActivityTracker {

    var timestampLastInput: Instant = Instant.now()
        get() = field

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
            process.inputStream.bufferedReader().forEachLine {
                if(it.startsWith("EVENT type 3") || it.startsWith("EVENT type 6")){
                    timestampLastInput = Instant.now()
//                println(it)
                }
            }
        }
    }
}