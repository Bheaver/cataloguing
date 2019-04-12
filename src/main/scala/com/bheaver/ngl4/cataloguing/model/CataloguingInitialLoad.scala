package com.bheaver.ngl4.cataloguing.model

import play.api.libs.json.{JsValue, Json, Writes}

case class CataloguingInitialLoadRequest(val libCode: String)

case class CataloguingInitialLoadResponse(materialTypes: List[MaterialType], bibliographicLevels: List[BibliographicLevel], catalogueTemplates: List[CatalogueTemplate])

case class MaterialType(code: String, value: String)

case class BibliographicLevel(code: String, value: String)

case class CatalogueTemplate(name: String, template: String)

object CataloguingInitialLoadImplicitWrites {


  implicit val BibliographicLevelWrites = new Writes[BibliographicLevel] {
    override def writes(o: BibliographicLevel): JsValue = {
      Json.obj(
        "code" -> o.code,
        "value" -> o.value
      )
    }
  }

  implicit val MaterialTypeWrites = new Writes[MaterialType] {
    override def writes(o: MaterialType): JsValue = {
      Json.obj(
        "code" -> o.code,
        "value" -> o.value
      )
    }
  }

  implicit val CataloguingTemplateWrites = new Writes[CatalogueTemplate] {
    override def writes(o: CatalogueTemplate): JsValue = {
      Json.obj(
        "name" -> o.name,
        "template" -> o.template
      )
    }
  }

  implicit val CataloguingInitialLoadResponseWrites = new Writes[CataloguingInitialLoadResponse] {
    override def writes(o: CataloguingInitialLoadResponse): JsValue = {
      Json.obj(
        "BibliographicLevels" -> o.bibliographicLevels,
        "MaterialTypes" -> o.materialTypes,
        "CatalogueTemplates" -> o.catalogueTemplates
      )
    }
  }
}