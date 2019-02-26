package events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestExceptionCatch {
    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newCachedThreadPool(new MyThreadFactory());
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        for (int i = 0; i < 10; i++) {
            executorService.submit(new MyTask());
        }
        try {
            final Thread thread = new Thread(new MyTask());
            //重点
            thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            thread.start();
        } catch (Exception e) {
            System.out.println("main Thread:");
            e.printStackTrace();
        }
    }

    public static class MyTask implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "");
            throw new RuntimeException("jiu shi gu yi de");
        }
    }

    public static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(t.getName() + ":");
            e.printStackTrace();
        }
    }

    public static class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            System.out.println("create " + t.getName());
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());//为每一个线程添加一个处理线程异常的类
            return t;
        }
    }
}