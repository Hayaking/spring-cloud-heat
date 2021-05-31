package com.haya.spark

import com.haya.spark.stater.SparkStater

object Main {
  val hBaseHost = "192.168.75.11:2181"
  val kafkaHost = "192.168.75.11:9092"

  def main(args: Array[String]): Unit = {
    new SparkStater(hBaseHost, kafkaHost)
      .ready()
      .run()
  }
}
