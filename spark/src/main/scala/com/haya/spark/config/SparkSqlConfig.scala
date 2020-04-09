package com.haya.spark.config

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrameReader, SparkSession}
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class SparkSqlConfig {

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
  def dataFrameReader: DataFrameReader = sparkSession
    .sqlContext
    .read
    .format("org.apache.phoenix.spark")
    .options(Map("table" -> "us_population", "zkUrl" -> "39.105.163.41:2181"))
}
