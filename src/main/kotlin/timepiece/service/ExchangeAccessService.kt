package timepiece.service

import microsoft.exchange.webservices.data.core.ExchangeService
import microsoft.exchange.webservices.data.core.PropertySet
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder
import microsoft.exchange.webservices.data.credential.WebCredentials
import microsoft.exchange.webservices.data.search.CalendarView
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class ExchangeAccessService {
    private val service = ExchangeService(ExchangeVersion.Exchange2010_SP2)

    init {
        val email = "lukas.stanek@iteratec.at"
        val credentials = WebCredentials(email, System.getenv("OWA_PASSWORD"))
        service.credentials = credentials
        service.autodiscoverUrl(email) {
            it.startsWith("https://autodiscover.system-hoster.com")
        }
    }

    fun findAppointments(startDate: LocalDateTime): List<String> {
        val cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar)
        val date = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant())
        val findResults = cf.findAppointments(CalendarView(date, date))
        for (appt in findResults.items) {
            appt.load(PropertySet.FirstClassProperties)
            println(appt.subject)
        }
        return listOf(findResults.items) as List<String>
    }
}