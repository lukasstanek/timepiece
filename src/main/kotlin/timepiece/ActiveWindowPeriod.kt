package timepiece

import java.time.Instant
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalField

data class ActiveWindowPeriod(
    val startTime: Instant,
    var endTime: Instant,
    var duaration: Long = 0,
    val windowTitle: String
) {
    val app = windowTitle.substringAfterLast("- ")

}