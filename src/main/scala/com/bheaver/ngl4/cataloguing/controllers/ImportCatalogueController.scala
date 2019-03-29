package com.bheaver.ngl4.cataloguing.controllers


import com.bheaver.ngl4.cataloguing.services.ImportCatalogueService
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, RestController}
import reactor.core.publisher.{Flux, Mono}
import com.bheaver.ngl4.cataloguing.model.CatalogueRecordImplicitJSONWrites._;
import play.api.libs.json.Json;
@RestController
@RequestMapping(path = Array("/cataloguing"))
class ImportCatalogueController {

  @Autowired
  @Qualifier("ImportCatalogueService")
  var importCatalogueService: ImportCatalogueService = null

  @PostMapping(path=Array("/parseiso2709"))
  def parseISO2709Record(@RequestBody requestBodyFlux: Mono[String]): Mono[String] = {
    requestBodyFlux.flatMap(rawString => {
      importCatalogueService.parseISO2709Records(rawString).map(recordModel => {
        Json.stringify(Json.toJson(recordModel))
      })
    })
  }
}
