package com.haya.spark.handler

import javax.annotation.PostConstruct
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Component

@Component
class HeatDataStreamHandler {
  @Autowired
  var context: Map[String, InputDStream[ConsumerRecord[String, String]]] = _
  @Autowired
  var sparkSession: SparkSession = _
  @Value("${host.hbase}")
  var hBaseHost: String = _

  @PostConstruct
  def handle(): Object = {
    val stream = context("heatData")
    val sqlContext = sparkSession.sqlContext
    import org.apache.phoenix.spark._
    import sqlContext.implicits._
    stream.foreachRDD(rdd => {
      rdd.map(_.value().split(","))
        .map(item => (
          item(0).toInt,
          item(1).toInt,
          item(2).toDouble,
          item(3).toDouble,
          item(4).toDouble,
          item(5).toLong))
        .toDF("ID", "CONSUMERID", "TEMPERATURE", "PRESSURE", "FLOW", "CREATEDATE")
        .saveToPhoenix(Map("table" -> "heatData", "zkUrl" -> hBaseHost))
    })
    null
  }
}
