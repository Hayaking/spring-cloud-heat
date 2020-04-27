package com.haya.spark.handle

class HeatHandler extends Handler {
  val colNames: Array[String] = Array("ID", "CONSUMERID", "TEMPERATURE", "PRESSURE", "FLOW", "CREATEDATE")

  override def handle(): Handler = {
    val sqlContext = sparkSession.sqlContext
    import org.apache.phoenix.spark._
    import sqlContext.implicits._
    stream.foreachRDD(rdd => {
      rdd.map(_.value().split(","))
        .map(item => (
          item(0).toInt,
          item(1).toInt,
          item(2).toDouble,
          item(3).toDouble,
          item(4).toDouble,
          item(5).toLong))
        .toDF("ID", "CONSUMERID", "TEMPERATURE", "PRESSURE", "FLOW", "CREATEDATE")
        .saveToPhoenix(Map("table" -> "heatData", "zkUrl" -> hBaseHost))
    })

//    stream.foreachRDD(rdd => {
//      rdd.map(item=>item.value().split(","))
//        .map(col=>(
//          col(0).toInt,
//          col(1).toInt,
//          col(2).toDouble,
//          col(3).toDouble,
//          col(4).toDouble,
//          col(5).toLong))
//        .map(row=>{
//          heatDataRangeConfig.filter(heatDataRangeConfig("consumer_id").===(col(1).toInt))
//          row._2.equals()
//          row
//        }).saveToPhoenix()


//      rdd.map(_.value()
//        .split(",")
//        .map(col => {
//          (col(0).toInt,
//            col(1).toInt,
//            col(2).toDouble,
//            col(3).toDouble,
//            col(4).toDouble,
//            col(5).toLong)
//        })
//      }).toDF(colNames: _*)
//      .saveToPhoenix(Map("table" -> "heatData", "zkUrl" -> hBaseHost))
//    })
    this
  }
}
