package cn.kunakun.common.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YangKun
 * @date 2018年2月8日上午11:16:43
 */
public class IdleConnectionEvictor extends Thread {
	private static Logger logger = LoggerFactory.getLogger(IdleConnectionEvictor.class);

	private final HttpClientConnectionManager connMgr;

	private volatile boolean shutdown;

	public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
		this.connMgr = connMgr;
		// 启动当前线程
		this.start();
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {

					wait(5000);
					// 关闭失效的连接
					// logger.debug("自动清理无效httpClient执行");
					connMgr.closeExpiredConnections();
				}
			}
		} catch (InterruptedException ex) {
			// 结束
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}