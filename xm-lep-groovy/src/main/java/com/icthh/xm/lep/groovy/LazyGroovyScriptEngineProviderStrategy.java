package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.ContextsHolder;
import com.icthh.xm.lep.api.LepManagerService;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;

import java.util.Objects;

/**
 * The {@link LazyGroovyScriptEngineProviderStrategy} class.
 */
public class LazyGroovyScriptEngineProviderStrategy implements GroovyScriptEngineProviderStrategy {

    private final ScriptNameLepResourceKeyMapper resourceKeyMapper;

    private volatile GroovyScriptEngine groovyScriptEngine;

    public LazyGroovyScriptEngineProviderStrategy(ScriptNameLepResourceKeyMapper resourceKeyMapper) {
        this.resourceKeyMapper = Objects.requireNonNull(resourceKeyMapper, "resourceKeyMapper can't be null");
    }

    /**
     * Strategy: build new instance on first call.
     *
     * @param managerService LEP manager service
     * @return GroovyScriptEngine instance
     */
    @Override
    public GroovyScriptEngine getEngine(LepManagerService managerService) {
        GroovyScriptEngine engine = this.groovyScriptEngine;
        if (engine == null) {
            synchronized (this) {
                engine = this.groovyScriptEngine;
                if (engine == null) {
                    this.groovyScriptEngine = engine = buildGroovyScriptEngine(managerService);
                }
            }
        }

        return engine;
    }

    private GroovyScriptEngine buildGroovyScriptEngine(LepManagerService managerService) {
        final ClassLoader parentClassLoader = getParentClassLoader();
        final ResourceConnector rc = buildResourceConnector(managerService);

        GroovyScriptEngine engine = (parentClassLoader == null)
            ? new GroovyScriptEngine(rc)
            : new GroovyScriptEngine(rc, parentClassLoader);

        initGroovyScriptEngine(engine, managerService);
        return engine;
    }

    protected ClassLoader getParentClassLoader() {
        return null;
    }

    protected void initGroovyScriptEngine(GroovyScriptEngine engine,
                                          ContextsHolder contextsHolder) {
        // nop by default
    }

    protected ResourceConnector buildResourceConnector(LepManagerService managerService) {
        return new LepScriptResourceConnector(managerService, resourceKeyMapper);
    }

}
