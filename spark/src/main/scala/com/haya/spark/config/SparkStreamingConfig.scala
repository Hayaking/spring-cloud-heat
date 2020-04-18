package com.haya.spark.config

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.context.event.ContextRefreshedEvent

@Configuration
class SparkStreamingConfig extends ApplicationListener[ContextRefreshedEvent] {
  @Autowired
  var streamingContext:StreamingContext = _

  @Bean
  def streamingContext(sparkContext: SparkContext): StreamingContext = {
//    val checkpointPath = "D:\\hadoop\\checkpoint\\kafka-direct"
    val ssc = new StreamingContext(sparkContext, Seconds(1))
//    ssc.checkpoint(checkpointPath)
    ssc
  }

  @Bean
  def context(streamingContext: StreamingContext): Map[String, InputDStream[ConsumerRecord[String, String]]] = {
    val bootstrapServers = "kafka:9092"
    val groupId = "haya"
    val topicName2 = "topic.q1"
    val maxPoll = 500

    val kafkaParams = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.MAX_POLL_RECORDS_CONFIG -> maxPoll.toString,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val stream2 = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](Set(topicName2), kafkaParams))
    Map(
      "heatData"->stream2
    )
  }
  // 所有Bean建立后 执行
  override def onApplicationEvent(e: ContextRefreshedEvent): Unit = {
    streamingContext.start()
  }
}
