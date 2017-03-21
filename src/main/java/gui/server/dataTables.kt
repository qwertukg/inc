package gui.server

import java.nio.file.Files
import java.nio.file.Paths

fun getContent(localPath: String): String {
    var result = ""
    Files.readAllLines(Paths.get("${System.getProperty("user.dir")}\\src\\main\\java" + localPath)).forEach { result += it + "\n" }
    return result
}
