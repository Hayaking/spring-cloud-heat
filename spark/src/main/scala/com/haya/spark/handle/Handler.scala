package com.haya.spark.handle

import com.haya.spark.stater.SparkStater
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream

trait Handler {
  @transient
  var stater:SparkStater = _
  var name:String = _
  var stream: InputDStream[ConsumerRecord[String, String]] = _
  var sparkSession: SparkSession = _
  var hBaseHost: String = _
  @transient
  var dataProducer: KafkaProducer[String, String] = _

  def handle(): Handler

  def register(topicName:String,stater: SparkStater): Handler ={
    this.stater = stater
    this.sparkSession = stater.sparkSession
    this.hBaseHost = stater.hBaseHost
    this.name = topicName
    this.dataProducer = stater.kafkaProducer
    stream = stater.getStream(topicName)
    this
  }
}
