package com.haya.spark.stater

import com.haya.spark.handle.HeatHandler
import com.haya.spark.util.StreamUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}


class SparkStater(hBase: String, kafka: String) extends Serializable {
  var hBaseHost: String = hBase
  var kafkaHost: String = kafka

  val sparkSession: SparkSession = SparkSession.builder()
    .appName("")
    .master("local[2]")
    .getOrCreate()
  val sparkContext: SparkContext = sparkSession.sparkContext
  // 批处理时间设置为60s
  val streamingContext: StreamingContext = new StreamingContext(sparkContext, Seconds(60))
  // 消费者组
  var groupId = "hayaking"
  val checkpointPath = "D:\\hadoop\\checkpoint\\kafka-direct"

  def ready(): SparkStater = {
    streamingContext.checkpoint(checkpointPath)
    val minuteHandle = new HeatHandler("HEAT_DATA_MINUTE",1)
//    val hourHandle = new HeatHandler("HEAT_DATA_HOUR",60)
//    val dayHandle = new HeatHandler("HEAT_DATA_DAY",60*24)
    minuteHandle.register("data1", SparkStater.this).handle()
//    hourHandle.register("data1", SparkStater.this).handle()
//    dayHandle.register("data1", SparkStater.this).handle()
    this
  }

  def getStream(topicName: String): InputDStream[ConsumerRecord[String, String]] = {
    StreamUtils.createStream(kafkaHost, groupId, topicName, streamingContext)
  }

  def run(): Unit ={
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
