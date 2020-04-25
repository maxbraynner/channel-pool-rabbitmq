package br.com.pooling.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import br.com.pooling.sender.ConnectionManager;
import br.com.pooling.sender.Queue;

public class HelloReceiver {

	public static void main(String[] argv) throws Exception {
		Channel channel = ConnectionManager.getInstance().create();

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        
        channel.basicConsume(Queue.HELLO.getName(), true, deliverCallback, consumerTag -> { });
	}

}
