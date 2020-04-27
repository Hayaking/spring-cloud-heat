package com.haya.spark.handle

import com.haya.spark.stater.SparkStater
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.InputDStream

trait Handler {
  var stater:SparkStater = _
  var name:String = _
  var stream: InputDStream[ConsumerRecord[String, String]] = _
  var sparkSession: SparkSession = _
  var hBaseHost: String = _
  var rangeCache:Map[Int,List[BigDecimal]] = _

  def handle(): Handler

  def setRangeCache(rangeCache:Map[Int,List[BigDecimal]]): Handler = {
    this.rangeCache = rangeCache
    this
  }

  def register(topicName:String,stater: SparkStater): Handler ={
    this.stater = stater
    this.sparkSession = stater.sparkSession
    this.hBaseHost = stater.hBaseHost
    this.name = topicName
    stream = stater.getStream(topicName)
    this
  }
}
