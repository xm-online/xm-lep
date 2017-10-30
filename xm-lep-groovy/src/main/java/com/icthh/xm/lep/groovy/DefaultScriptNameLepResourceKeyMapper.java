package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.commons.UrlLepResourceKey;

/**
 * The {@link DefaultScriptNameLepResourceKeyMapper} class.
 */
public class DefaultScriptNameLepResourceKeyMapper implements ScriptNameLepResourceKeyMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public String map(UrlLepResourceKey resourceKey) {
        return (resourceKey != null) ? resourceKey.getId() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UrlLepResourceKey map(String scriptName) {
        return (scriptName != null) ? new UrlLepResourceKey(scriptName) : null;
    }

}
