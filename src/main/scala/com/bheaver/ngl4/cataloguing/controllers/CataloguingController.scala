package com.bheaver.ngl4.cataloguing.controllers

import com.bheaver.ngl4.cataloguing.model.{CataloguingInitialLoadRequest, CataloguingInitialLoadResponse, GetMarcDictionarySubFieldsIndicatorsRequest, SearchDictionaryByTagRequest}
import com.bheaver.ngl4.cataloguing.services.{CataloguingInitialLoadService, MarcDictionaryService}
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestHeader, RequestMapping, RequestParam, RestController}
import play.api.libs.json.Json
import reactor.core.publisher.Mono
import com.bheaver.ngl4.cataloguing.model.CataloguingInitialLoadImplicitWrites._
import com.bheaver.ngl4.cataloguing.model.MarcDictionaryModelWrites._
@RestController
@RequestMapping(path = Array("/cataloguing"))
class CataloguingController {

  @Autowired
  @Qualifier("CataloguingInitialLoadService")
  var cataloguingInitialLoadService: CataloguingInitialLoadService = null

  @Autowired
  @Qualifier("MarcDictionaryService")
  var marcDictionaryService: MarcDictionaryService = null

  @PostMapping(path=Array("/initialLoad"))
  def cataloguingInitialLoadup(@RequestHeader("libCode") libCode: String): Mono[String] = {
    cataloguingInitialLoadService.initialLoadup(CataloguingInitialLoadRequest(libCode))
      .map(response => {
        Json.stringify(Json.toJson(response))
      })
  }

  @GetMapping(path=Array("/listTags"))
  def searchMarcDictionaryByTag(@RequestHeader("libCode") libCode: String): Mono[String] = {
    marcDictionaryService.searchMarcDictionaryByTag(SearchDictionaryByTagRequest(libCode))
      .map(response => {
        Json.stringify(Json.toJson(response))
      })
  }
  @GetMapping(path=Array("/getTagDetails"))
  def getTagDetails(@RequestHeader("libCode") libCode: String,
                   @RequestParam("tag")tag: String): Mono[String] = {
    marcDictionaryService.listTagData(GetMarcDictionarySubFieldsIndicatorsRequest(libCode,tag))
      .map(response => {
        Json.stringify(Json.toJson(response))
      })
  }
}
