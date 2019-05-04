package com.bheaver.ngl4.cataloguing

import com.bheaver.ngl4.cataloguing.services.{CataloguingInitialLoadService, CataloguingInitialLoadServiceImpl, ImportCatalogueService, ImportCatalogueServiceImpl, MarcDictionaryService, UniversalMarcDictionary}
import com.bheaver.ngl4.util.mongoUtils.Database
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration, DependsOn}
import play.api.libs.json.{JsValue, Json}

import scala.io.Source
import scala.xml._

@Configuration
@ComponentScan(basePackages = Array("com.bheaver.ngl4.util","com.bheaver.ngl4.util.filters","com.bheaver.ngl4.util.config"))
class BeanFactory {

  @Bean(Array("ApplicationYAMLString"))
  def getApplicationYAMLString(): String = {
    val strings = Source.fromResource("application.yaml").getLines()
    strings.toArray.mkString("\n")
  }

  @Bean(Array("ImportCatalogueService"))
  def getImportCatalogueService: ImportCatalogueService = {
    new ImportCatalogueServiceImpl
  }

  @Bean(Array("CataloguingInitialLoadService"))
  def getCataloguingInitialLoadService(@Autowired @Qualifier("Database") database: Database): CataloguingInitialLoadService = {
    new CataloguingInitialLoadServiceImpl(database)
  }

  @Bean(Array("MarcDictionaryService"))
  @DependsOn(Array("MarcDictionaryXMLEle"))
  def getMarcDicitionaryService(@Qualifier("MarcDictionaryXMLEle")elem: Elem): MarcDictionaryService ={
    new UniversalMarcDictionary(elem)
  }

  @Bean(Array("MarcDictionaryXMLEle"))
  def getMARCDictionaryXML():Elem = {
    XML.loadString(Source.fromResource("marcDictionary/Biblio.xml").getLines().toArray.mkString)
  }
}
