package br.com.pooling.objectpool;

public interface ObjectPoolFactory<T> {

	T create();
	
}
