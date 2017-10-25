package com.icthh.xm.lep.groovy;

import com.icthh.xm.lep.api.ContextsHolder;
import com.icthh.xm.lep.api.LepResource;
import com.icthh.xm.lep.api.LepResourceDescriptor;
import com.icthh.xm.lep.api.LepResourceService;
import com.icthh.xm.lep.api.commons.UrlLepResourceKey;
import com.icthh.xm.lep.script.ScriptLepResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Objects;

/**
 * The {@link LepResourceKeyURLConnection} class implements lazy data loading for {@link ScriptLepResource}.
 */
public class LepResourceKeyURLConnection extends URLConnection {

    private final UrlLepResourceKey resourceKey;

    private final LepResourceService resourceService;

    private final ContextsHolder contextsHolder;

    /**
     * Constructs a URL connection to the specified URL.
     */
    public LepResourceKeyURLConnection(UrlLepResourceKey resourceKey,
                                       LepResourceService resourceService,
                                       ContextsHolder contextsHolder) {
        super(Objects.requireNonNull(resourceKey, "resourceKey can't be null").getUrl());
        this.resourceKey = Objects.requireNonNull(resourceKey, "resourceKey can't be null");
        this.resourceService = Objects.requireNonNull(resourceService,
                                                      "resourceService can't be null");
        this.contextsHolder = Objects.requireNonNull(contextsHolder, "contextsHolder can't be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws IOException {
        throw new IOException("Cannot connect to " + this.url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return getResource().getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentEncoding() {
        return getResource().getEncoding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastModified() {
        return getResourceDescriptor().getModificationTime().toEpochMilli();
    }

    private LepResourceDescriptor resourceDescriptor;

    private LepResourceDescriptor getResourceDescriptor() {
        if (resourceDescriptor == null) {
            resourceDescriptor = resourceService.getResourceDescriptor(contextsHolder, resourceKey);
            if (resourceDescriptor == null) {
                throw new IllegalStateException("Can't find LEP resource descriptor for resource key: "
                                                    + resourceKey);
            }
        }
        return resourceDescriptor;
    }

    private ScriptLepResource resource;

    private ScriptLepResource getResource() {
        if (resource == null) {
            LepResource lepResource = resourceService.getResource(contextsHolder, resourceKey);
            if (lepResource == null) {
                throw new IllegalStateException("Can't find LEP resource by key: "
                                                    + resourceKey.getId());
            }

            if (!(lepResource instanceof ScriptLepResource)) {
                throw new IllegalStateException("LEP resource by key: "
                                                    + resourceKey + " expect to be "
                                                    + ScriptLepResource.class.getSimpleName()
                                                    + " but actually is "
                                                    + lepResource.getClass().getSimpleName());
            }

            resource = ScriptLepResource.class.cast(lepResource);
        }
        return onGetResource(resource);
    }

    protected ScriptLepResource onGetResource(ScriptLepResource resource) {
        // return same resource as default
        return resource;
    }

}
