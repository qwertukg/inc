package base.json

data class Column(
        val index: Int,
        var name: String = "default_name",
        val type: String = "default_type",
        var results: MutableList<Result> = mutableListOf(),
        val converters: MutableList<Converter> = mutableListOf()
)

data class Result(
        val index: Int,
        var value: String,
        val converters: MutableList<Converter> = mutableListOf()
)
