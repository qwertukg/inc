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
                    row.add("[${it.value}]")
                    if (!it.converters.isEmpty()) {
                        val v = it.converters.last().value
                        if (v != it.value) row.add("[${it.converters.last().value}]")
                        else row.add("")
                    }
                    else row.add("")
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
