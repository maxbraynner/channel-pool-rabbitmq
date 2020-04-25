package br.com.pooling.sender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.com.pooling.objectpool.ObjectPoolFactory;

public class QueueManager implements ObjectPoolFactory<Channel> {

	private static QueueManager instance;

	private final ConnectionFactory factory;
	private Connection connection;

	private QueueManager() throws IOException, TimeoutException {
		factory = new ConnectionFactory();
		factory.setHost("localhost");

		manageConnection();
	}

	public static QueueManager getInstance() {
		try {
			if (instance != null) {
				return instance;
			}

			synchronized (QueueManager.class) {
				if (instance != null) {
					return instance;
				}
				instance = new QueueManager();
				return instance;
			}
		} catch (IOException | TimeoutException up) {
			throw new RuntimeException(up);
		}
	}

	private void manageConnection() throws IOException, TimeoutException {
		if (connection != null && connection.isOpen()) {
			return;
		}

		synchronized (this) {
			if (connection != null && connection.isOpen()) {
				return;
			}
			connection = factory.newConnection();
		}
	}

	public boolean pushMessage(final String message) {
		ChannelPool instance = ChannelPool.getInstance();
		return instance.execute(channel -> {
			try {
				channel.basicPublish("", "hello", null, message.getBytes("UTF-8"));
				return true;
			} catch (IOException up) {
				up.printStackTrace();
				return false;
			}
		});
	}

	@Override
	public Channel create() {
		try {
			manageConnection();
			return connection.createChannel();
		} catch (IOException | TimeoutException up) {
			throw new RuntimeException(up);
		}
	}

}
