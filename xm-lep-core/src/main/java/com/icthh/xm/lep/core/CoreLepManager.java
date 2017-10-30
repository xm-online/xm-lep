package com.icthh.xm.lep.core;

import com.icthh.xm.lep.api.ExtensionService;
import com.icthh.xm.lep.api.LepExecutor;
import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepKey;
import com.icthh.xm.lep.api.LepKeyResolver;
import com.icthh.xm.lep.api.LepManager;
import com.icthh.xm.lep.api.LepManagerEvent;
import com.icthh.xm.lep.api.LepManagerEvent.LepManagerDestroyedEvent;
import com.icthh.xm.lep.api.LepManagerEvent.LepManagerInitedEvent;
import com.icthh.xm.lep.api.LepManagerListener;
import com.icthh.xm.lep.api.LepManagerServiceAware;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.LepProcessingEvent;
import com.icthh.xm.lep.api.LepProcessingEvent.AfterExecutionEvent;
import com.icthh.xm.lep.api.LepProcessingEvent.BeforeExecutionEvent;
import com.icthh.xm.lep.api.LepProcessingListener;
import com.icthh.xm.lep.api.LepResourceService;
import com.icthh.xm.lep.api.MethodSignature;
import com.icthh.xm.lep.api.ScopedContext;
import com.icthh.xm.lep.api.Version;
import com.icthh.xm.lep.api.util.ListenersHolder;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The {@link CoreLepManager} class.
 */
public class CoreLepManager implements LepManager {

    protected enum State {
        CREATED,
        INITIALIZED,
        DESTROYED;
    }

    /**
     * LEP manager state.
     */
    private volatile State currentState = State.CREATED;

    /**
     * Contexts holder instance.
     */
    private volatile CoreContextsHolder contextsHolder = new CoreContextsHolder();

    /**
     * Holder for LepProcessingListener's.
     */
    private final ListenersHolder<LepProcessingListener, LepProcessingEvent> processingListeners =
        new ListenersHolder<>();

    /**
     * Holder for LepManagerListener's.
     */
    private final ListenersHolder<LepManagerListener, LepManagerEvent> managerListeners = new ListenersHolder<>();

    /**
     * Extension service instance.
     */
    private ExtensionService extensionService;

    /**
     * Resource service instance.
     */
    private LepResourceService resourceService;

    /**
     * LEP executor instance.
     */
    private LepExecutor executor;

    private <T> T initLepManagerServiceAware(T obj) {
        if (obj instanceof LepManagerServiceAware) {
            LepManagerServiceAware.class.cast(obj).init(this);
        }
        return obj;
    }

    private void destroyLepManagerServiceAware(Object obj) {
        if (obj instanceof LepManagerServiceAware) {
            try {
                LepManagerServiceAware.class.cast(obj).destroy();
            } catch (Exception e) {
                // nop
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ExtensionService extensionService,
                     LepResourceService resourceService,
                     LepExecutor executor) {
        Objects.requireNonNull(extensionService, "extensionService can't be null");
        Objects.requireNonNull(resourceService, "resourceService can't be null");
        Objects.requireNonNull(executor, "executor can't be null");

        synchronized (this) {
            this.resourceService = initLepManagerServiceAware(resourceService);
            this.extensionService = initLepManagerServiceAware(extensionService);
            this.executor = initLepManagerServiceAware(executor);

            this.currentState = State.INITIALIZED;
        }

        managerListeners.fireEvent(new LepManagerInitedEvent(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        synchronized (this) {
            destroyLepManagerServiceAware(executor);
            destroyLepManagerServiceAware(extensionService);
            destroyLepManagerServiceAware(resourceService);

            this.executor = null;
            this.extensionService = null;
            this.resourceService = null;

            // cleanup thread local context var
            endThreadContext();

            this.currentState = State.DESTROYED;
        }

        managerListeners.fireEvent(new LepManagerDestroyedEvent(this));
    }

    protected final boolean isInitialized() {
        return getCurrentState() == State.INITIALIZED;
    }

    protected final void validateIsInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException(getClass().getSimpleName()
                                                + " not initialized (current state: " + currentState + ")");
        }
    }

    protected final State getCurrentState() {
        return currentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginThreadContext(Consumer<? super ScopedContext> contextInitAction) {
        Objects.requireNonNull(contextInitAction, "context init action can't be null");
        contextInitAction.accept(contextsHolder.beginThreadContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endThreadContext() {
        contextsHolder.endThreadContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtensionService getExtensionService() {
        return extensionService;
    }

    @Override
    public LepResourceService getResourceService() {
        return resourceService;
    }

    private LepExecutor getExecutor() {
        return executor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object processLep(LepKey baseKey,
                             Version extensionResourceVersion,
                             LepKeyResolver keyResolver,
                             LepMethod method) throws LepInvocationCauseException {
        validateIsInitialized();

        Objects.requireNonNull(baseKey, "baseKey can't be null");
        Objects.requireNonNull(method, "method can't be null");

        beginExecutionContext();

        LepKey extensionKey = null;
        Exception processingException = null;
        Object resultValue = null;
        try {
            // fire BeforeProcessingEvent
            processingListeners.fireEvent(new LepProcessingEvent.BeforeProcessingEvent(this, baseKey, method));

            if (keyResolver != null) {
                extensionKey = keyResolver.resolve(baseKey, method, this);
                if (extensionKey == null) {
                    throw new IllegalStateException("Key resolver: "
                                                        + keyResolver.getClass().getCanonicalName()
                                                        + " return null for "
                                                        + getLepDescription(baseKey, method));
                }
            } else {
                extensionKey = baseKey;
            }

            // fire BeforeExecutionEvent
            processingListeners.fireEvent(new BeforeExecutionEvent(this, extensionKey, method));

            // execute extension and get result value
            resultValue = getExecutor().execute(extensionKey, extensionResourceVersion,
                                                method, this);

            // fire AfterExecutionEvent
            processingListeners.fireEvent(new AfterExecutionEvent(this,
                                                                  extensionKey,
                                                                  method,
                                                                  resultValue));

            return resultValue;
        } catch (Exception e) {
            processingException = e;
            throw e;
        } finally {
            endExecutionContext();

            // fire AfterProcessingEvent
            processingListeners.fireEvent(new LepProcessingEvent.AfterProcessingEvent(this,
                                                                                      extensionKey,
                                                                                      method,
                                                                                      processingException,
                                                                                      resultValue));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean registerProcessingListener(LepProcessingListener listener) {
        return processingListeners.registerListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unregisterProcessingListener(LepProcessingListener listener) {
        return processingListeners.unregisterListener(listener);
    }

    @Override
    public boolean registerLepManagerListener(LepManagerListener listener) {
        return managerListeners.registerListener(listener);
    }

    @Override
    public boolean unregisterLepManagerListener(LepManagerListener listener) {
        return managerListeners.unregisterListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScopedContext getContext(String scope) {
        return contextsHolder.getContext(scope);
    }

    private void beginExecutionContext() {
        contextsHolder.beginExecutionContext();
    }

    private void endExecutionContext() {
        contextsHolder.endExecutionContext();
    }

    /**
     * Returns LEP detail description.
     *
     * @param key    LEP key
     * @param method LEP method
     * @return LEP detail description
     */
    private static String getLepDescription(LepKey key,
                                            LepMethod method) {
        final Object target = method.getTarget();
        final MethodSignature ms = method.getMethodSignature();
        final Object[] methodArgValues = method.getMethodArgValues();


        StringBuilder message = new StringBuilder();
        message.append("key: ").append(key).append(": ").append(key);
        message.append("; target object: ").append(target.getClass().getCanonicalName());
        message.append("; declaring class: ").append(ms.getDeclaringClassName());
        message.append("; method: ");
        message.append(Modifier.toString(ms.getModifiers())).append(' ');
        message.append(ms.getReturnType().getSimpleName()).append(' ');
        message.append(ms.getName()).append('(');
        for (int i = 0; i < ms.getParameterNames().length; i++) {
            Class<?> argType = ms.getParameterTypes()[i];
            String argName = ms.getParameterNames()[i];
            message.append(argType.getSimpleName()).append(' ').append(argName);
            if (i > 0) {
                message.append(", ");
            }
        }
        message.append(") ");
        if (ms.getExceptionTypes().length > 0) {
            message.append("throws ");
            boolean isFirst = true;
            for (Class<?> exceptionType : ms.getExceptionTypes()) {
                if (!isFirst) {
                    message.append(", ");
                }
                message.append(exceptionType.getSimpleName());

                isFirst = false;
            }
        }
        message.append("; arg values: ");
        for (int i = 0; i < ms.getParameterNames().length; i++) {
            message.append(ms.getParameterNames()[i]);
            message.append(": ");
            message.append(methodArgValues[i]);
        }
        return message.toString();
    }

}
