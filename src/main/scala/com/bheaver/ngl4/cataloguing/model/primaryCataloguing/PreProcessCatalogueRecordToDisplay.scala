package com.bheaver.ngl4.cataloguing.model.primaryCataloguing

import com.bheaver.ngl4.cataloguing.model.RecordModel

case class PreProcessCatalogueRecordToDisplayRequest(val libCode: String,
                                                      val recordModel: RecordModel,
                                                      val templateSelected: String)

case class PreProcessCatalogueRecordToDisplayResponse(val fieldDescriptions: List[FieldDescription])

