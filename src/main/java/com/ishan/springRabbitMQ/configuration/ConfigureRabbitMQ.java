package com.ishan.springRabbitMQ.configuration;

import com.ishan.springRabbitMQ.staticQueues.consumer.MessageConsumer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMQ {

  public static final String QUEUE_NAME = "staticqueue";

  public static final String EXCHANGE_NAME = "staticechange";

  //Create Queue
  @Bean
  Queue queue() {
    return new Queue(QUEUE_NAME, true, false, false);
  }

  //Create an Exchange
  @Bean
  TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  //Create binding b/w Queue and Exchange
  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        // Anything with the routing key
        // (you send this in the message header when you send a message)
        // staticqueue.* will match and will be routed from the given
        // exchange to given queue
        // #->Pound key (single or multiple matches)
        .with("staticqueue.#");
  }

  // Create the Listener
  // Spring will use reflection to invoke the correct method(here - handleMessage)
  @Bean
  MessageListenerAdapter listenerAdapter(MessageConsumer messageConsumer) {
    return new MessageListenerAdapter(
        messageConsumer,
        "handleMessage"
    );
  }

  @Bean
  ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
        "127.0.0.1",
        Integer.valueOf("5672"));
    connectionFactory.setUsername("test");
    connectionFactory.setPassword("test");
    connectionFactory.setVirtualHost("/");
    return connectionFactory;
  }

  // Create Listener Container
  // The Default Connection Factory will use localhost and port 5672
  // if you do-not specifically create a ConnectionFactory bean
  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(QUEUE_NAME);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

}
