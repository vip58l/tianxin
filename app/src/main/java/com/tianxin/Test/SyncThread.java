/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/28 0028
 */


package com.tianxin.Test;

/**
 * 指定要给某个对象加锁
 * 1.为什么要使用synchronized
 * 在并发编程中存在线程安全问题，主要原因有：1.存在共享数据 2.多线程共同操作共享数据。关键字synchronized可以保证在同一时刻，只有一个线程可以执行某个方法或某个代码块，同时synchronized可以保证一个线程的变化可见（可见性）
 *synchronized可以保证方法或者代码块在运行时，同一时刻只有一个方法可以进入到临界区，同时它还可以保证共享变量的内存可见性
 *
 */
public class SyncThread implements Runnable {

    private static int count;

    public SyncThread() {
        count = 0;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void run() {
        //一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞
        synchronized (this) {

            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + (count++));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
