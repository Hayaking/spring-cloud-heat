package com.haya.spark.controller

import org.apache.spark.sql.DataFrameReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class HelloController {

  @Autowired
  val heatDataReader: DataFrameReader = null

  @GetMapping(value = Array("hello"))
  def hello(): String = {
    heatDataReader.load().printSchema()
    "hello,scala"
  }
}
