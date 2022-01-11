package travel.util

import spray.json.{JsObject, JsString, JsValue}
import travel.dal.model.Vars

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
