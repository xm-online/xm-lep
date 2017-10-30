package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.LepExecutor;
import com.icthh.xm.lep.api.LepExecutorEvent;
import com.icthh.xm.lep.api.LepExecutorListener;
import com.icthh.xm.lep.api.util.ListenersHolder;

/**
 * The {@link BaseLepExecutor} class.
 */
public abstract class BaseLepExecutor implements LepExecutor {

    /**
     * Holder for LepExecutorListener's.
     */
    private final ListenersHolder<LepExecutorListener, LepExecutorEvent> executorListeners = new ListenersHolder<>();

    protected void fireEvent(LepExecutorEvent event) {
        executorListeners.fireEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean registerExecutorListener(LepExecutorListener listener) {
        return executorListeners.registerListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unregisterExecutorListener(LepExecutorListener listener) {
        return executorListeners.unregisterListener(listener);
    }

}
