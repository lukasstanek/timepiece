package timepiece.ui.controller

import javafx.collections.ObservableList
import timepiece.ActiveWindowPeriod
import timepiece.ActivityService
import tornadofx.Controller
import tornadofx.observable
import java.time.Instant

class RootController: Controller() {

    private val activityService: ActivityService = ActivityService()

    fun getActivityForDate(): ObservableList<ActiveWindowPeriod> {
        return activityService.getActivityForDate(Instant.now()).observable()
    }
}