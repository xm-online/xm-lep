package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepManagerService;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.commons.UrlLepResourceKey;
import groovy.lang.Binding;

/**
 * The {@link GroovyScriptRunner} interface.
 */
public interface GroovyScriptRunner {

    ScriptNameLepResourceKeyMapper getResourceKeyMapper();

    Object runScript(UrlLepResourceKey lepResourceKey,
                     LepMethod lepMethod,
                     LepManagerService managerService,
                     String scriptName,
                     Binding binding) throws LepInvocationCauseException;

}
