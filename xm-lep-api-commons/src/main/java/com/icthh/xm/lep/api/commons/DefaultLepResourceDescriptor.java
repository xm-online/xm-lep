package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.LepResourceDescriptor;
import com.icthh.xm.lep.api.LepResourceKey;
import com.icthh.xm.lep.api.LepResourceType;
import com.icthh.xm.lep.api.Version;

import java.time.Instant;
import java.util.Objects;

/**
 * The {@link DefaultLepResourceDescriptor} class.
 */
public class DefaultLepResourceDescriptor implements LepResourceDescriptor {

    private LepResourceType type;
    private LepResourceType subType;
    private LepResourceKey key;
    private Instant creationTime;
    private Instant modificationTime;

    public DefaultLepResourceDescriptor() {
        super();
    }

    public DefaultLepResourceDescriptor(LepResourceType type,
                                        LepResourceType subType,
                                        LepResourceKey key,
                                        Instant creationTime,
                                        Instant modificationTime) {
        this.type = Objects.requireNonNull(type);
        this.key = Objects.requireNonNull(key);
        this.subType = subType;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
    }

    public DefaultLepResourceDescriptor(LepResourceType type,
                                        LepResourceKey key,
                                        Instant creationTime,
                                        Instant modificationTime) {
        this(type, null, key, creationTime, modificationTime);
    }

    public DefaultLepResourceDescriptor(LepResourceType type,
                                        LepResourceKey key,
                                        Instant creationTime) {
        this(type, key, creationTime, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(this.getClass().getSimpleName());
        buffer.append(" [");
        buffer.append("type: ").append(Objects.toString(type)).append(", ");
        buffer.append("subType: ").append(Objects.toString(subType)).append(", ");
        buffer.append("key: ").append(Objects.toString(key)).append(", ");
        buffer.append("creationTime: ").append(Objects.toString(creationTime)).append(", ");
        buffer.append("modificationTime: ").append(Objects.toString(modificationTime));
        buffer.append("]");
        return buffer.toString();
    }

    @Override
    public LepResourceType getType() {
        return type;
    }

    @Override
    public LepResourceType getSubType() {
        return subType;
    }

    @Override
    public LepResourceKey getKey() {
        return key;
    }

    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    @Override
    public Instant getModificationTime() {
        return modificationTime;
    }

    @Override
    public Version getVersion() {
        return getKey().getVersion();
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public void setModificationTime(Instant modificationTime) {
        this.modificationTime = modificationTime;
    }

    public void setType(LepResourceType type) {
        this.type = Objects.requireNonNull(type);
    }

    public void setKey(LepResourceKey key) {
        this.key = Objects.requireNonNull(key);
    }

    public void setSubType(LepResourceType subType) {
        this.subType = subType;
    }

}
