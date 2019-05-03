package com.bheaver.ngl4.cataloguing.model

import play.api.libs.json.{JsArray, JsValue, Json, Writes}

class MarcDictionaryModel {

}

case class SearchDictionaryByTagRequest(libCode: String)

case class SearchDictionaryByTagResponse(list: List[(String, String)])

case class GetMarcDictionarySubFieldsIndicatorsRequest(libCode: String, tag: String)

case class GetMarcDictionarySubFieldsIndicatorsResponse(subFields: List[SubFieldDictionary], indicator1: IndicatorDictionary, indicator2: IndicatorDictionary)

case class SubFieldDictionary(definition: String,
                              description: String,
                              userDefinition: String,
                              userDescription: String,
                              example: String,
                              userExample: String,
                              repeat: Boolean,
                              value: String)

case class IndicatorValueDictionary(definition: String,
                                    description: String,
                                    userDefinition: String,
                                    userDescription: String,
                                    value: String)

case class IndicatorDictionary(definition: String,
                               description: String,
                               userDefinition: String,
                               userDescription: String,
                               indicator: List[IndicatorValueDictionary])


object MarcDictionaryModelWrites {

  implicit val searchDictionaryByTagResponseWrites = new Writes[SearchDictionaryByTagResponse] {
    override def writes(o: SearchDictionaryByTagResponse): JsValue = new JsArray(o.list.map(item => {
      Json.obj(
        "tag" -> item._1,
        "definition" -> item._2
      )
    }).toIndexedSeq)
  }
  implicit val subFieldDictionaryWrites = new Writes[SubFieldDictionary] {
    override def writes(o: SubFieldDictionary): JsValue = Json.obj(
      "definition" -> o.definition,
      "description" -> o.description,
      "userDefinition" -> o.userDefinition,
      "userDescription" -> o.userDescription,
      "examples" -> o.example,
      "userExamples" -> o.userExample,
      "repeat" -> o.repeat,
      "value" -> o.value
    )
  }
  implicit val indicatorValueDictionaryWrites = new Writes[IndicatorValueDictionary] {
    override def writes(o: IndicatorValueDictionary): JsValue = Json.obj(
      "definition" -> o.definition,
      "description" -> o.description,
      "userDefinition" -> o.userDefinition,
      "userDescription" -> o.userDescription,
      "value" -> o.value
    )
  }
  implicit val indicatorDictionaryWrites = new Writes[IndicatorDictionary] {
    override def writes(o: IndicatorDictionary): JsValue = Json.obj(
      "definition" -> o.definition,
      "description" -> o.description,
      "userDefinition" -> o.userDefinition,
      "userDescription" -> o.userDescription,
      "indicator" -> o.indicator
    )
  }
  implicit val getMarcDictionarySubFieldsIndicatorsResponseWrites = new Writes[GetMarcDictionarySubFieldsIndicatorsResponse] {
    override def writes(o: GetMarcDictionarySubFieldsIndicatorsResponse): JsValue = Json.obj(
      "subFields" -> o.subFields,
      "indicator1" -> o.indicator1,
      "indicator2" -> o.indicator2
    )
  }
}