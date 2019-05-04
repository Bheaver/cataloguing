package com.bheaver.ngl4.cataloguing.services

import com.bheaver.ngl4.cataloguing.model.{GetMarcDictionarySubFieldsIndicatorsRequest, GetMarcDictionarySubFieldsIndicatorsResponse, IndicatorDictionary, IndicatorValueDictionary, SearchDictionaryByTagRequest, SearchDictionaryByTagResponse, SubFieldDictionary, TagInfoDictionary}
import org.apache.logging.log4j.scala.Logging
import reactor.core.publisher.Mono

import scala.xml._

trait MarcDictionaryService {
  def searchMarcDictionaryByTag(searchDictionaryByTagRequest: SearchDictionaryByTagRequest): Mono[SearchDictionaryByTagResponse]

  def listTagData(request: GetMarcDictionarySubFieldsIndicatorsRequest): Mono[GetMarcDictionarySubFieldsIndicatorsResponse]
}

class UniversalMarcDictionary(xmlEle: Elem) extends MarcDictionaryService with Logging {
  override def searchMarcDictionaryByTag(searchDictionaryByTagRequest: SearchDictionaryByTagRequest): Mono[SearchDictionaryByTagResponse] = {
    Mono.fromSupplier(() => {
      SearchDictionaryByTagResponse((xmlEle \ "Tag").map(nodeEle => ((nodeEle \ "Value").text, (nodeEle \ "Definition").text)).sortBy(item => item._2).toList)
    })
  }

  override def listTagData(request: GetMarcDictionarySubFieldsIndicatorsRequest): Mono[GetMarcDictionarySubFieldsIndicatorsResponse] = {
    Mono.fromSupplier(() => {
      (xmlEle \ "Tag")
        .find(tagNode => (tagNode \ "Value").text.equals(request.tag))
        .map(tagNode => {
          val tagInfoDictionry = TagInfoDictionary((tagNode \ "Definition").text,
            (tagNode \ "Description").text,
            (tagNode \ "UserDefinition").text,
            (tagNode \ "UserDescription").text,
            (tagNode \ "Example").text,
            (tagNode \ "UserExample").text,
            (tagNode \ "Repeat").text.equals("R"),
            (tagNode \ "Value").text)

          val i1List = (tagNode \ "Indicator1" \ "IN")
            .map(indicatorNode => IndicatorValueDictionary((indicatorNode \ "Definition").text,
              (indicatorNode \ "Description").text,
              (indicatorNode \ "UserDefinition").text,
              (indicatorNode \ "UserDescription").text,
              (indicatorNode \ "Value").text))
            .toList
          val i2List = (tagNode \ "Indicator2" \ "IN")
            .map(indicatorNode => IndicatorValueDictionary((indicatorNode \ "Definition").text,
              (indicatorNode \ "Description").text,
              (indicatorNode \ "UserDefinition").text,
              (indicatorNode \ "UserDescription").text,
              (indicatorNode \ "Value").text))
            .toList

          val i1 = IndicatorDictionary((tagNode \ "Indicator1" \ "Definition").text,
            (tagNode \ "Indicator1" \ "Description").text,
            (tagNode \ "Indicator1" \ "UserDefinition").text,
            (tagNode \ "Indicator1" \ "UserDescription").text,
            i1List)

          val i2 = IndicatorDictionary((tagNode \ "Indicator2" \ "Definition").text,
            (tagNode \ "Indicator2" \ "Description").text,
            (tagNode \ "Indicator2" \ "UserDefinition").text,
            (tagNode \ "Indicator2" \ "UserDescription").text,
            i2List)

          val subList = (tagNode \ "SubField" \"SF")
            .map(subFieldNode => SubFieldDictionary((subFieldNode \ "Definition").text,
              (subFieldNode \ "Description").text,
              (subFieldNode \ "UserDefinition").text,
              (subFieldNode \ "UserDescription").text,
              (subFieldNode \ "Example").text,
              (subFieldNode \ "UserExample").text,
              (subFieldNode \ "Repeat").text.equals("R"),
              (subFieldNode \ "Value").text)).toList


          GetMarcDictionarySubFieldsIndicatorsResponse(tagInfoDictionry,subList, i1, i2)
        }).get

      /*json
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
        }).get*/
    })
  }
}