package com.ishan.springRabbitMQ.staticQueues.consumer;

import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

  public void handleMessage(String message) {
    System.out.println("Listener | Handle Message");
    System.out.println(message);
  }

}
