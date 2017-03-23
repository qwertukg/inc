package base

import base.json.Field
import com.google.gson.Gson

object Storage {
    val fields = mutableListOf<Field>()

    fun toData(): String {
        val data = mutableListOf<MutableList<String>>()

        fields.forEach { f ->
            if (f.y == 0) {
                val row = mutableListOf(f.x.toString())
                fields.filter { it.x == f.x }.forEach {
                    var lastValue: String = it.value
                    var source = it.value
                    var arrow = ""
                    var result = ""

                    if (!it.converters.isEmpty()) lastValue = it.converters.last().value

                    if (it.value != lastValue) {
                        source = "<pre class='source'>" + it.value + "</pre>"
                        arrow = "&rarr;"
                        result = "<pre class='result'>" + it.converters.last().value + "</pre>"
                    }

                    row.add(source)
                    row.add(arrow)
                    row.add(result)
                }

                data.add(f.x, row)
            }
        }

        return "{\"data\" : ${Gson().toJson(data)}}"
    }

    fun toResult(): String {
        return Gson().toJson(fields)
    }

}
