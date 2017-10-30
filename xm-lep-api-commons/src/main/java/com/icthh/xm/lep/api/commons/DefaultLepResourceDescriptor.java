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

    /**
     * Lep resource descriptor constructor.
     *
     * @param type             resource type
     * @param subType          sub type
     * @param key              resource key
     * @param creationTime     resource creation time
     * @param modificationTime resource modification time
     */
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

    /**
     * Lep resource descriptor constructor.
     *
     * @param type             resource type
     * @param key              resource key
     * @param creationTime     resource creation time
     * @param modificationTime resource modification time
     */
    public DefaultLepResourceDescriptor(LepResourceType type,
                                        LepResourceKey key,
                                        Instant creationTime,
                                        Instant modificationTime) {
        this(type, null, key, creationTime, modificationTime);
    }

    /**
     * Lep resource descriptor constructor.
     *
     * @param type         resource type
     * @param key          resource key
     * @param creationTime resource creation time
     */
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
        return this.getClass().getSimpleName()
            + " [type: " + Objects.toString(type)
            + ", subType: " + Objects.toString(subType)
            + ", key: " + Objects.toString(key)
            + ", creationTime: " + Objects.toString(creationTime)
            + ", modificationTime: " + Objects.toString(modificationTime) + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepResourceType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepResourceType getSubType() {
        return subType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepResourceKey getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getModificationTime() {
        return modificationTime;
    }

    /**
     * {@inheritDoc}
     */
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
