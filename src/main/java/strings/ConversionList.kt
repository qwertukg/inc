package strings

import base.Convertable
import base.Log
import strings.converters.*

class ConversionList(val value: String, val converter: Convertable) {
    private val converters = listOf<Convertable>(
            Trim(value),
            Remove(value, converter.parameters),
            Replace(value, converter.parameters),
            ToUpper(value)
    )

    fun convert(): Convertable {
        return converters.first { it.name == converter.name }.convert()
    }
}
