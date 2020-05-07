package com.haya.spark

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Checker extends Serializable {
//  @transient
  var rangeCache:Map[Int,List[BigDecimal]] = _
//  @transient
  var kafkaProducer: KafkaProducer[String, String] = _

  def checkFlow(consumerId: Int, value: Double): Unit = {
    val flowUpper = rangeCache(consumerId)(4)
    val flowLower = rangeCache(consumerId)(5)
    if (!(value > flowLower && value < flowUpper)) {
      // 发送预警消息
      println("流量不对啊")
      val record = new ProducerRecord[String, String]("topic.q2", "流量不对啊")
      kafkaProducer.send(record)
    }
  }

  def checkPres(consumerId: Int, value: Double): Unit = {
    val presUpper = rangeCache(consumerId)(2)
    val presLower = rangeCache(consumerId)(3)
    if (!(value > presLower && value < presUpper)) {
      println("压力不对啊")
      val record = new ProducerRecord[String, String]("topic.q2", "压力不对啊")
      kafkaProducer.send(record)
    }
  }

  def checkTemp(consumerId: Int, value: Double): Unit = {
    val tempUpper = rangeCache(consumerId).head
    val tempLower = rangeCache(consumerId)(1)
    if (!(value > tempLower && value < tempUpper)) {
      // 发送预警消息
      println("温度不对啊")
      val record = new ProducerRecord[String, String]("topic.q2", "温度不对啊")
      kafkaProducer.send(record)
    }
  }
}
