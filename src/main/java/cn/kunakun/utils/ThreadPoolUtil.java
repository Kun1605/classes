package cn.kunakun.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import cn.kunakun.utils.ThreadPoolBuilder.FixedThreadPoolBuilder;

/**
 * =====================================================================================
 *
 * @Filename    :   ThreadPoolUtil.java
 * @Description :   线程池工具集
 * @Version     :   2.0
 * @Created     :   2017年9月6日 19:02:29
 * @Compiler    :   jdk 1.8
 * @Author      :   冯兵兵
 * @Email       :   cnrainbing@163.com
 * @Copyright   :   简媒(http://www.ejianmedia.com/)
 * =====================================================================================
 */
public class ThreadPoolUtil {

	private final static ExecutorService executorService = new FixedThreadPoolBuilder().setPoolSize(200).build();

	/***
	 * 初始化线程池
	 * @param task
	 */
	public static void executorThreadPool(Runnable task) {
		executorService.execute(task);
	}

	/**
	 * 创建ThreadFactory，使得创建的线程有自己的名字而不是默认的"pool-x-thread-y"
	 * 
	 * 使用了Guava的工具类
	 * 
	 * @see ThreadFactoryBuilder#build()
	 */
	public static ThreadFactory buildThreadFactory(@NotNull String threadNamePrefix) {
		return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
	}

	/**
	 * 可设定是否daemon, daemon线程在主线程已执行完毕时, 不会阻塞应用不退出, 而非daemon线程则会阻塞.
	 * 
	 * @see   #buildThreadFactory
	 */
	public static ThreadFactory buildThreadFactory(@NotNull String threadNamePrefix, @NotNull boolean daemon) {
		return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
	}

	/**
	 * 防止用户没有捕捉异常导致中断了线程池中的线程, 使得SchedulerService无法继续执行.
	 * 
	 * 在无法控制第三方包的Runnable实现时，调用本函数进行包裹.
	 */
	public static Runnable safeRunnable(@NotNull Runnable runnable) {
		return new SafeRunnable(runnable);
	}

	/**
	 * 保证不会有Exception抛出到线程池的Runnable包裹类，防止用户没有捕捉异常导致中断了线程池中的线程,
	 * 使得SchedulerService无法执行. 在无法控制第三方包的Runnalbe实现时，使用本类进行包裹.
	 */
	public static class SafeRunnable implements Runnable {

		private static Logger logger = LoggerFactory.getLogger(SafeRunnable.class);

		private Runnable runnable;

		public SafeRunnable(Runnable runnable) {
			Validate.notNull(runnable);
			this.runnable = runnable;
		}

		@Override
		public void run() {
			try {
				runnable.run();
			} catch (Throwable e) {
				// catch any exception, because the scheduled thread will break
				// if the exception thrown to outside.
				logger.error("Unexpected error occurred in task", e);
			}
		}
	}
}
