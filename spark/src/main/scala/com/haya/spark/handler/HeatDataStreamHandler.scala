package com.haya.spark.handler

import java.util

import com.haya.spark.config.HeatData
import javax.annotation.PostConstruct
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.streaming.dstream.InputDStream
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HeatDataStreamHandler {
  @Autowired
  var context: Map[String, InputDStream[ConsumerRecord[String, String]]] = null
  @Autowired
  var sparkSession: SparkSession = _

  @PostConstruct
  def handle(): Object = {

    val stream = context("heatData")
    val sqlContext = sparkSession.sqlContext
    import sqlContext.implicits._
    import org.apache.phoenix.spark._
    stream.foreachRDD(rdd => {
      rdd.map(_.value().split(","))
        .map(item=>(item(0).toInt,item(1).toInt,item(2).toDouble,item(3).toDouble,item(4).toDouble,item(5).toLong))
      .toDF("ID","CONSUMERID","TEMPERATURE","PRESSURE","FLOW","CREATEDATE")
//      df.schema.add(DataTypes.createStructField("id", DataTypes.IntegerType, false))
//        .add(DataTypes.createStructField("consumerId", DataTypes.IntegerType, false))
//        .add(DataTypes.createStructField("temperature", DataTypes.DoubleType, false))
//        .add(DataTypes.createStructField("pressure", DataTypes.DoubleType, false))
//        .add(DataTypes.createStructField("flow", DataTypes.DoubleType, false))
//        .add(DataTypes.createStructField("createDate", DataTypes.LongType, true))

    .saveToPhoenix(Map("table" -> "heatData", "zkUrl" -> "hbase:2181"))
//        .write
//        .format("org.apache.phoenix.spark")
//        .mode(SaveMode.Overwrite)
//        .option("table", "heatData")
//        .option("zkUrl","hbase:2181" )
//        .save()
    })
    null
  }
}
