package timepiece.ui.controller

import javafx.collections.ObservableList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timepiece.data.ActiveWindowPeriod
import timepiece.service.ActivityService
import timepiece.windowTitleInterval
import tornadofx.Controller
import tornadofx.observable
import java.time.Instant
import java.util.*
import kotlin.concurrent.schedule
import kotlin.coroutines.suspendCoroutine

class RootController: Controller() {

    private val activityService: ActivityService = ActivityService()

    private val activityData: ObservableList<ActiveWindowPeriod>

    init {
        activityData = activityService.getActivityForDate(Instant.now()).observable()
        GlobalScope.launch {
            updateData()
        }
    }

    fun getActivityForDate(): ObservableList<ActiveWindowPeriod> {
        return activityData
    }

    suspend fun updateData(){
        return suspendCoroutine {
            Timer().schedule(windowTitleInterval, windowTitleInterval){
                activityData.setAll(activityService.getActivityForDate(Instant.now()))
            }
        }
    }


}