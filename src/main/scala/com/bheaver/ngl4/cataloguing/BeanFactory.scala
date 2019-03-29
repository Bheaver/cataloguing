package com.bheaver.ngl4.cataloguing

import com.bheaver.ngl4.cataloguing.services.{ImportCatalogueService, ImportCatalogueServiceImpl}
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}

import scala.io.Source

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
}
