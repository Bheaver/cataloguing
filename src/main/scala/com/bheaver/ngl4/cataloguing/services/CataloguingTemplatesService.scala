package com.bheaver.ngl4.cataloguing.services

import com.bheaver.ngl4.cataloguing.model.{ListTemplatesRequest, ListTemplatesResponse}
import reactor.core.publisher.Mono

trait CataloguingTemplatesService {
  def listTemplates(listTemplatesRequest: ListTemplatesRequest): Mono[ListTemplatesResponse]
}

class CataloguingTemplatesServiceImpl extends CataloguingTemplatesService{
  override def listTemplates(listTemplatesRequest: ListTemplatesRequest): Mono[ListTemplatesResponse] = ???
}
