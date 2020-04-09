package com.haya

import java.util

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Hello world!
 *
 */
object App {
  def main(args: Array[String]): Unit = {
    val checkpointPath = "D:\\hadoop\\checkpoint\\kafka-direct"

    val conf = new SparkConf()
      .setAppName("ScalaKafkaStream")
      .setMaster("local[2]")

    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val ssc = new StreamingContext(sc, Seconds(1))
    ssc.checkpoint(checkpointPath)

    val bootstrapServers = "39.105.163.41:9092"
    val groupId = "haya"
    val topicName = "topic.quick.initial"
    val maxPoll = 500

    val kafkaParams = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.MAX_POLL_RECORDS_CONFIG -> maxPoll.toString,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val config = ConsumerStrategies.Subscribe[String, String](Set(topicName), kafkaParams)
    val stream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, config)
    stream.map(p => p.value()).print()
    ssc.start()
    ssc.awaitTermination()
  }

}
