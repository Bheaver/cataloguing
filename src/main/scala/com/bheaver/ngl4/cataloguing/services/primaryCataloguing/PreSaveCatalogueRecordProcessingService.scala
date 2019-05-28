package com.bheaver.ngl4.cataloguing.services.primaryCataloguing

import java.util

import scala.collection.JavaConverters._
import com.bheaver.ngl4.cataloguing.model.GetMarcDictionarySubFieldsIndicatorsRequest
import com.bheaver.ngl4.cataloguing.model.primaryCataloguing.{FieldDescription, PreProcessCatalogueRecordToDisplayRequest, PreProcessCatalogueRecordToDisplayResponse, SubFieldDescription}
import com.bheaver.ngl4.cataloguing.services.MarcDictionaryService
import com.bheaver.ngl4.util.mongoUtils.Database
import reactor.core.publisher.{Flux, Mono}

import scala.collection.mutable.ListBuffer

trait PreSaveCatalogueRecordProcessingService {
  def preProcessRecordToDisplay(request: PreProcessCatalogueRecordToDisplayRequest): Mono[PreProcessCatalogueRecordToDisplayResponse]
}

class PreSaveCatalogueRecordProcessingServiceImpl(marcDictionaryService: MarcDictionaryService,
                                                  database: Database) extends PreSaveCatalogueRecordProcessingService {
  override def preProcessRecordToDisplay(request: PreProcessCatalogueRecordToDisplayRequest): Mono[PreProcessCatalogueRecordToDisplayResponse] = {
    val mongoDb = database.getDatabase(request.libCode)
    val scalaListmono = request.recordModel.dataFields.map(dataField => {
      val dictDetailsMono = marcDictionaryService.listTagData(GetMarcDictionarySubFieldsIndicatorsRequest(request.libCode, dataField.tag))
      dictDetailsMono.map[FieldDescription](dictDetails => {
        val subFieldDescList = dataField.subFields.map(subField => {
          dictDetails.subFields.find(dicSubField => dicSubField.value == subField.identifier.toString).map(subDefFound => {
            SubFieldDescription(subField.identifier.toString,
              subField.data,
              Option(subDefFound.userExample).getOrElse(subDefFound.example),
              Option(subDefFound.userDefinition).getOrElse(subDefFound.definition),
              Option(subDefFound.userDescription).getOrElse(subDefFound.description))
          }).getOrElse(???)
        })
        FieldDescription(dataField.tag,
          dataField.indicator1,
          dataField.indicator2,
          subFieldDescList,
          Option(dictDetails.tagInfoDictionary.userDefinition).getOrElse(dictDetails.tagInfoDictionary.definition),
          Option(dictDetails.tagInfoDictionary.userDescription).getOrElse(dictDetails.tagInfoDictionary.description),
          Option(dictDetails.tagInfoDictionary.userExample).getOrElse(dictDetails.tagInfoDictionary.example),
          "", "")
      })
    })
    val x: util.List[Mono[FieldDescription]] = scalaListmono.asJava
    Flux.concat(x).collectList().map(liFieldDesc => PreProcessCatalogueRecordToDisplayResponse(liFieldDesc.asScala.toList))
  }
}