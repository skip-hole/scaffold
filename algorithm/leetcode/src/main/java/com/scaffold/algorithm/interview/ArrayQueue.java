package com.scaffold.algorithm.interview;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hui.zhang
 * @date 2022年07月27日 18:37
 */
public class ArrayQueue<E> {

    final Object[] items;
    int takeIndex;
    int putIndex;
    int count;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public ArrayQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.items = new Object[capacity];
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void put(E e) throws InterruptedException {
        checkNotNull(e);
        //1、加快读取速度，直接从线程栈读取 2、线程安全 final初始化后不再改变 防止内存重排序，对象的可见性
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == items.length) {
                notFull.await();
            }
            final Object[] items = this.items;
            items[putIndex] = e;
            if (++putIndex == items.length) {
                putIndex = 0;
            }
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            final Object[] items = this.items;
            E x = (E) items[takeIndex];
            items[takeIndex] = null;
            if (++takeIndex == items.length) {
                takeIndex = 0;
            }
            count--;
            return x;
        } finally {
            lock.unlock();
        }
    }

    public synchronized int size() {
        return count;
    }

    public synchronized void clear() {
        int k = count;
        if (k > 0) {
            final int putIndex = this.putIndex;
            int i = takeIndex;
            do {
                items[i] = null;
                if (++i == items.length) {
                    i = 0;
                }
            } while (i != putIndex);
            takeIndex = putIndex;
            count = 0;
            notFull.signal();
        }
    }

    private static void checkNotNull(Object v) {
        if (v == null) {
            throw new NullPointerException();
        }
    }
}
