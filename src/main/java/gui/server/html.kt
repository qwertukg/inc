package gui.server

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.apache.commons.csv.*

fun renderPage(cols: Int): String {
    return createHTML().html {
        head {
            styleLink("https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css")
            script(ScriptType.textJScript, "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.0/jquery.min.js")
            script(ScriptType.textJScript, "https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js")
            script(ScriptType.textJScript, "/data-tables.js")
        }
        body {
            attributes["style"] = "font-family: segoe ui;"
            div("container-fluid") {
                div("row") {
                    div("col-md-12") {
                        bootstrapPanel("Data input") {
                            postForm("/csv", FormEncType.multipartFormData) {
                                attributes["style"] = "float: left;"
                                bootstrapInput("csv")
                            }
                            postForm("/json", FormEncType.multipartFormData) {
                                bootstrapInput("json")
                            }
                            div { attributes["style"] = "clear: left;" }
                        }
                    }
                }

                div("row") {
                    div("col-md-12") {
                        bootstrapPanel("Results") {
                            table("data display table table-striped table-bordered") {
                                attributes["cellspacing"] = "0"
                                attributes["width"] = "100%"
                                thead {
                                    tr {
                                        th {
                                            attributes["style"] = "text-align: left;"
                                            +"#"
                                        }
                                        for (i in 1..cols) {
                                            th {
                                                attributes["style"] = "text-align: left;"
                                                +i.toString()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

private fun FORM.bootstrapInput(_name: String, text: Boolean = false) {
    submitInput {
        attributes["style"] = "float: left;"
        value = "Load ${_name.capitalize()}"
    }

    div("form-group") {
        attributes["style"] = "float: left;"

        if (text) textInput {
            attributes["id"] = _name
            attributes["class"] = "form-control"
            name = _name
        } else fileInput {
            attributes["id"] = _name
            name = _name
        }
    }
}

private fun DIV.bootstrapPanel(title: String, block: DIV.() -> Unit) {
    div { +title.toUpperCase() }
    block()
}
