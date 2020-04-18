package com.haya.spark

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrixDashboard
@ComponentScan(value = Array("com.haya"))
@SpringBootApplication
class SparkApplication


object SparkApplication extends App  {

  SpringApplication.run(classOf[SparkApplication])

}

