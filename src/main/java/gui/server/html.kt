package gui.server

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.apache.commons.csv.*

fun renderPage(): String {
    return createHTML().html {
        head {
            styleLink("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
            styleLink("https://cdn.datatables.net/r/bs-3.3.5/jq-2.1.4,dt-1.10.8/datatables.min.css")
            script(ScriptType.textJScript, "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.0/jquery.min.js")
            script(ScriptType.textJScript, "https://cdn.datatables.net/r/bs-3.3.5/jqc-1.11.3,dt-1.10.8/datatables.min.js")
            script(ScriptType.textJScript, "/data-tables.js")
        }
        body {
            div("container-fluid") {
                attributes["style"] = "margin-top: 20px;"

                div("row") {
                    div("col-md-2") {
                        bootstrapPanel("Data input") {
                            postForm("/json") {
                                encType = FormEncType.multipartFormData
                                //bootstrapInput("input", true)
                                bootstrapInput("json")
                                div("form-group") {
                                    submitInput {
                                        attributes["class"] = "btn btn-success"
                                        value = "Run!"
                                    }
                                }
                            }
                            hr {  }
                            postForm("/csv") {
                                encType = FormEncType.multipartFormData
                                bootstrapInput("csv")
                                div("form-group") {
                                    submitInput {
                                        attributes["class"] = "btn btn-success"
                                        value = "Load"
                                    }
                                }
                            }
                        }
                    }

                    div("col-md-10") {
                        bootstrapPanel("Results") {
                            table("data display table table-striped table-bordered") {
                                attributes["cellspacing"] = "0"
                                attributes["width"] = "100%"
                                thead {
                                    tr {
                                        th { +"#" }
                                        th { +"Row" }
                                        th { +"Column" }
                                        th { +"Source" }
                                        th { +"Operation" }
                                        th { +"Result" }
                                        th { +"Parameter" }
                                    }
                                }
                            }
                        }
                    }
                }

                div("row") {
                    div("col-md-12") {
                        bootstrapPanel("JSON") {
                            code { }
                        }
                    }
                }

            }
        }
    }
}

private fun FORM.bootstrapInput(_name: String, text: Boolean = false) {
    div("form-group") {
        label {
            for_ = _name
            +_name.capitalize()
        }

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

private fun DIV.bootstrapPanel(title: String, body: DIV.() -> Unit) {
    div("panel panel-primary") {
        div("panel-heading") { b { +title } }
        div("panel-body") {
            body()
        }
    }
}
