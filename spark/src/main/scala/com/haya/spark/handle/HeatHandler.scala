package com.haya.spark.handle

import com.haya.spark.Checker
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Seconds

class HeatHandler extends Handler with Serializable {
  val colNames: Array[String] = Array("ID", "CONSUMERID", "TEMPERATURE", "PRESSURE", "FLOW", "CREATEDATE")

  override def handle(): Handler = {
    val sqlContext = sparkSession.sqlContext

    val updateFuc = (values: Seq[(Double, Double, Double, Int)],
                     states: Option[(Double, Double, Double, Int)]) => {
      var v: (Double, Double, Double, Int) = states.getOrElse(0,0,0,0)
      for(item <- values){
        v = (
          item._1 + v._1,
          item._2 + v._2,
          item._3 + v._3,
          item._4 + v._4
        )
      }
      Option(v)
    }
    val reduceFuc = (a: (Double, Double, Double, Int),
                     b: (Double, Double, Double, Int)) => {
      (a._1 + b._1,
        a._2 + b._2,
        a._3 + b._3,
        a._4 + b._4)
    }
    import org.apache.phoenix.spark._
    import sqlContext.implicits._


    //    stream.map(_.value().split(",")).map(item => {
    //      (
    //        item(0).toInt,
    //        item(1).toInt,
    //        item(2).toDouble,
    //        item(3).toDouble,
    //        item(4).toDouble,
    //        item(5).toLong)
    //    }).transform(rdd => {
    //      rdd.toDF(colNames: _*)
    //        .saveToPhoenix(Map("table" -> "heatdata", "zkUrl" -> hBaseHost))
    //      rdd
    //    }).map(row => {
    //      (row._1.toInt, (row._2.toDouble, row._3.toDouble, row._4.toDouble, 1))
    //    }).reduceByKeyAndWindow(reduceFuc, Seconds(1), Seconds(1)).map(item => {
    //      val value = item._2
    //      (item._1,
    //        value._1 / value._4,
    //        value._2 / value._4,
    //        value._3 / value._4,
    //        value._4)
    //    }).print()

    stream.map(_.value().split(",")).map(item => {
      (
        item(0).toInt,
        item(1).toInt,
        item(2).toDouble,
        item(3).toDouble,
        item(4).toDouble,
        item(5).toLong)
    })
      .transform(rdd => {
        rdd.toDF(colNames: _*)
          .saveToPhoenix(Map("table" -> "heatdata", "zkUrl" -> hBaseHost))
        rdd
      }).map(row => {
      (row._2.toInt, (row._2.toDouble, row._3.toDouble, row._4.toDouble, 1))
    }).updateStateByKey[(Double, Double, Double, Int)](updateFuc)
      .map(item => {
        val value = item._2
        (item._1,
          value._1 / value._4,
          value._2 / value._4,
          value._3 / value._4,
          value._4)
      }).print()

    this
  }
}
