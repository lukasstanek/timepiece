package timepiece.ui.views

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventType
import javafx.scene.Scene
import timepiece.ActiveWindowPeriod
import timepiece.ActivityService
import timepiece.ui.controller.RootController
import timepiece.util.getTimeOfDay
import tornadofx.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Root: View() {
    val controller: RootController by inject()

    val records: ObservableList<ActiveWindowPeriod>

    init {
        records = controller.getActivityForDate()
        primaryStage.isMaximized = true
    }

//    val tableView =

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
            readonlyColumn("Duration (s)", ActiveWindowPeriod::duaration)
            readonlyColumn("App", ActiveWindowPeriod::app)
            readonlyColumn("Window", ActiveWindowPeriod::windowTitle)

            resizeColumnsToFitContent()
            fitToParentSize()
        }.also {
            primaryStage.sizeToScene()
        }
    }
}