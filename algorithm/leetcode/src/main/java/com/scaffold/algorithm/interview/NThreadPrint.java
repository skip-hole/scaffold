package com.scaffold.algorithm.interview;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hui.zhang
 * @date 2022年07月28日 18:03
 */
public class NThreadPrint {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static Condition condition3 = lock.newCondition();
    private static volatile int count = 0;


    public static void main(String[] args) {
        new Thread(thread1).start();
        new Thread(thread2).start();
        new Thread(thread3).start();
    }

    private static Runnable thread1 = () -> {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                if (count % 3 != 0) {
                    condition1.await();
                }
                System.out.println(1);
                count++;
                condition2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    };

    private static Runnable thread2 = () -> {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                if (count % 3 != 1) {
                    condition2.await();
                }
                System.out.println(2);
                count++;
                condition3.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    };

    private static Runnable thread3 = () -> {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                if (count % 3 != 1) {
                    condition3.await();
                }
                System.out.println(3);
                count++;
                condition1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    };

}
