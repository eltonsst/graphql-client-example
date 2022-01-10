package travel

import spray.json.{JsObject, JsString, JsValue}

object GraphQLRequestBuilder {
  def build(
      queryAst: String,
      vars: Option[Vars],
      converter: Option[Vars] => Option[JsValue]
  ): String =
    JsObject(
      Map(
        "query" -> JsString(queryAst),
        "variables" -> converter.apply(vars).getOrElse(JsObject.empty)
      )
    ).toString
}
