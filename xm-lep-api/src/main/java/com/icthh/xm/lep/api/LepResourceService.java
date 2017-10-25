package com.icthh.xm.lep.api;

import java.util.List;

/**
 * The {@link LepResourceService} interface.
 */
public interface LepResourceService {

    default boolean isResourceExists(ContextsHolder contextsHolder, LepResourceKey resourceKey) {
        return getResourceDescriptor(contextsHolder, resourceKey) != null;
    }

    LepResourceDescriptor getResourceDescriptor(ContextsHolder contextsHolder, LepResourceKey resourceKey);

    LepResource getResource(ContextsHolder contextsHolder, LepResourceKey resourceKey);

    /**
     * Save extension resource.
     * save = create or update.
     *
     * @param contextsHolder LEP contexts holder
     * @param extensionKey   LEP extension key
     * @param resource       LEP resource instance
     * @return return extension resource
     */
    LepResource saveResource(ContextsHolder contextsHolder, LepKey extensionKey, LepResource resource);

    List<Version> getResourceVersions(ContextsHolder contextsHolder, LepResourceKey resourceKey);

}
