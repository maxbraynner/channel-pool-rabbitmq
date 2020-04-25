package br.com.pooling.objectpool;

@FunctionalInterface
public interface ObjectPoolFactory<T> {

	T create();
	
}
