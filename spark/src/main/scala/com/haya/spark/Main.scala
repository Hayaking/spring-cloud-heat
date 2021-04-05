package com.haya.spark

import com.haya.spark.stater.SparkStater

object Main {
//  val hBaseHost = "39.105.163.41:2181"
  val hBaseHost = "192.168.75.11:2181"
//  val kafkaHost = "39.105.163.41:9092"
  val kafkaHost = "192.168.75.11:9092"
  val mySQlConfig = Map(
    "user" -> "root",
    "password" -> "",
    "url" -> "jdbc:mysql:///heat_data?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true",
    "driver" -> "com.mysql.cj.jdbc.Driver",
    "dbtable" -> "consumer_config"
  )

  def main(args: Array[String]): Unit = {

    new SparkStater(hBaseHost, kafkaHost)
      .ready()
      .setMySQLConfig(mySQlConfig)
      .run()
  }
}
