package com.icthh.xm.lep.api;

/**
 * The {@link LepExecutor} interface.
 */
public interface LepExecutor {

    /**
     * Executes custom logic implementation for LEP.
     *
     * @param extensionKey             extension key
     * @param extensionResourceVersion extension resource version or {@code null} if not used
     * @param method                   method data on what LEP call occurs
     * @param managerService           LEP manager service object
     * @return LEP result value
     */
    Object execute(LepKey extensionKey,
                   Version extensionResourceVersion,
                   LepMethod method,
                   LepManagerService managerService) throws LepInvocationCauseException;

    /**
     * Add processing listener to executor.
     *
     * @param listener the listener to add
     * @return {@code true} if the specified listener successfully added or executor already
     * contains the specified listener
     */
    boolean registerExecutorListener(LepExecutorListener listener);

    /**
     * Removes processing listener from executor.
     *
     * @param listener the listener to remove
     * @return {@code true} if this executor contained the specified listener
     */
    boolean unregisterExecutorListener(LepExecutorListener listener);

}
