package com.haya.spark.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class SparkStreamingConfig {
  var map = null

//  @Bean
//  def sparkContext: SparkContext = new SparkContext(sparkConf) {
//    {
//      setLogLevel("WARN")
//    }
//  }
//
//  @Bean
//  def sparkConf: SparkConf = new SparkConf()
//    .setAppName("ScalaKafkaStream")
//    .setMaster("local[2]")

  @Bean
  def streamingContext(sparkContext: SparkContext): StreamingContext = {
    val checkpointPath = "D:\\hadoop\\checkpoint\\kafka-direct"
    val ssc = new StreamingContext(sparkContext, Seconds(1))
    ssc.checkpoint(checkpointPath)
    ssc
  }

  @Bean
  def config(streamingContext: StreamingContext): Object = {
    val bootstrapServers = "39.105.163.41:9092"
    val groupId = "haya"
    val topicName = "topic.quick.initial"
    //    val topicName = "topic.quick.initial"
    val maxPoll = 500

    val kafkaParams = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.MAX_POLL_RECORDS_CONFIG -> maxPoll.toString,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val stream = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](Set(topicName), kafkaParams))
    //    map = Map(
    //
    //    )
    stream.map(p => p.value()).print()
//    streamingContext.start()
    null
  }
}
