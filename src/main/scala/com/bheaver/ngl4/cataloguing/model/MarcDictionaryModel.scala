package com.bheaver.ngl4.cataloguing.model

import play.api.libs.json.{JsArray, JsValue, Json, Writes}

class MarcDictionaryModel {

}

case class SearchDictionaryByTagRequest(val libCode: String)

case class SearchDictionaryByTagResponse(val list: List[(String, String)])

case class GetMarcDictionarySubFieldsIndicatorsRequest(val libCode: String, val tag: String)

case class GetMarcDictionarySubFieldsIndicatorsResponse(val tagInfoDictionary: TagInfoDictionary,
                                                        val subFields: List[SubFieldDictionary],
                                                        val indicator1: IndicatorDictionary,
                                                        val indicator2: IndicatorDictionary)

case class SubFieldDictionary(val definition: String,
                              val description: String,
                              val userDefinition: String,
                              val userDescription: String,
                              val example: String,
                              val userExample: String,
                              val repeat: Boolean,
                              val value: String)

case class IndicatorValueDictionary(val definition: String,
                                    val description: String,
                                    val userDefinition: String,
                                    val userDescription: String,
                                    val value: String)

case class IndicatorDictionary(val definition: String,
                               val description: String,
                               val userDefinition: String,
                               val userDescription: String,
                               val indicator: List[IndicatorValueDictionary])


case class TagInfoDictionary(val definition: String,
                             val description: String,
                             val userDefinition: String,
                             val userDescription: String,
                             val example: String,
                             val userExample: String,
                             val repeat: Boolean,
                             val value: String)

object MarcDictionaryModelWrites {

  implicit val searchDictionaryByTagResponseWrites = new Writes[SearchDictionaryByTagResponse] {
    override def writes(o: SearchDictionaryByTagResponse): JsValue = new JsArray(o.list.map(item => {
      Json.obj(
        "tag" -> item._1,
        "definition" -> item._2
      )
    }).toIndexedSeq)
  }
  implicit val tagInfoDictionaryWrites = new Writes[TagInfoDictionary] {
    override def writes(o: TagInfoDictionary): JsValue = Json.obj(
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
      "tag" -> o.tagInfoDictionary,
      "indicator1" -> o.indicator1,
      "indicator2" -> o.indicator2,
      "subFields" -> o.subFields
    )
  }
}