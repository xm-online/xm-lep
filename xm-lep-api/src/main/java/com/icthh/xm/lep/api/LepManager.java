package com.icthh.xm.lep.api;

import java.util.function.Consumer;

/**
 * The {@link LepManager} interface.
 */
public interface LepManager extends LepManagerService {

    void init(ExtensionService extensionService,
              LepResourceService resourceService,
              LepExecutor executor);

    void destroy();

    void beginThreadContext(Consumer<? super ScopedContext> contextInitAction);

    void endThreadContext();

    /**
     * Process LEP and return extension point result value.
     *
     * @param key                      LEP key (complete key if keyResolver is null, or base key if
     *                                 keyResolver is not null)
     * @param extensionResourceVersion extension resource version or {@code null}
     * @param keyResolver              dynamic LEP key resolver, can be {@code null}
     * @param method                   callable method data
     * @return LEP result value
     */
    Object processLep(LepKey key,
                      Version extensionResourceVersion,
                      LepKeyResolver keyResolver,
                      LepMethod method) throws LepInvocationCauseException;

    /**
     * Add processing listener to manager.
     *
     * @param listener the listener to add
     * @return {@code true} if the specified listener successfully added or manager already
     * contains the specified listener
     */
    boolean registerProcessingListener(LepProcessingListener listener);

    /**
     * Removes processing listener from manager.
     *
     * @param listener the listener to remove
     * @return {@code true} if this manager contained the specified listener
     */
    boolean unregisterProcessingListener(LepProcessingListener listener);

    /**
     * Add manager listener.
     *
     * @param listener the listener to add
     * @return {@code true} if the specified listener successfully added or manager already
     * contains the specified listener
     */
    boolean registerLepManagerListener(LepManagerListener listener);

    /**
     * Removes manager listener.
     *
     * @param listener the listener to remove
     * @return {@code true} if this manager contained the specified listener
     */
    boolean unregisterLepManagerListener(LepManagerListener listener);

}
