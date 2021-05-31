package com.haya.spark.util

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object DataSender extends Serializable {
  val topic = "topic.q3"
  var rangeCache: Map[Int, List[BigDecimal]] = _
  var kafkaProducer: KafkaProducer[String, String] = _

  def send(value: String): Unit = {
    //    println("=================")
    //    println(value)
    //    println("=================")
    kafkaProducer.send(new ProducerRecord[String, String](topic, value))
  }
}
