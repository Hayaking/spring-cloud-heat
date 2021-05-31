package com.haya.spark.handle

import org.apache.spark.sql.Row
import org.apache.spark.streaming.Minutes

import java.text.SimpleDateFormat


class HeatHandler(tableName: String, windowSize: Long) extends Handler with Serializable {
  var colNames: Array[String] = Array("code","metricValue","counter","id","metricName","__time","lat","lon","street","area","address","type","componentName","collectorName","ip","port","isAlarm")

  override def handle(): Handler = {
    val sqlContext = sparkSession.sqlContext
    import org.apache.spark.sql._
    import org.apache.spark.sql.types._
    val schema = StructType(
      Seq(
        StructField("code",StringType,true)
        ,StructField("metricValue",DoubleType,true)
        ,StructField("counter",DoubleType,true)
        ,StructField("id",StringType,true)
        ,StructField("metricName",StringType,true)
        ,StructField("time",StringType,true)
        ,StructField("lat",DoubleType,true)
        ,StructField("lon",DoubleType,true)
        ,StructField("street",StringType,true)
        ,StructField("area",StringType,true)
        ,StructField("address",StringType,true)
        ,StructField("type",StringType,true)
        ,StructField("componentName",StringType,true)
        ,StructField("collectorName",StringType,true)
        ,StructField("ip",StringType,true)
        ,StructField("port",StringType,true)
        ,StructField("isAlarm",StringType,true)
      )
    )

    import org.apache.phoenix.spark._
    import sqlContext.implicits._
    // 使用scala原生包
    import scala.util.parsing.json._

    val struct = stream.map(item => {
      val jsonValue = JSON.parseFull(item.value())
      var dim = jsonValue match {
        case Some(map: Map[String, Any]) => map
      }
      println(dim)
      val metricValue: Double = dim("metricValue").toString.toDouble
      val fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val dt = fm.parse(dim("time").toString)

      val time: Long = dt.getTime
      val counter = 1D
      dim -= "metricValue"
      dim -= "time"
      (dim, (metricValue, counter,time))
    })
      // 预聚合
    val preAgg = struct.reduceByKeyAndWindow((a: (Double, Double,Long), b: (Double, Double,Long)) => {
        val v1 = a._1
        val v2 = b._1
        val c1 = a._2
        val c2 = b._2
        val t1 = a._3
        val t2 = b._3
        (v1 + v2, c1 + c2, Math.min(t1,t2))
      }, Minutes(windowSize), Minutes(windowSize))
    preAgg.foreachRDD(data=>{
      val rdd = data.map(item => {
        Row(
          item.hashCode().toString,
          item._2._1.toString.toDouble,
          item._2._2.toString.toDouble,
          item._1("id").toString,
          item._1("metricName").toString,
          item._2._3.toString,
          item._1("lat").toString.toDouble,
          item._1("lon").toString.toDouble,
          item._1("street").toString,
          item._1("area").toString,
          item._1("address").toString,
          item._1("type").toString,
          item._1("componentName").toString,
          item._1("collectorName").toString,
          item._1("ip").toString,
          item._1("port").toString,
          item._1("isAlarm").toString
        )
      })
      val df = sparkSession.createDataFrame(rdd,schema)
      df.saveToPhoenix(Map("table" -> tableName, "zkUrl" -> hBaseHost))
    })
      // 保存到hbase
//    val t1 = preAgg.transform(data =>  {
//        val rdd = data.map(item => {
//          (
//            item.hashCode().toString,
//            item._2._1.toString.toDouble,
//            item._2._2.toString.toDouble,
//            item._1("id").toString,
//            item._1("metricName").toString,
//            item._1("time").toString,
//            item._1("lat").toString.toDouble,
//            item._1("lon").toString.toDouble,
//            item._1("street").toString,
//            item._1("area").toString,
//            item._1("address").toString,
//            item._1("type").toString,
//            item._1("componentName").toString,
//            item._1("collectorName").toString,
//            item._1("ip").toString,
//            item._1("port").toString,
//            item._1("isAlarm").toString
//          )
//        })
//        rdd.toDF(colNames: _*).saveToPhoenix(Map("table" -> MINUTE_TABLE_NAME, "zkUrl" -> hBaseHost))
//        data
//      })

//      .reduceByKeyAndWindow((a: (Double, Double), b: (Double, Double)) => {
//        val v1 = a._1
//        val v2 = b._1
//        val c1 = a._2
//        val c2 = b._2
//        (v1 + v2, c1 + c2)
//      }, Minutes(60), Minutes(60))
//      .transform(data => save(HOUR_TABLE_NAME, data))
//      .reduceByKeyAndWindow((a: (Double, Double), b: (Double, Double)) => {
//        val v1 = a._1
//        val v2 = b._1
//        val c1 = a._2
//        val c2 = b._2
//        (v1 + v2, c1 + c2)
//      }, Minutes(60*24), Minutes(60*24))
//      .transform(data => save(DAY_TABLE_NAME, data))

    this
  }

  def tranTimeToLong(tm:String) :Long={
    val fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dt = fm.parse(tm)
    dt.getTime
  }
}
