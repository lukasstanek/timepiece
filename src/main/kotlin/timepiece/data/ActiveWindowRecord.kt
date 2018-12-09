package timepiece.data

import java.time.Instant

data class ActiveWindowRecord(
    val date: Instant,
    val windowTitle: String
)