package com.haya.spark.handle

import com.haya.spark.{Checker, DataSender}
import com.haya.spark.Checker.{kafkaProducer, topic}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Seconds

class HeatHandler extends Handler with Serializable {
  val colNames: Array[String] = Array("ID", "CONSUMERID", "TEMPERATURE", "PRESSURE", "FLOW", "CREATEDATE")

  override def handle(): Handler = {
    val sqlContext = sparkSession.sqlContext

    val updateFuc = (values: Seq[(Double, Double, Double, Int)],
                     states: Option[(Double, Double, Double, Int)]) => {
      var v: (Double, Double, Double, Int) = states.getOrElse(0, 0, 0, 0)
      for (item <- values) {
        v = (
          item._1 + v._1,
          item._2 + v._2,
          item._3 + v._3,
          item._4 + v._4
        )
      }
      Option(v)
    }
    import org.apache.phoenix.spark._
    import sqlContext.implicits._
    /*
    * 0. id
    * 1.
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
    }).transform(rdd => {
      rdd.toDF(colNames: _*)
        .saveToPhoenix(Map("table" -> "heatdata", "zkUrl" -> hBaseHost))
      rdd
    }).map(row => {
      Checker.checkFlow(row._2, row._5)
      Checker.checkTemp(row._2, row._3)
      Checker.checkPres(row._2, row._4)
      (row._2.toInt, (row._3.toDouble, row._4.toDouble, row._5.toDouble, 1))
//      (row._2, row._2)
    })
//      .reduceByKeyAndWindow( (a:Int, b:Int)=> a+b,Seconds(10),Seconds(5),2)
      .updateStateByKey[(Double, Double, Double, Int)](updateFuc)
      .map(item => {
        val value = item._2
        val v = (item._1,
          value._1 / value._4,
          value._2 / value._4,
          value._3 / value._4,
          value._4)
        DataSender.send(v.toString())
        v
      }).print()
    this
  }
}
