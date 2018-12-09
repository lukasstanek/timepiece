package timepiece.data

import timepiece.util.format
import java.time.Duration
import java.time.Instant

data class ActiveWindowPeriod(
    val startTime: Instant,
    var endTime: Instant,
    var duaration: String = Duration.ZERO.format(),
    val app: String,
    val windowTitle: String
)