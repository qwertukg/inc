@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

import base.Log
import base.json.Column
import base.json.Converter
import base.json.Rules
import base.json.Result
import com.google.gson.Gson
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.jetty.embeddedJettyServer
import org.jetbrains.ktor.request.*
import org.jetbrains.ktor.response.*
import org.jetbrains.ktor.routing.*
import gui.server.*
import strings.ConversionList
import org.apache.commons.csv.CSVFormat
import java.io.IOException

fun main(args: Array<String>) {
    //val reader = FileReader("${System.getProperty("user.dir")}\\src\\main\\java\\data.base.json")

    val server = embeddedJettyServer(8080) {
        routing {
            get("/result") {
                call.respondText(Log.toResult())
            }

            post("/csv") {
                val reader = readCsv(call.request)
                val rows = CSVFormat.EXCEL.parse(reader)
                val minRowSize = rows.first().size()
                Log.columns.clear()

                rows.withIndex().forEach { csvRow ->
                    if (minRowSize != csvRow.value.size()) throw IOException("Min row size [$minRowSize] != current row size [${csvRow.value.size()}]")

                    csvRow.value.withIndex().forEach { csvField ->
                        if (csvField.value.isEmpty()) throw IOException("[] Value can not be empty [${csvRow.index} : ${csvField.index}]")

                        if (!Log.columns.any { it.index == csvField.index }) Log.columns.add(csvField.index, Column(csvField.index))

                        Log.columns[csvField.index].results.add(csvRow.index, Result(csvRow.index, csvField.value))
                    }
                }

                reader.close()
                rows.close()
                call.respondRedirect("/")
            }

            post("/json") {
                val reader = readJson(call.request)
                val rules: Rules = Gson().fromJson(reader, Rules().javaClass)

                rules.columns.withIndex().forEach { column ->
                    val storedColumn = Log.columns.first { it.index == column.index }

                    storedColumn.results.forEach { result ->
                        var input = result.value
                        result.converters.clear()

                        column.value.converters.forEach { converter ->
                            val converted = ConversionList(input, converter).convert()

                            if (converted.source != converted.value) {
                                val resultConverter = Converter(converted.value, converted.parameters)
                                resultConverter.name = converted.name
                                resultConverter.source = converted.source
                                result.converters.add(resultConverter)

                                input = converted.value
                            }
                        }

                    }
                }

                reader.close()
                call.respondRedirect("/")
            }

            get("/") {
                call.respondText(renderPage(), ContentType.Text.Html)
            }

            get("/data") {
                call.respondText(Log.toData(), ContentType.Application.Json)
            }

            get("/data-tables.js") {
                val js = getContent("\\gui\\client\\dataTables.js")
                call.respondText(js, ContentType.Application.Json)
            }


        }
    }

    server.start(true)

    /*var result = strings.ConversionList("  Daniil  ").stringConverters.first { it.name == "trim" }.convert()
    result = strings.ConversionList(result.value, listOf(strings.__Parameter("a", "@"))).stringConverters.first { it.name == "replace" }.convert()

    val r = result*/
}
