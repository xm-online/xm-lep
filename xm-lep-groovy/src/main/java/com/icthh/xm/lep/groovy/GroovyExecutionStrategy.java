package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepManagerService;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.commons.UrlLepResourceKey;

import java.util.function.Supplier;

/**
 * The {@link GroovyExecutionStrategy} interface.
 */
public interface GroovyExecutionStrategy extends ExecutionStrategy<GroovyScriptRunner, UrlLepResourceKey> {

    /**
     * {@inheritDoc}
     */
    @Override
    Object executeLepResource(UrlLepResourceKey resourceKey,
                              LepMethod method,
                              LepManagerService managerService,
                              Supplier<GroovyScriptRunner> resourceExecutorSupplier) throws LepInvocationCauseException;

}
