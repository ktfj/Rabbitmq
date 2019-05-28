package com.example.producer;

import java.util.Map;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.example.entity.Order;

@Component
public class Producer {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	final RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback() {
		//参数 cause：异常消息。
		@Override
		public void confirm(CorrelationData correlationData, boolean ack,
				String cause) {
			System.err.println("correlationData: " + correlationData);
			//说明broker没有收到消息。
			if(!ack){
				System.err.println("日志记录boker没有收到的消息....");
			}
			
		}
	};
	//消息没有进入消息队列中。或者 因为routeKey不对。
	final RabbitTemplate.ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
		
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message,
				int replyCode, String replyText, String exchange, String routingKey) {
			// TODO Auto-generated method stub
			System.err.println("return exchange: " + exchange + ", routingKey: " 
					+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
		}
	};
	
	public void sendMessage(Object message,Map<String,Object> properties){
		
		MessageHeaders mhs=new MessageHeaders(properties);
		Message msg=MessageBuilder.createMessage(message, mhs);
		
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		CorrelationData cd=new CorrelationData();
		cd.setId("123456");//这个Id保证消息全局唯一，在可靠性投递中,就用该Id来找到未成功投递的消息。
		rabbitTemplate.convertAndSend("exchange-1", "springBoot.a", msg, cd);
		
	}
	
	/**
	 * 直接发送order对象。
	 * 注意：如果对象没有序列化 会报错。
	 * 只能发送 string、 byte[]、serializable payloads. 
	 * @param order
	 */
	
    public void sendOrder(Order order){
    	rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		CorrelationData cd=new CorrelationData();
		cd.setId("123456");//这个Id保证消息全局唯一，在可靠性投递中,就用该Id来找到未成功投递的消息。
		rabbitTemplate.convertAndSend("exchange-2", "springboot.a", order, cd);
		
	}


}
