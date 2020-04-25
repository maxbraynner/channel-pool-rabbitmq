package br.com.pooling.sender;

import com.rabbitmq.client.AMQP.BasicProperties;

public enum Queue {

	HELLO("", "hello") {
		@Override
		public BasicProperties getProps() {
			return null;
		}
	}

	;

	private final String exchange;
	private final String name;

	Queue(String exchange, String name) {
		this.exchange = exchange;
		this.name = name;
	}

	public abstract BasicProperties getProps();

	public String getName() {
		return this.name;
	}

	public String getExchange() {
		return exchange;
	}

}
