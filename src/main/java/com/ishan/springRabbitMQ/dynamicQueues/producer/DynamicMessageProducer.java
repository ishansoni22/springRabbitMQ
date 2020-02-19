package com.ishan.springRabbitMQ.dynamicQueues.producer;

import com.ishan.springRabbitMQ.configuration.ConfigureRabbitMQ;
import java.util.UUID;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dynamic")
public class DynamicMessageProducer {

  @Autowired
  private AmqpAdmin amqpAdmin;

  @Autowired
  private SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private TopicExchange exchange;

  @PostMapping(value = "/send", consumes = {MediaType.APPLICATION_JSON_VALUE})
  public String message(@RequestBody MessageDTO messageDTO) {

    String queueName = messageDTO.getQueue();
    String message = messageDTO.getMessage();
    String id = UUID.randomUUID().toString().split("-")[0];

    Queue queue = new Queue(
        queueName, true, false, false);

    Binding binding = BindingBuilder
        .bind(queue)
        .to(exchange)
        .with("dynamicqueue." + id);

    amqpAdmin.declareQueue(queue);
    amqpAdmin.declareBinding(binding);

    simpleMessageListenerContainer.addQueueNames(queueName);

    rabbitTemplate.convertAndSend(
        ConfigureRabbitMQ.EXCHANGE_NAME,
        "dynamicqueue." + id,
        message
    );

    return "Dynamic | Queue Created & Message Sent";
  }

}
