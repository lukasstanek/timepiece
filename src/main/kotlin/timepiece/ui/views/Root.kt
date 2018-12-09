package timepiece.ui.views

import javafx.collections.ObservableList
import timepiece.data.ActiveWindowPeriod
import timepiece.ui.controller.RootController
import timepiece.util.getTimeOfDay
import tornadofx.*
import java.time.LocalDateTime
import java.time.ZoneId

class Root: View("Activity Overview - Timepiece") {
    val controller: RootController by inject()

    val records: ObservableList<ActiveWindowPeriod>

    init {
        records = controller.getActivityForDate()
    }

    override val root = vbox {
        tableview(records) {
            column("Date", String::class){
                value { param ->
                    val date = LocalDateTime.ofInstant(param.value.startTime, ZoneId.systemDefault())
                    "${date.dayOfMonth}.${date.monthValue}.${date.year}"
                }
            }
            column("Start", String::class){
                value {param ->
                    param.value.startTime.getTimeOfDay()
                }
            }
            column("End", String::class){
                value {param ->
                    param.value.endTime.getTimeOfDay()
                }
            }
            readonlyColumn("Duration", ActiveWindowPeriod::duaration)
            readonlyColumn("App", ActiveWindowPeriod::app)
            readonlyColumn("Window", ActiveWindowPeriod::windowTitle)

            resizeColumnsToFitContent()
            fitToParentSize()

            scrollTo(records.size - 1)

            records.onChange {
                this.scrollTo(records.size - 1)
            }
        }.also {
            primaryStage.sizeToScene()
        }
    }
}