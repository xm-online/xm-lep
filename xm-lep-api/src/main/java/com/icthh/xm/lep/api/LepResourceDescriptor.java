package com.icthh.xm.lep.api;

import java.time.Instant;

/**
 * The {@link LepResourceDescriptor} interface.
 */
public interface LepResourceDescriptor {

    /**
     * Composite class constant.
     */
    Class<CompositeLepResourceDescriptor> COMPOSITE_CLASS = CompositeLepResourceDescriptor.class;

    LepResourceType getType();

    LepResourceType getSubType();

    LepResourceKey getKey();

    Instant getCreationTime();

    Instant getModificationTime();

    Version getVersion();

    default boolean isComposite() {
        return COMPOSITE_CLASS.isInstance(this);
    }

    /**
     * Check is composite implementation and if so cast or throw exception otherwise.
     *
     * @return composite class representation of resource descriptor
     */
    default CompositeLepResourceDescriptor asComposite() {
        if (isComposite()) {
            return COMPOSITE_CLASS.cast(this);
        } else {
            throw new IllegalStateException("Can't cast not composite: "
                                                + this.getClass().getSimpleName()
                                                + " object to composite: "
                                                + COMPOSITE_CLASS.getSimpleName());
        }
    }

}
