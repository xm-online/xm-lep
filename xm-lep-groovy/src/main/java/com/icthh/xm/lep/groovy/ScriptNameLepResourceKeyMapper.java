package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.commons.UrlLepResourceKey;

/**
 * The {@link ScriptNameLepResourceKeyMapper} interface.
 */
public interface ScriptNameLepResourceKeyMapper {

    /**
     * Maps LEP resource key to script name.
     *
     * @param resourceKey the LEP resource key
     * @return script name
     */
    String map(UrlLepResourceKey resourceKey);

    /**
     * Maps script name to LEP resource key.
     *
     * @param scriptName the script name
     * @return URL LEP resource key
     */
    UrlLepResourceKey map(String scriptName);

}
