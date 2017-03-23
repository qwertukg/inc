package strings

import base.Convertable
import strings.converters.*

class ConversionList(val value: String, val converter: Convertable) {
    val converters = listOf<Convertable>(
            Trim(value),
            Remove(value, converter.parameters),
            Replace(value, converter.parameters),
            ReplaceIgnoreCase(value, converter.parameters),
            ToUpper(value)
    )

    fun convert(): Convertable {
        return converters.first { it.name == converter.name }.convert()
    }
}
