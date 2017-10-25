package com.icthh.xm.lep.api.util;

import java.util.EventObject;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * The {@link ListenersHolder} class.
 */
public class ListenersHolder<L extends Consumer<E>, E extends EventObject> {

    /**
     * Collection of listeners.
     */
    private final LinkedHashSet<L> listeners = new LinkedHashSet<>();

    /**
     * Fair read/write lock for listeners collection.
     */
    private final ReadWriteLock listenersReadWriteLock = new ReentrantReadWriteLock(true);

    /**
     * Read lock for listeners collection.
     */
    private final Lock listenersReadLock = listenersReadWriteLock.readLock();

    /**
     * Write lock for listeners collection.
     */
    private final Lock listenersWriteLock = listenersReadWriteLock.writeLock();

    public boolean registerListener(L listener) {
        listenersWriteLock.lock();
        try {
            return listeners.add(listener) || listeners.contains(listener);
        } finally {
            listenersWriteLock.unlock();
        }
    }

    public boolean unregisterListener(L listener) {
        listenersWriteLock.lock();
        try {
            return listeners.remove(listener);
        } finally {
            listenersWriteLock.unlock();
        }
    }

    public void fireEvent(E event) {
        listenersReadLock.lock();
        try {
            listeners.forEach(listener -> listener.accept(event));
        } finally {
            listenersReadLock.unlock();
        }
    }

}
