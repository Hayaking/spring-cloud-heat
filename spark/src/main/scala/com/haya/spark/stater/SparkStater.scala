package com.haya.spark.stater

import java.util.Properties

import com.haya.spark.Checker
import com.haya.spark.handle.{Handler, HeatHandler}
import com.haya.spark.util.StreamUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}


class SparkStater(hBase: String, kafka: String) extends Serializable {
  val sparkSession: SparkSession = SparkSession.builder()
    .appName("")
    .master("local[2]")
    .getOrCreate()
  val sparkContext: SparkContext = sparkSession.sparkContext
  val streamingContext: StreamingContext = new StreamingContext(sparkContext, Seconds(1))
  var hBaseHost: String = hBase
  var kafkaHost: String = kafka
  var handlerList: List[Handler] = List()
  var groupId = "haya"
  val checkpointPath = "D:\\hadoop\\checkpoint\\kafka-direct"

  var kafkaProducer: KafkaProducer[String, String] = _
  var kafkaProducerConfig: Properties = new Properties(){{
    put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  }}

  kafkaProducerConfig.put("bootstrap.servers", kafkaHost)

  def ready(): SparkStater = {
    streamingContext.checkpoint(checkpointPath)
    // 处理器队列
    handlerList :+ new HeatHandler()
      .register("topic.q1", SparkStater.this)
      .handle()
    // 消息队列生产者，用于发送消息
    kafkaProducer = new KafkaProducer[String, String](kafkaProducerConfig)
    Checker.kafkaProducer = kafkaProducer
    this
  }

  def getStream(topicName: String): InputDStream[ConsumerRecord[String, String]] = {
    StreamUtils.createStream(kafkaHost, "haya", topicName, streamingContext)
  }

  def setMySQLConfig(config: Map[String, String]): SparkStater = {
    var rangeCache: Map[Int, List[BigDecimal]] = Map()
    // 连接MySQL
    val df = sparkSession.sqlContext.read
      .format("jdbc")
      .options(config)
      .load()
    // 读取consumer_config表, 存入Map[consumer_id,data_range]
    df.collect().foreach(row => {
      rangeCache += (row.getInt(11) -> List(
        row.getDecimal(1),
        row.getDecimal(2),
        row.getDecimal(3),
        row.getDecimal(4),
        row.getDecimal(5),
        row.getDecimal(6)
      ))
    })
    Checker.rangeCache = rangeCache
//    handlerList.foreach(_.setRangeCache(rangeCache))
    this
  }

  def run(): Unit ={
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
