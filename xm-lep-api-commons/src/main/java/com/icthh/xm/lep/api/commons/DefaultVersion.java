package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.Version;

import java.util.Objects;

/**
 * The {@link DefaultVersion} class.
 */
public class DefaultVersion implements Version {

    private final String version;

    public DefaultVersion(String version) {
        this.version = Objects.requireNonNull(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof DefaultVersion) {
            DefaultVersion other = DefaultVersion.class.cast(obj);
            return Objects.equals(this.getId(), other.getId());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
