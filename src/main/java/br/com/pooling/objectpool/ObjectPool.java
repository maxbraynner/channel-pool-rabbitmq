package br.com.pooling.objectpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public abstract class ObjectPool<T> {

	private final ObjectPoolFactory<T> factory;
	private final BlockingQueue<T> blockingQueue;
	
	private long timeout = 60;
	private TimeUnit timeoutUnit = TimeUnit.SECONDS;

	public ObjectPool(ObjectPoolFactory<T> factory, int poolSize) {
		super();
		this.factory = factory;
		blockingQueue = new LinkedBlockingDeque<T>(poolSize);
		
		for (int i = 0; i < poolSize; i++) {
			blockingQueue.add(factory.create());
		}
	}
	
	public <R> R execute(ObjectPoolExecutor<T, R> executor) {
		T object = poll();
		
		if (!validate(object)) {
			object = factory.create();
		}
		
		 R result = executor.exec(object);
		 
		 blockingQueue.add(object);
		 
		 return result;
	}

	/**
	 * recupera um objeto na fila
	 * @return
	 */
	private T poll() {
		try {
			return  blockingQueue.poll(timeout, timeoutUnit);
		} catch (InterruptedException up) {
			throw new RuntimeException(up);
		}
	}
	
	protected abstract boolean validate(T object);

	public long getTimeout() {
		return timeout;
	}

	public TimeUnit getTimeoutUnit() {
		return timeoutUnit;
	}

	public synchronized void setTimeout(long timeout, TimeUnit unit) {
		this.timeout = timeout;
		this.timeoutUnit = unit;
	}

}