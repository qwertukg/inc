package gui.server

import com.google.gson.stream.JsonReader
import org.jetbrains.ktor.application.ApplicationRequest
import org.jetbrains.ktor.request.*
import java.io.*

fun readCsv(request: ApplicationRequest): InputStreamReader {
    val multiPartData = request.content.get<MultiPartData>()

    multiPartData.parts.forEach {
        part -> when (part) {
            is PartData.FileItem -> {
                return InputStreamReader(part.streamProvider.invoke())
            }
        }
    }

    throw IOException("SCV file not found")
}

fun readJson(request: ApplicationRequest): JsonReader {
    val multiPartData = request.content.get<MultiPartData>()

    multiPartData.parts.forEach {
        part -> when (part) {
            is PartData.FileItem -> {
                return JsonReader(InputStreamReader(part.streamProvider.invoke()))
            }
        }
    }

    throw IOException("JSON file not found")
}
