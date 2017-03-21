package base

import base.json.Column
import base.json.Parameter
import base.json.Result
import com.google.gson.Gson

object Log {
    val columns = mutableListOf<Column>()

    /*fun toRows(): MutableList<MutableList<Result>> {
        val rows = mutableListOf<MutableList<Result>>()

        columns.forEach { column ->
            column.results.forEach { result ->
                //rows.add(result.index, )
            }
        }

        return rows
    }*/

    fun toData(): String {
        val data = mutableListOf<MutableList<String>>()

        columns.forEach { column ->
            val columnId = (column.index + 1).toString()
            column.results.forEach { result ->
                val rowId = (result.index + 1).toString()
                result.converters.forEach { converter ->
                    val id = (data.size + 1).toString()
                    data.add(mutableListOf(id, rowId, columnId, converter.source, converter.name, converter.value, "NOT IMPLEMENTED"))
                }
            }
        }

        return "{\"data\" : ${Gson().toJson(data)}}"
    }

    fun toResult(): String {
        return Gson().toJson(columns)
    }

}
