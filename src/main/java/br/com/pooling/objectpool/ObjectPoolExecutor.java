package br.com.pooling.objectpool;

@FunctionalInterface
public interface ObjectPoolExecutor<T, R> {

	R execute(T object);
	
}