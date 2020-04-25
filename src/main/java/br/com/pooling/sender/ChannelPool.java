package br.com.pooling.sender;

import com.rabbitmq.client.Channel;

import br.com.pooling.objectpool.ObjectPool;
import br.com.pooling.objectpool.ObjectPoolFactory;

public class ChannelPool extends ObjectPool<Channel> {

	private static final int POOL_SIZE = 10;
	
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
			instance = new ChannelPool(ConnectionManager.getInstance(), POOL_SIZE);
			return instance;
		}
	}

	@Override
	protected boolean validate(Channel channel) {
		return channel != null && channel.isOpen();
	}

}
