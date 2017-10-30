package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.LepManagerService;
import groovy.util.GroovyScriptEngine;

/**
 * The {@link GroovyScriptEngineProviderStrategy} interface.
 */
public interface GroovyScriptEngineProviderStrategy {

    GroovyScriptEngine getEngine(LepManagerService managerService);

}
