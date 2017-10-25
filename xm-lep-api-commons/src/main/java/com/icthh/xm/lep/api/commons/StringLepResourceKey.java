package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.LepResourceKey;
import com.icthh.xm.lep.api.Version;

import java.util.Objects;

/**
 * The {@link StringLepResourceKey} class.
 */
public class StringLepResourceKey implements LepResourceKey {

    private final String resourceKeyId;
    private final Version version;

    public StringLepResourceKey(String resourceKeyId) {
        this(resourceKeyId, null);
    }

    public StringLepResourceKey(String resourceKeyId, Version version) {
        this.resourceKeyId = Objects.requireNonNull(resourceKeyId, "resourceKeyId can't be null");
        this.version = version;
    }

    @Override
    public String getId() {
        return resourceKeyId;
    }

    @Override
    public Version getVersion() {
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

        if (obj instanceof StringLepResourceKey) {
            StringLepResourceKey other = StringLepResourceKey.class.cast(obj);
            return Objects.equals(this.getId(), other.getId()) &&
                Objects.equals(this.getVersion(), other.getVersion());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVersion());
    }

}
