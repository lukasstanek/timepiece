package timepiece

import com.sun.jna.Platform
import timepiece.service.ExchangeAccessService
import timepiece.tracking.ActivityTracker
import timepiece.tracking.linux.ActiveWindowTracker
import timepiece.tracking.linux.InputDeviceTracker
import timepiece.tracking.windows.ActiveWindowTrackerWindows
import timepiece.ui.TimepieceUi
import tornadofx.launch


const val ACTIVITY_INACTIVE = "INACTIVE"

const val logFile = "./window.log"
const val windowTitleInterval: Long = 60 * 1000
const val timeUntilInactivity = 60 * 5 // in secs


fun main(args: Array<String>) {
    trackActivity()
    ExchangeAccessService()
    launch<TimepieceUi>()
}

fun trackActivity() {
    when (Platform.getOSType()) {
        Platform.WINDOWS -> getTrackerWindows().forEach(ActivityTracker::trackActivity)
        Platform.LINUX -> getTrackerLinux().forEach(ActivityTracker::trackActivity)
        else -> println("Operating system not supported")
    }
}

fun getTrackerWindows(): List<ActivityTracker> {
    return listOf(ActiveWindowTrackerWindows())
}

fun getTrackerLinux(): List<ActivityTracker> {
    val inputDeviceTracker = InputDeviceTracker()
    val activeWindowTracker = ActiveWindowTracker(inputDeviceTracker)
    return listOf(inputDeviceTracker, activeWindowTracker)
}

