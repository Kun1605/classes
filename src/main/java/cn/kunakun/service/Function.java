package cn.kunakun.service;

public interface Function<T,E> {
	public T callback(E e);
}
