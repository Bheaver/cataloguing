package com.bheaver.ngl4.cataloguing.services

import com.bheaver.ngl4.cataloguing.model.{GetMarcDictionarySubFieldsIndicatorsRequest, GetMarcDictionarySubFieldsIndicatorsResponse, IndicatorDictionary, IndicatorValueDictionary, SearchDictionaryByTagRequest, SearchDictionaryByTagResponse, SubFieldDictionary}
import org.apache.logging.log4j.scala.Logging
import play.api.libs.json.{JsValue, Json}
import reactor.core.publisher.Mono

import scala.io.Source

trait MarcDictionaryService {
  def searchMarcDictionaryByTag(searchDictionaryByTagRequest: SearchDictionaryByTagRequest): Mono[SearchDictionaryByTagResponse]

  def listTagData(request: GetMarcDictionarySubFieldsIndicatorsRequest): Mono[GetMarcDictionarySubFieldsIndicatorsResponse]
}

class UniversalMarcDictionary(json: List[JsValue]) extends MarcDictionaryService with Logging {
  override def searchMarcDictionaryByTag(searchDictionaryByTagRequest: SearchDictionaryByTagRequest): Mono[SearchDictionaryByTagResponse] = {
    Mono.fromSupplier(() => {
      SearchDictionaryByTagResponse(json.map(jsval =>
        ((jsval \ "Value").as[String], (jsval \ "Definition").as[String])
      ).sortBy(item => item._2))
    })
  }

  override def listTagData(request: GetMarcDictionarySubFieldsIndicatorsRequest): Mono[GetMarcDictionarySubFieldsIndicatorsResponse] = {
    Mono.fromSupplier(() => {
      json
        .find(jsvalMain => (jsvalMain \ "Value").as[String].equals(request.tag))
        .map(jsvalMain => {
          println(jsvalMain)
          val indi1 = (jsvalMain \ "Indicator1" \ "IN").as[List[JsValue]].map(jsval => {
            IndicatorValueDictionary((jsval \ "Definition").as[String], (jsval \ "Description").as[String], (jsval \ "UserDefinition").as[String], (jsval \ "UserDescription").as[String], (jsval \ "Value").as[String])
          })
          val indi2 = (jsvalMain \ "Indicator2" \ "IN").as[List[JsValue]].map(jsval => {
            IndicatorValueDictionary((jsval \ "Definition").as[String], (jsval \ "Description").as[String], (jsval \ "UserDefinition").as[String], (jsval \ "UserDescription").as[String], (jsval \ "Value").as[String])
          })
          val indicator1 = IndicatorDictionary((jsvalMain \ "Indicator1" \ "Definition").as[String],
            (jsvalMain \ "Indicator1" \ "Description").as[String],
            (jsvalMain \ "Indicator1" \ "UserDefinition").as[String],
            (jsvalMain \ "Indicator1" \ "UserDescription").as[String],
            indi1)
          val indicator2 = IndicatorDictionary((jsvalMain \ "Indicator2" \ "Definition").as[String],
            (jsvalMain \ "Indicator2" \ "Description").as[String],
            (jsvalMain \ "Indicator2" \ "UserDefinition").as[String],
            (jsvalMain \ "Indicator2" \ "UserDescription").as[String],
            indi2)
          val subFieldsList = (jsvalMain \ "SubField").as[List[JsValue]].map(jsval => {
            SubFieldDictionary((jsval \ "Definition").as[String],
              (jsval \ "Description").as[String],
              (jsval \ "UserDefinition").as[String],
              (jsval \ "UserDescription").as[String],
              (jsval \ "Example").as[String],
              (jsval \ "UserExample").as[String],
              (jsval \ "Repeat").as[Boolean],
              (jsval \ "Value").as[String]
            )
          })
          GetMarcDictionarySubFieldsIndicatorsResponse(subFieldsList, indicator1, indicator2)
        }).get
    })
  }
}