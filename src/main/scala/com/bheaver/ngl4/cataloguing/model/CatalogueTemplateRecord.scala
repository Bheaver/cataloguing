package com.bheaver.ngl4.cataloguing.model

case class CatalogueTemplateRecord(leader: String,
                                   controlFields: List[ControlField],
                                   dataFields: List[DataFieldTemplate],
                                   description: String,
                                   example: String)

case class DataFieldTemplate(tag: String,
                     indicator1: Char,
                     indicator2: Char,
                     subFields: List[SubFieldTemplate],
                     description: String,
                     example: String)

case class SubFieldTemplate(identifier: Char,
                    data: String,
                    description: String,
                    example: String)