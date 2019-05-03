package com.bheaver.ngl4.cataloguing.model

import play.api.libs.json.{JsValue, Json, Writes}

import scala.beans.BeanProperty

case class RecordModel(leader: String,
                       controlFields: List[ControlField],
                       dataFields: List[DataField])

case class ControlField(tag: String,
                        data: String)

case class DataField(tag: String,
                     indicator1: Char,
                     indicator2: Char,
                     subFields: List[SubField])

case class SubField(identifier: Char,
                    data: String)


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