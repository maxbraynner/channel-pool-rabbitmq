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

	private ConnectionManager() {
		factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		factory.setUsername(username);
//		factory.setPassword(password);

		manageConnection();
	}

	public static ConnectionManager getInstance() {
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
	}

	private Connection manageConnection() {
		if (connection != null && connection.isOpen()) {
			return connection;
		}

		synchronized (this) {
			if (connection != null && connection.isOpen()) {
				return connection;
			}

			try {
				connection = factory.newConnection();
			} catch (IOException | TimeoutException up) {
				throw new RuntimeException(up);
			}

			return connection;
		}
	}

	@Override
	public Channel create() {
		manageConnection();
		try {
			return connection.createChannel();
		} catch (IOException up) {
			throw new RuntimeException(up);
		}
	}

}
