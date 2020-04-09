package com.haya.streaming.controller

import org.apache.avro.file.DataFileReader
import org.apache.spark.sql.DataFrameReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class HelloController {

  @Autowired
  val  dfr:DataFrameReader = null

  @GetMapping(value = Array("hello"))
  def hello(): String = {
    dfr.load().printSchema()
    return "hello,scala"
  }
}
