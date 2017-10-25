package com.icthh.xm.lep.script;

import com.icthh.xm.lep.api.CompositeLepResource;
import com.icthh.xm.lep.api.LepResource;
import com.icthh.xm.lep.api.LepResourceDescriptor;

import java.util.List;
import java.util.Objects;

/**
 * The {@link CompositeGroupLepResource} class.
 */
public class CompositeGroupLepResource implements CompositeLepResource {

    private LepResourceDescriptor descriptor;

    private List<LepResource> children;

    public CompositeGroupLepResource(LepResourceDescriptor descriptor,
                                     List<LepResource> children) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor can't be null");
        this.children = children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepResourceDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> V getValue(Class<? extends V> valueClass) {
        Objects.requireNonNull(valueClass, "valueClass can't be null");
        if (valueClass.equals(List.class)) {
            return valueClass.cast(getChildren());
        }

        throw new IllegalArgumentException(getClass().getSimpleName()
                                               + " supports value only of List type and can't cast value to "
                                               + valueClass.getCanonicalName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LepResource> getChildren() {
        return children;
    }

    public void setChildren(List<LepResource> children) {
        this.children = children;
    }

    public void setDescriptor(LepResourceDescriptor descriptor) {
        this.descriptor = descriptor;
    }

}
