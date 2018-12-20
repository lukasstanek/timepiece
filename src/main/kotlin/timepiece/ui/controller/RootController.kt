package timepiece.ui.controller

import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import timepiece.data.ActiveWindowPeriod
import timepiece.service.ActivityService
import timepiece.windowTitleInterval
import tornadofx.Controller
import tornadofx.observable
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.concurrent.schedule

class RootController: Controller() {

    var currentDayInstant = Instant.now()
    var currentDay: SimpleStringProperty = SimpleStringProperty(currentDayInstant.truncatedTo(ChronoUnit.DAYS).toString())

    private val activityService: ActivityService = ActivityService()

    private val activityData: ObservableList<ActiveWindowPeriod>

    init {
        activityData = activityService.getActivityForDate(Instant.now()).observable()
        updateDataPeriodically()
    }

    fun getActivityForDate(): ObservableList<ActiveWindowPeriod> {
        return activityData
    }

    private fun updateDataPeriodically(){
        Timer().schedule(windowTitleInterval, windowTitleInterval){
            updateData()
        }
    }

    private fun updateData(){
        runAsync {
            activityService.getActivityForDate(currentDayInstant)
        } ui {
            activityData.setAll(it)
        }
    }

    fun showPreviousDay(){
        setCurrentDayAndInstant(currentDayInstant.minus(1, ChronoUnit.DAYS))
        updateData()
        println("new day ${currentDayInstant}")
    }

    fun showNextDay() {
        setCurrentDayAndInstant(currentDayInstant.plus(1, ChronoUnit.DAYS))
        updateData()
        println("new day ${currentDayInstant}")
    }

    fun setCurrentDayAndInstant(instant: Instant){
        currentDayInstant = instant;
        currentDay.value = currentDayInstant.truncatedTo(ChronoUnit.DAYS).toString()
    }

}