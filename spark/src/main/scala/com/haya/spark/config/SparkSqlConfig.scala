package com.haya.spark.config

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrameReader, SparkSession}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class SparkSqlConfig {

  @Value("${host.hbase}")
  var hBaseHost: String = _

  @Bean
  def sparkContext: SparkContext = sparkSession.sparkContext

  @Bean
  def sparkSession: SparkSession = {
    SparkSession
      .builder()
      .appName("")
      .master("local[2]")
      .getOrCreate()
  }

  @Bean
  def heatDataReader: DataFrameReader = sparkSession
    .sqlContext
    .read
    .format("org.apache.phoenix.spark")
    .options(Map("table" -> "HeatData", "zkUrl" -> hBaseHost))


}
