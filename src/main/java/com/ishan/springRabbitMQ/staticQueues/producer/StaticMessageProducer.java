package com.ishan.springRabbitMQ.staticQueues.producer;

import com.ishan.springRabbitMQ.configuration.ConfigureRabbitMQ;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/static")
public class StaticMessageProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @PostMapping("/send")
  public String message(@RequestBody String message) {
    rabbitTemplate.convertAndSend(
        ConfigureRabbitMQ.EXCHANGE_NAME,
        "staticqueue.message",
        message
    );
    return "Static | Message Sent";
  }

}
