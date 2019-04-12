package com.bheaver.ngl4.cataloguing.services

import com.bheaver.ngl4.cataloguing.model._
import com.bheaver.ngl4.util.mongoUtils.Database
import reactor.core.publisher.{Flux, Mono}
import com.mongodb.client.model.{Filters => filters}
import scala.collection.JavaConverters._

trait CataloguingInitialLoadService {
  def initialLoadup(request: CataloguingInitialLoadRequest): Mono[CataloguingInitialLoadResponse]
}

class CataloguingInitialLoadServiceImpl(database: Database) extends CataloguingInitialLoadService{

  override def initialLoadup(request: CataloguingInitialLoadRequest): Mono[CataloguingInitialLoadResponse] = {
    val db = database.getDatabase(request.libCode)

    val pubBibLevels = Flux.from(db.getCollection("config_bibliographic_levels").find())
                          .map[BibliographicLevel](document => BibliographicLevel(document.getString("code"),document.getString("description")))
                          .collectList().map[List[BibliographicLevel]](list => list.asScala.toList)

    val pubMatTypes = Flux.from(db.getCollection("config_material_types").find())
                          .map[MaterialType](document => MaterialType(document.getString("code"),document.getString("description")))
                          .collectList().map[List[MaterialType]](list => list.asScala.toList)

    val templates = Flux.from(db.getCollection("catalogue_templates").find(filters.eq("status",1)))
                        .map[CatalogueTemplate](document => CatalogueTemplate(document.getString("name"),document.getString("template")))
                          .collectList().map[List[CatalogueTemplate]](list => list.asScala.toList)

    Mono.zip(pubBibLevels,pubMatTypes,templates).map(tuple3 => CataloguingInitialLoadResponse(tuple3.getT2,tuple3.getT1,tuple3.getT3))

  }
}