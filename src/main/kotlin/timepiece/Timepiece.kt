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
import timepiece.tracking.ActiveWindowTracker
import timepiece.tracking.InputDeviceTracker
import timepiece.ui.TimepieceUi
import tornadofx.launch

const val ACTIVITY_INACTIVE = "INACTIVE"

const val logFile = "./window.log"
const val windowTitleInterval: Long = 60 * 1000
const val timeUntilInactivity = 60 * 5 // in secs


fun main(args: Array<String>){
    GlobalScope.launch {
        launchUi()
    }
    if (args.size > 0 && args[0] == "report"){
        reportOnTimeTracked()
    }else{
        trackActivity()
    }

    launch<TimepieceUi>()

}

suspend fun launchUi(){
    return suspendCoroutine {
    }
}

fun trackActivity() {
    val inputDeviceTracker = InputDeviceTracker()
    val activeWindowTracker = ActiveWindowTracker(inputDeviceTracker)

    inputDeviceTracker.trackActivity()
    activeWindowTracker.trackActivity()
}






fun reportOnTimeTracked() {


}


