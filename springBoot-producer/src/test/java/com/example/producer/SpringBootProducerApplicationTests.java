package com.example.producer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.entity.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootProducerApplicationTests {
	@Autowired
    private Producer producer;
	@Test
	public void contextLoads() {
	}
	@Test
	public void sendMessage(){
		Map<String,Object> properties=new HashMap<String,Object>();
		properties.put("time", "2019-05-27");
		
		String message="this is String Hello world!";
		producer.sendMessage(message, properties);
		
	}
	
	@Test
	public void sendOrder(){
		Order order=new Order();
		order.setId("UUID-kheyskiei-key");
		order.setOrderNo("LYSC1234567");
		producer.sendOrder(order);
		
	}

}
