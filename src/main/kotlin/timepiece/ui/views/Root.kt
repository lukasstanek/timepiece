package timepiece.ui.views

import javafx.collections.FXCollections
import timepiece.ActiveWindowPeriod
import timepiece.ActivityService
import tornadofx.*
import java.time.Instant

class Root: View() {

    override val root = vbox {
        tableview<ActiveWindowPeriod> {
            items = ActivityService().getActivityForDate(Instant.now()).observable()

            column("Start", ActiveWindowPeriod::startTime)
            column("End", ActiveWindowPeriod::endTime)
            column("Duration", ActiveWindowPeriod::duaration)
            column("App", ActiveWindowPeriod::app)
            column("Window", ActiveWindowPeriod::windowTitle)
        }
    }
}