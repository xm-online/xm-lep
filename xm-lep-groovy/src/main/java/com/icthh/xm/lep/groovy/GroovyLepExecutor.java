package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.ContextScopes;
import com.icthh.xm.lep.api.LepExecutor;
import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepManagerService;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.ScopedContext;
import com.icthh.xm.lep.api.commons.UrlLepResourceKey;
import groovy.lang.Binding;

import java.util.function.Supplier;

/**
 * The {@link GroovyLepExecutor} class.
 */
public class GroovyLepExecutor extends StrategyGroovyLepExecutor implements LepExecutor {

    public GroovyLepExecutor(ScriptNameLepResourceKeyMapper resourceKeyMapper) {
        super(resourceKeyMapper,
              new LazyGroovyScriptEngineProviderStrategy(resourceKeyMapper),
              new InnerGroovyExecutionStrategy());
    }

    private static class InnerGroovyExecutionStrategy implements GroovyExecutionStrategy {

        @Override
        public Object executeLepResource(UrlLepResourceKey resourceKey,
                                         LepMethod method,
                                         LepManagerService managerService,
                                         Supplier<GroovyScriptRunner> resourceExecutorSupplier)
            throws LepInvocationCauseException {
            GroovyScriptRunner groovyScriptRunner = resourceExecutorSupplier.get();

            ScriptNameLepResourceKeyMapper resourceKeyMapper = groovyScriptRunner.getResourceKeyMapper();
            String scriptName = resourceKeyMapper.map(resourceKey);
            Binding binding = buildBinding(managerService, method);

            return groovyScriptRunner.runScript(resourceKey, method, managerService, scriptName, binding);
        }

        private Binding buildBinding(LepManagerService managerService,
                                     LepMethod method) {
            Binding binding = new Binding();

            // add execution context values
            ScopedContext executionContext = managerService.getContext(ContextScopes.EXECUTION);
            if (executionContext != null) {
                executionContext.getValues().forEach(binding::setVariable);
            }

            // add method arg values
            final String[] parameterNames = method.getMethodSignature().getParameterNames();
            final Object[] methodArgValues = method.getMethodArgValues();
            for (int i = 0; i < parameterNames.length; i++) {
                String paramName = parameterNames[i];
                Object paramValue = methodArgValues[i];

                binding.setVariable(paramName, paramValue);
            }

            return binding;
        }
    }

}
