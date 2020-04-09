package com.haya.spark

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrixDashboard
@ComponentScan(value = Array("com.haya"))
@SpringBootApplication
class SparkApplication

object SparkApplication extends App{

  SpringApplication.run(classOf[SparkApplication])

}

