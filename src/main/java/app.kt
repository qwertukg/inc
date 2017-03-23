@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

import base.Storage
import base.json.*
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
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

fun main(args: Array<String>) {
    //val reader = FileReader("${System.getProperty("user.dir")}\\src\\main\\java\\data.base.json")

    var cols = 0

    var csvColor: String = "#c90"
    var jsonColor: String = "#c00"
    var runColor: String = "#c00"

    val server = embeddedJettyServer(8080) {
        routing {
            post("/csv") {
                val reader = readCsv(call.request)
                val rows = CSVFormat.EXCEL.parse(reader)
                val minRowSize = rows.first().size()
                cols = rows.first().size() * 3

                Storage.fields.clear()

                rows.withIndex().forEach { csvRow ->
                    if (minRowSize != csvRow.value.size()) throw IOException("Min row size [$minRowSize] must be equal current row size [${csvRow.value.size()}]")

                    csvRow.value.withIndex().forEach { csvField ->
                        if (csvField.value.isEmpty()) throw IOException("Value can not be empty [${csvRow.index} : ${csvField.index}]")

                        Storage.fields.add(Field(csvRow.index, csvField.index, csvField.value))
                    }
                }

                csvColor = "#090"
                jsonColor = "#c90"
                runColor = "#c00"

                reader.close()
                rows.close()
                call.respondRedirect("/")
            }

            post("/json") {
                val reader = readJson(call.request)
                val rules: Rules = Gson().fromJson(reader, Rules().javaClass)

                Storage.fields.forEach { f ->
                    val jsonColumn = rules.columns.withIndex().first { it.index == f.y }.value
                    f.converters.clear()

                    jsonColumn.converters.forEach { c ->
                        val converter = Converter(f.value, c.parameters)
                        converter.name = c.name
                        f.converters.add(converter)
                    }
                }

                csvColor = "#090"
                jsonColor = "#090"
                runColor = "#c90"

                reader.close()
                call.respondRedirect("/")
            }

            post("/run") {
                Storage.fields.forEach { f ->
                    var input = f.value
                    f.converters.forEach { c ->
                        val converted = ConversionList(input, c).convert()
                        input = converted.value
                        c.value = converted.value
                    }
                }

                csvColor = "#090"
                jsonColor = "#090"
                runColor = "#090"

                call.respondRedirect("/")
            }

            get("/") {
                call.respondText(renderPage(cols, csvColor, jsonColor, runColor), ContentType.Text.Html)
            }

            get("/data") {
                call.respondText(Storage.toData(), ContentType.Application.Json)
            }

            get("/data-tables.js") {
                val js = getContent("\\gui\\client\\dataTables.js")
                call.respondText(js, ContentType.Application.Json)
            }

            get("/result") {
                call.respondText(Storage.toResult())
            }
        }
    }

    server.start(true)
}
