package com.icthh.xm.lep.api;

/**
 * The {@link LepResource} interface.
 */
public interface LepResource {

    /**
     * Composite class constant.
     */
    Class<CompositeLepResource> COMPOSITE_CLASS = CompositeLepResource.class;

    LepResourceDescriptor getDescriptor();

    <V> V getValue(Class<? extends V> valueClass);

    default boolean isComposite() {
        return COMPOSITE_CLASS.isInstance(this);
    }

    /**
     * Check is composite implementation and if so cast or throw exception otherwise.
     *
     * @return composite class representation of resource
     */
    default CompositeLepResource asComposite() {
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
