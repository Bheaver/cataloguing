package com.bheaver.ngl4.cataloguing.model.primaryCataloguing

case class FieldDescription(tag: String,
                            indicator1: Char,
                            indicator2: Char,
                            subFields: List[SubFieldDescription],
                            displayName: String,
                            description: String,
                            examples: String,
                            authorityReferenceId: String,
                            linkedCatalogueRecordId: String)

case class SubFieldDescription(identifier: String,
                               data: String,
                               examples: String,
                               displayName: String,
                               description: String)