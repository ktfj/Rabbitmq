package com.example.consumer;

import java.io.IOException;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.entity.Order;
import com.rabbitmq.client.Channel;

@Component
public class Consumer {
	//@RabbitListener 可以建立关系。并且 可以创建这些exchange等。
    @RabbitListener(
    	bindings=@QueueBinding(
    			value=@Queue(value="queue-1",durable="true"), 
    			exchange = @Exchange(value="exchange-1",durable="true",type="topic",ignoreDeclarationExceptions="true"),
    			key="springBoot.#"
    			)
    )  
    @RabbitHandler
	public void consume(Message message,Channel channel) throws IOException{
    	//消息体 --body
    	String payLoad=message.getPayload().toString();
    	//消息头--header
    	MessageHeaders mh=message.getHeaders();
    	Long tag=(Long) mh.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("消费端收到的消息："+payLoad);
    	//手工ack，
    	channel.basicAck(tag, false);
    	
	}
    /**
     * 直接接受对象。
     * @param message
     * @param channel
     * @throws IOException
     */
    
    @RabbitListener(
    		bindings=@QueueBinding(
    				value=@Queue(value="${spring.rabbitmq.listener.order.queue.name}",durable="${spring.rabbitmq.listener.order.queue.durable}"),
    				exchange=@Exchange(
    					value="${spring.rabbitmq.listener.order.exchange.name}",
	    				type="${spring.rabbitmq.listener.order.exchange.type}",
	    				durable="${spring.rabbitmq.listener.order.exchange.durable}",
	    				ignoreDeclarationExceptions="${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"
    				),
    				key="${spring.rabbitmq.listener.order.key}"
    			)
    		)
    
	public void consumeObj(@Payload Order order,Channel channel,@Headers Map<String,Object> headers) throws IOException{
    	Long tag=(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("消费端收到的消息："+order.getId());
    	//手工ack，
    	channel.basicAck(tag, false);
    	
	}
 
}
