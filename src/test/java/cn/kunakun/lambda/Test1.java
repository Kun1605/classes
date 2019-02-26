package cn.kunakun.lambda;

import org.junit.Test;

public class Test1 {
	/*
	 * 开始使用Java 8时，
	 * 首先做的就是使用lambda表达式替换匿名类，而实现Runnable接口是匿名类的最好示例。
	 * 看一下Java 8之前的runnable实现方法，需要4行代码，而使用lambda表达式只需要一行代码。我们在这里做了什么呢？那就是用() -> {}代码块替代了整个匿名类。
	 */
	@Test
	public void test1() {
		new Thread(()->{System.out.println("hello lambda");}).start();
	}
	
}
