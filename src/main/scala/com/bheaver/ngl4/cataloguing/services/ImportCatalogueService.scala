package com.bheaver.ngl4.cataloguing.services

import java.io.ByteArrayInputStream

import com.bheaver.ngl4.cataloguing.model.{ControlField, DataField, RecordModel, SubField}
import org.marc4j.MarcStreamReader
import reactor.core.publisher.{Flux, Mono}

import collection.JavaConverters._
import scala.collection.mutable.ListBuffer

trait ImportCatalogueService {
  def parseISO2709Records(rawRecord: String): Mono[List[RecordModel]]
}


class ImportCatalogueServiceImpl extends ImportCatalogueService {

  override def parseISO2709Records(rawRecord: String): Mono[List[RecordModel]] = {
    Mono.fromSupplier(() => {
      val ipstream = new ByteArrayInputStream(rawRecord.getBytes)
      val marcReader = new MarcStreamReader(ipstream)
      val buf = ListBuffer.empty[RecordModel]
      while(marcReader.hasNext){
        var record = marcReader.next()
        val dataFields = record.getDataFields.asScala.map(marcDataField => {
          DataField(marcDataField.getTag, marcDataField.getIndicator1, marcDataField.getIndicator2, marcDataField.getSubfields.asScala.map(subFieldMarc => {
            SubField(subFieldMarc.getCode, subFieldMarc.getData)
          }).toList)
        }).toList

        val controlFields = record.getControlFields.asScala.map(controlFieldMarc => {
          ControlField(controlFieldMarc.getTag, controlFieldMarc.getData)
        }).toList

        buf += RecordModel(record.getLeader.marshal(),controlFields,dataFields)
      }
      buf.toList
    })
  }
}
