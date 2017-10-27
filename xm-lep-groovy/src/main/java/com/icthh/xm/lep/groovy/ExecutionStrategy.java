package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepManagerService;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.LepResourceKey;

import java.util.function.Supplier;

/**
 * The {@link ExecutionStrategy} interface.
 */
public interface ExecutionStrategy<E, RK extends LepResourceKey> {

    /**
     * Executes LEP resource using some executor.
     *
     * @param resourceKey              LEP resource key
     * @param method                   LEP method info data
     * @param managerService           LEP manager service
     * @param resourceExecutorSupplier LEP resource executor supplier function
     * @return LEP method result
     * @throws LepInvocationCauseException when LEP invocation resource throws exception
     */
    Object executeLepResource(RK resourceKey,
                              LepMethod method,
                              LepManagerService managerService,
                              Supplier<E> resourceExecutorSupplier) throws LepInvocationCauseException;

}
