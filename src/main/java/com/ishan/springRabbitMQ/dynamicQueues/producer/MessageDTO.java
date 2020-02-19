package com.ishan.springRabbitMQ.dynamicQueues.producer;

public class MessageDTO {
  private String queue;
  private String message;

  public String getQueue() {
    return queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
