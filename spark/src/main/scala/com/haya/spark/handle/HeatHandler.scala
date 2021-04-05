package com.haya.spark.handle

import com.haya.spark.{Checker, DataSender}
import com.haya.spark.Checker.{kafkaProducer, topic}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Minutes, Seconds}

class HeatHandler extends Handler with Serializable {
  val colNames: Array[String] = Array("ID", "CONSUMERID", "TEMPERATURE", "PRESSURE", "FLOW", "CREATEDATE")

  override def handle(): Handler = {
    val sqlContext = sparkSession.sqlContext

//    val updateFuc = (values: Seq[(Double, Double, Double, Int)],
//                     states: Option[(Double, Double, Double, Int)]) => {
//      var v: (Double, Double, Double, Int) = states.getOrElse(0, 0, 0, 0)
//      for (item <- values) {
//        v = (
//          item._1 + v._1,
//          item._2 + v._2,
//          item._3 + v._3,
//          item._4 + v._4
//        )
//      }
//      Option(v)
//    }
    import org.apache.phoenix.spark._
    import sqlContext.implicits._
    /*
    * 0. id
    * 1. consumerId
    * 2. pres
    * 3. temp
    * 4. flow
    * 5. date
    * */
    stream.map(_.value().split(","))
      .map(item => {
      (
        item(0).toInt,
        item(1).toInt,
        item(2).toDouble,
        item(3).toDouble,
        item(4).toDouble,
        item(5).toLong)
    }).map(row => {
      val consumerId = row._2
      val flow = row._5
      val temp = row._3
      val pres = row._4
      // 检查 告警
      Checker.checkFlow(consumerId, flow)
      Checker.checkTemp(consumerId, temp)
      Checker.checkPres(consumerId, pres)
      (row._2.toInt, (row._3.toDouble, row._4.toDouble, row._5.toDouble, row._1))
    }).reduceByKeyAndWindow((a:(Double, Double, Double, Int),b:(Double, Double, Double, Int)) => {
      val data = (
        Math.max(a._1,b._1),
        Math.max(a._2,b._2),
        Math.max(a._3,b._3),
        Math.max(a._4,b._4)
      )
      DataSender.send(data.toString())
      data
    }, Minutes(1), Minutes(1)).transform(data=>{
      val rdd = data.map(item=>{
        (
          item._2._4,
          item._1,
          item._2._1,
          item._2._2,
          item._2._3,
          item._2._4)
      })
      rdd.toDF(colNames: _*).saveToPhoenix(Map("table" -> "heatdata", "zkUrl" -> hBaseHost))
      data
    }).reduceByKeyAndWindow((a:(Double, Double, Double, Int),b:(Double, Double, Double, Int)) => {
      (
        Math.max(a._1,b._1),
        Math.max(a._2,b._2),
        Math.max(a._3,b._3),
        Math.max(a._4,b._4)
      )
    }, Minutes(60*24), Minutes(1)).transform(data=>{
      val rdd = data.map(item=>{
        (
          item._2._4,
          item._1,
          item._2._1,
          item._2._2,
          item._2._3,
          item._2._4)
      })
      rdd.toDF(colNames: _*).saveToPhoenix(Map("table" -> "heatdata_day", "zkUrl" -> hBaseHost))
      data
    }).print()
    this
  }
}
