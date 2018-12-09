package timepiece.service

import timepiece.data.Application

class ApplicationParser {
    companion object {
        val intellij = Regex("(?<title>^.* \\[.*\\]) -.*- (?<app>IntelliJ IDEA)\$")
        val chrome = Regex("^(?<title>.*) - (?<app>Google Chrome)\$")
        val regexes = listOf(intellij, chrome)
    }


    fun parse(windowTitle: String): Application {
        val matchEntire = matchRegexes(windowTitle)
        matchEntire ?: return unkownApp(windowTitle)

        val app = matchEntire.groups["app"]
        val title = matchEntire.groups["title"]

        app ?: return unkownApp(windowTitle)
        title ?: return unkownApp(windowTitle)

        return Application(app.value, title.value)
    }

    fun matchRegexes(windowTitle: String): MatchResult? {
        regexes.forEach{
            regex ->
            regex.matchEntire(windowTitle)?.let {
                return it
            }
        }
        return null
    }

    fun unkownApp(windowTitle: String): Application {
        return Application("Unkown", windowTitle)
    }
}