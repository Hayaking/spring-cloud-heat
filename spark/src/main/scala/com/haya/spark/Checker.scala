package com.haya.spark

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Checker extends Serializable {

  val topic = "topic.q2"
  var rangeCache: Map[Int, List[BigDecimal]] = _
  var kafkaProducer: KafkaProducer[String, String] = _

  def checkFlow(consumerId: Int, value: Double): Unit = {
    val flowUpper = rangeCache(consumerId)(4)
    val flowLower = rangeCache(consumerId)(5)
    check("flow", consumerId, flowUpper, flowLower, value)
  }

  def checkPres(consumerId: Int, value: Double): Unit = {
    val presUpper = rangeCache(consumerId)(2)
    val presLower = rangeCache(consumerId)(3)
    check("pres", consumerId, presUpper, presLower, value)
  }

  def check(name: String, consumerId: Int, upper: BigDecimal, lower: BigDecimal, value: Double): Unit = {
    if (value >= upper) {
      kafkaProducer.send(new ProducerRecord[String, String](topic, name + "," + consumerId + ",low"))
    } else if (value <= lower) {
      kafkaProducer.send(new ProducerRecord[String, String](topic, name + "," + consumerId + ",high"))
    }
  }

  def checkTemp(consumerId: Int, value: Double): Unit = {
    val tempUpper = rangeCache(consumerId).head
    val tempLower = rangeCache(consumerId)(1)
    check("temp", consumerId, tempUpper, tempLower, value)
  }
}
