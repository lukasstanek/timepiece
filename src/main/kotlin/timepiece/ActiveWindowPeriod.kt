package timepiece

import java.time.Instant

data class ActiveWindowPeriod(
    val startTime: Instant,
    var endTime: Instant,
    var duaration: Long = 0,
    val windowTitle: String
) {
    val app = windowTitle.substringAfterLast("- ")

}