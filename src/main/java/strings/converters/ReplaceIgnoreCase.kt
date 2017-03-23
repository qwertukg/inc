package strings.converters

import base.json.*

class ReplaceIgnoreCase(value: String, parameters: MutableList<Parameter>? = null) : Converter(value, parameters) {
    override var name: String = "replace_ignore_case"

    override fun convert(): ReplaceIgnoreCase {
        var _value = value.toLowerCase()

        parameters?.forEach {
            _value = _value.replace(it.first, it.second)
        }

        value = _value.capitalize()

        return this
    }
}
