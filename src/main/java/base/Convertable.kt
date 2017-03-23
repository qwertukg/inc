package base

import base.json.Parameter

interface Convertable {
    var name: String
    var value: String
    val parameters: MutableList<Parameter>?
    fun convert(): Convertable
}
