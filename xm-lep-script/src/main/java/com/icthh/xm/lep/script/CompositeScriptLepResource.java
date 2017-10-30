package com.icthh.xm.lep.script;

import com.icthh.xm.lep.api.CompositeLepResource;
import com.icthh.xm.lep.api.LepResource;
import com.icthh.xm.lep.api.LepResourceDescriptor;

import java.util.Collections;
import java.util.List;

/**
 * The {@link CompositeScriptLepResource} class.
 */
public class CompositeScriptLepResource extends ScriptLepResource implements CompositeLepResource {

    private List<LepResource> children;

    public CompositeScriptLepResource(LepResourceDescriptor descriptor,
                                      String encoding,
                                      String scriptText,
                                      List<LepResource> children) {
        super(descriptor, scriptText, encoding);
        this.children = children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LepResource> getChildren() {
        return (children == null) ? Collections.emptyList() : children;
    }

    public void setChildren(List<LepResource> children) {
        this.children = children;
    }

}
