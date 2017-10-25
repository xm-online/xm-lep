package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.LepResourceType;

import java.util.Objects;

/**
 * The {@link DefaultLepResourceType} class.
 */
public class DefaultLepResourceType implements LepResourceType {

    private final String name;

    public DefaultLepResourceType(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name can't be blank");
        }
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof DefaultLepResourceType) {
            DefaultLepResourceType other = DefaultLepResourceType.class.cast(obj);
            return Objects.equals(this.getName(), other.getName());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

}
