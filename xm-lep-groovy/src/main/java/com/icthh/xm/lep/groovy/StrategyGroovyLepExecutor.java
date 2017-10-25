package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.ExtensionService;
import com.icthh.xm.lep.api.LepExecutorEvent.AfterResourceExecutionEvent;
import com.icthh.xm.lep.api.LepExecutorEvent.BeforeResourceExecutionEvent;
import com.icthh.xm.lep.api.LepExecutorEvent.ResultObject;
import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepKey;
import com.icthh.xm.lep.api.LepManagerService;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.LepResourceKey;
import com.icthh.xm.lep.api.Version;
import com.icthh.xm.lep.api.commons.BaseLepExecutor;
import com.icthh.xm.lep.api.commons.UrlLepResourceKey;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.util.Objects;

/**
 * The {@link StrategyGroovyLepExecutor} class.
 */
public class StrategyGroovyLepExecutor extends BaseLepExecutor implements GroovyScriptRunner {

    private final ScriptNameLepResourceKeyMapper scriptNameMapper;

    private final GroovyScriptEngineProviderStrategy providerStrategy;

    private final GroovyExecutionStrategy execStrategy;

    public StrategyGroovyLepExecutor(ScriptNameLepResourceKeyMapper scriptNameMapper,
                                     GroovyScriptEngineProviderStrategy providerStrategy,
                                     GroovyExecutionStrategy execStrategy) {
        this.scriptNameMapper = Objects.requireNonNull(scriptNameMapper, "scriptNameMapper can't be null");
        this.providerStrategy = Objects.requireNonNull(providerStrategy, "providerStrategy can't be null");
        this.execStrategy = Objects.requireNonNull(execStrategy, "execStrategy can't be null");
    }

    @Override
    public Object execute(LepKey extensionKey,
                          Version extensionResourceVersion,
                          LepMethod method,
                          LepManagerService managerService) throws LepInvocationCauseException {
        // validate input parameters
        Objects.requireNonNull(managerService, "managerService can't be null");
        Objects.requireNonNull(method, "method can't be null");
        Objects.requireNonNull(method.getMethodSignature(), "methodSignature can't be null");
        ExtensionService extensionService = Objects.requireNonNull(
            managerService.getExtensionService(),
            "managerService.getExtensionService() can't be null");

        // resolve resource key by extension key
        LepResourceKey resourceKey = extensionService.getResourceKey(extensionKey,
                                                                     extensionResourceVersion);
        if (resourceKey == null) {
            throw new IllegalStateException(
                "Can't find resource key for extension key: " + extensionKey.getId());
        }

        // cast resource key to URL resource key
        if (!(resourceKey instanceof UrlLepResourceKey)) {
            throw new IllegalArgumentException("Unsupported resource class " + resourceKey.getClass());
        }
        UrlLepResourceKey urlLepResourceKey = UrlLepResourceKey.class.cast(resourceKey);

        // execute LEP resource
        return execStrategy.executeLepResource(urlLepResourceKey, method, managerService, () -> this);
    }

    // ===== GroovyScriptRunner methods

    @Override
    public ScriptNameLepResourceKeyMapper getResourceKeyMapper() {
        return scriptNameMapper;
    }

    @Override
    public Object runScript(UrlLepResourceKey lepResourceKey,
                            LepMethod lepMethod,
                            LepManagerService managerService,
                            String scriptName,
                            Binding binding) throws LepInvocationCauseException {
        ResultObject resultObject = null;
        try {
            // Fire BeforeResourceExecutionEvent
            fireEvent(new BeforeResourceExecutionEvent(this, lepResourceKey, lepMethod));

            // Execute LEP resource
            Object value = getGroovyScriptEngine(managerService).run(scriptName, binding);
            resultObject = new ResultObject(value);
            return value;
        } catch (ResourceException e) {
            resultObject = new ResultObject(e);
            throw new GroovyLepExecutorException(
                "LEP GroovyEngine resource exception: " + e.getMessage(), e);
        } catch (ScriptException e) {
            // FIXME add cause detection for LepInvocationCauseException
            resultObject = new ResultObject(e);
            throw new GroovyLepExecutorException(
                "LEP GroovyEngine script exception: " + e.getMessage(), e);
        } catch (Exception e) {
            resultObject = new ResultObject(e);
            throw new LepInvocationCauseException(e);
        } finally {
            // Fire AfterResourceExecutionEvent
            fireEvent(new AfterResourceExecutionEvent(this, lepResourceKey, lepMethod, resultObject));
        }
    }

    private GroovyScriptEngine getGroovyScriptEngine(LepManagerService managerService) {
        return providerStrategy.getEngine(managerService);
    }

}
