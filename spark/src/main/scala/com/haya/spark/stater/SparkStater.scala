package com.haya.spark.stater

import com.haya.spark.handle.{Handler, HeatHandler}
import com.haya.spark.util.StreamUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

class SparkStater(hBase: String, kafka: String) {
  val sparkSession: SparkSession = SparkSession.builder()
    .appName("")
    .master("local[2]")
    .getOrCreate()
  val sparkContext: SparkContext = sparkSession.sparkContext
  val streamingContext: StreamingContext = new StreamingContext(sparkContext, Seconds(1))
  var hBaseHost: String = hBase
  var kafkaHost: String = kafka
  var handlerList: List[Handler] = List()
  var groupId = "haya"

  def ready(): SparkStater = {
    handlerList :+ new HeatHandler()
      .register("q1", SparkStater.this)
      .handle()
    this
  }

  def getStream(topicName: String): InputDStream[ConsumerRecord[String, String]] = {
    StreamUtils.createStream(kafkaHost, "haya", topicName, streamingContext)
  }

  def setMySQLConfig(config: Map[String, String]): SparkStater = {
    var rangeCache: Map[Int, List[BigDecimal]] = Map()
    // 连接MySQL
    val df = sparkSession.sqlContext.read
      .format("jdbc")
      .options(config)
      .load()
    df.collect().foreach(row => {
      rangeCache += (row.getInt(13) -> List(
        row.getDecimal(1),
        row.getDecimal(2),
        row.getDecimal(3),
        row.getDecimal(4),
        row.getDecimal(5),
        row.getDecimal(6),
        row.getDecimal(7),
        row.getDecimal(8)
      ))
    })
    handlerList.foreach(_.setRangeCache(rangeCache))
    this
  }

  def run(): Unit ={
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
