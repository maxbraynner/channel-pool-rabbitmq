package br.com.pooling.sender;

import com.rabbitmq.client.Channel;

import br.com.pooling.objectpool.ObjectPool;
import br.com.pooling.objectpool.ObjectPoolFactory;

public class ChannelPool extends ObjectPool<Channel> {

	private static ChannelPool instance = null;

	private ChannelPool(ObjectPoolFactory<Channel> factory, int poolSize) {
		super(factory, poolSize);
	}

	public static ChannelPool getInstance() {
		if (instance != null) {
			return instance;
		}

		synchronized (ChannelPool.class) {
			if (instance != null) {
				return instance;
			}
			instance = new ChannelPool(QueueManager.getInstance(), 10);
			return instance;
		}
	}

	@Override
	protected boolean validate(Channel channel) {
		return channel != null && channel.isOpen();
	}

}
