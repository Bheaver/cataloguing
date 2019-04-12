package com.bheaver.ngl4.cataloguing.controllers

import com.bheaver.ngl4.cataloguing.model.{CataloguingInitialLoadRequest, CataloguingInitialLoadResponse}
import com.bheaver.ngl4.cataloguing.services.CataloguingInitialLoadService
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.web.bind.annotation.{PostMapping, RequestHeader, RequestMapping, RestController}
import play.api.libs.json.Json
import reactor.core.publisher.Mono
import com.bheaver.ngl4.cataloguing.model.CataloguingInitialLoadImplicitWrites._

@RestController
@RequestMapping(path = Array("/cataloguing"))
class CataloguingController {

  @Autowired
  @Qualifier("CataloguingInitialLoadService")
  var cataloguingInitialLoadService: CataloguingInitialLoadService = null

  @PostMapping(path=Array("/initialLoad"))
  def cataloguingInitialLoadup(@RequestHeader("libCode") libCode: String): Mono[String] = {
    cataloguingInitialLoadService.initialLoadup(CataloguingInitialLoadRequest(libCode))
      .map(response => {
        Json.stringify(Json.toJson(response))
      })
  }
}
