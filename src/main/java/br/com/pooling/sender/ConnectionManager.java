package br.com.pooling.sender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.com.pooling.objectpool.ObjectPoolFactory;

public class ConnectionManager implements ObjectPoolFactory<Channel> {

	private static ConnectionManager instance;

	private final ConnectionFactory factory;
	private Connection connection;

	private ConnectionManager() throws IOException, TimeoutException {
		factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		factory.setUsername(username);
//		factory.setPassword(password);

		manageConnection();
	}

	public static ConnectionManager getInstance() {
		try {
			if (instance != null) {
				return instance;
			}

			synchronized (ConnectionManager.class) {
				if (instance != null) {
					return instance;
				}
				instance = new ConnectionManager();
				return instance;
			}
		} catch (IOException | TimeoutException up) {
			throw new RuntimeException(up);
		}
	}

	private Connection manageConnection() throws IOException, TimeoutException {
		if (connection != null && connection.isOpen()) {
			return connection;
		}

		synchronized (this) {
			if (connection != null && connection.isOpen()) {
				return connection;
			}
			connection = factory.newConnection();
			return connection;
		}
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
