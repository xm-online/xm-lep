package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.LepManagerService;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.net.URLConnection;
import java.util.Objects;

/**
 * The {@link LepScriptResourceConnector} class.
 */
public class LepScriptResourceConnector implements ResourceConnector {

    private final LepManagerService managerService;

    private final ScriptNameLepResourceKeyMapper mapper;

    public LepScriptResourceConnector(LepManagerService managerService,
                                      ScriptNameLepResourceKeyMapper mapper) {
        this.managerService = Objects.requireNonNull(managerService);
        this.mapper = Objects.requireNonNull(mapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URLConnection getResourceConnection(String scriptName) throws ResourceException {
        try {
            return new LepResourceKeyURLConnection(mapper.map(scriptName),
                                                   managerService.getResourceService(),
                                                   managerService);
        } catch (Exception e) {
            // FIXME add logging (GroovyScriptEngine eats ResourceException's...) !!!
            throw new ResourceException("Error while building "
                                            + LepResourceKeyURLConnection.class.getSimpleName()
                                            + ": " + e.getMessage(), e);
        }
    }

}
