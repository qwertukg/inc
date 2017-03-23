@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

import base.Log
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

    val server = embeddedJettyServer(8080) {
        routing {
            get("/result") {
                call.respondText(Log.toResult())
            }

            post("/csv") {
                val reader = readCsv(call.request)
                val rows = CSVFormat.EXCEL.parse(reader)
                val minRowSize = rows.first().size()
                cols = rows.first().size() * 2

                Log.fields.clear()

                rows.withIndex().forEach { csvRow ->
                    if (minRowSize != csvRow.value.size()) throw IOException("Min row size [$minRowSize] != current row size [${csvRow.value.size()}]")

                    csvRow.value.withIndex().forEach { csvField ->
                        if (csvField.value.isEmpty()) throw IOException("[] Value can not be empty [${csvRow.index} : ${csvField.index}]")

                        Log.fields.add(Field(csvRow.index, csvField.index, csvField.value))
                    }
                }

                reader.close()
                rows.close()
                call.respondRedirect("/")
            }

            post("/json") {
                val reader = readJson(call.request)
                val rules: Rules = Gson().fromJson(reader, Rules().javaClass)

                Log.fields.forEach { f ->
                    val jsonColumn = rules.columns.withIndex().first { it.index == f.y }.value
                    f.converters.clear()

                    jsonColumn.converters.forEach { c ->
                        val _c = Converter(f.value, c.parameters)
                        _c.name = c.name
                        f.converters.add(_c)
                    }
                }

                Log.fields.forEach { f ->
                    var input = f.value
                    f.converters.forEach { c ->
                        val converted = ConversionList(input, c).convert()
                        input = converted.value
                        c.value = converted.value
                    }
                }

                reader.close()
                call.respondRedirect("/")
            }

            get("/") {
                call.respondText(renderPage(cols), ContentType.Text.Html)
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
