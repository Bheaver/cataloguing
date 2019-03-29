package com.bheaver.ngl4.cataloguing.model

import java.util

import play.api.libs.json.{JsValue, Json, Writes}

import scala.beans.BeanProperty

case class RecordModel(@BeanProperty leader: String,
                       @BeanProperty controlFields: List[ControlField],
                       @BeanProperty dataFields: List[DataField])

case class ControlField(@BeanProperty tag: String,
                        @BeanProperty data: String)

case class DataField(@BeanProperty tag: String,
                     @BeanProperty indicator1: Char,
                     @BeanProperty indicator2: Char,
                     @BeanProperty subFields: List[SubField])

case class SubField(@BeanProperty identifier: Char,
                    @BeanProperty data: String)


object CatalogueRecordImplicitJSONWrites {
  implicit val subFieldWrites = new Writes[SubField] {
    override def writes(o: SubField): JsValue = Json.obj(
      "identifier" -> o.identifier.toString,
      "data" -> o.data
    )
  }

  implicit val dataFieldWrites = new Writes[DataField] {
    override def writes(o: DataField): JsValue = Json.obj(
      "tag" -> o.tag,
      "indicator1" -> o.indicator1.toString,
      "indicator2" -> o.indicator2.toString,
      "subfields" -> o.subFields
    )
  }
  implicit val controlFieldWrites = new Writes[ControlField] {
    override def writes(o: ControlField): JsValue = Json.obj(
      "tag" -> o.tag,
      "data" -> o.data
    )
  }
  implicit val recordModelWrites = new Writes[RecordModel] {
    override def writes(o: RecordModel): JsValue = Json.obj(
      "leader" -> o.leader,
      "controlFields" -> o.controlFields,
      "dataFields" -> o.dataFields
    )
  }
}