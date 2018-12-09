package timepiece.service

import timepiece.data.ActiveWindowPeriod
import timepiece.data.ActiveWindowRecord
import timepiece.logFile
import timepiece.util.format
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class ActivityService {

    fun getActivityForDate(date: Instant): List<ActiveWindowPeriod> {
        val now = date.truncatedTo(ChronoUnit.DAYS)

        return File(logFile).readLines().map {
            val split = it.split(",")
            ActiveWindowRecord(Instant.parse(split[0]), split[1])
        }.filter {
            now == it.date.truncatedTo(ChronoUnit.DAYS)
        }.fold(mutableListOf()){
                list, it ->
            val lastOrNull = list.lastOrNull()
            val itereeAppAndTitle = ApplicationParser().parse(it.windowTitle)

            if(lastOrNull == null){
                list.add(
                    ActiveWindowPeriod(
                        it.date.truncatedTo(ChronoUnit.SECONDS),
                        it.date.truncatedTo(ChronoUnit.SECONDS),
                        Duration.ZERO.format(),
                        itereeAppAndTitle.name,
                        itereeAppAndTitle.windowTitle
                    )
                )
                return@fold list
            }

            lastOrNull.endTime = it.date.truncatedTo(ChronoUnit.SECONDS)
            lastOrNull.duaration = Duration.between(lastOrNull.startTime, lastOrNull.endTime).format()

            if(lastOrNull.app != itereeAppAndTitle.name || lastOrNull.windowTitle != itereeAppAndTitle.windowTitle){
                list.add(
                    ActiveWindowPeriod(
                        it.date.truncatedTo(ChronoUnit.SECONDS),
                        it.date.truncatedTo(ChronoUnit.SECONDS),
                        Duration.ZERO.format(),
                        itereeAppAndTitle.name,
                        itereeAppAndTitle.windowTitle
                    )
                )

            }
            list
        }
    }

    fun printActivity(it: List<ActiveWindowPeriod>){
        val colWidths = mutableListOf(0,0)
        it.forEach {
            if (it.duaration.toString().length > colWidths[0]) colWidths[0] = it.duaration.toString().length
            if (it.app.length > colWidths[1]) colWidths[1] = it.app.length
        }

        it.forEach{
            println("${it.endTime} | ${it.duaration.toString().padEnd(colWidths[0], ' ')} | ${it.app.padEnd(colWidths[1], ' ')}|${it.windowTitle}")
        }
//        val durTotal = it.fold(0L) { a, b -> a + b.duaration}
//        println(durTotal / 60)
    }
}