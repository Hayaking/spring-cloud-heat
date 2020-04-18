package com.haya.spark.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class KafkaConfig {
  @Bean def topic1 = new NewTopic("topic.q1", 8, 1.toShort)

  @Bean def topic2 = new NewTopic("topic.q2", 8, 1.toShort)

  @Bean def topic3 = new NewTopic("topic.q3", 8, 1.toShort)
}
