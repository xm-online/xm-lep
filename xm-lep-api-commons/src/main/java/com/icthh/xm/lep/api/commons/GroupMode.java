package com.icthh.xm.lep.api.commons;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

/**
 * The {@link GroupMode} class.
 */
public class GroupMode {

    private final GroupModeType type;

    private final KeyIdGroupMode keyIdGroupMode;

    private final int segmentsCount;

    private final GroupMode parent;

    private static final GroupMode NONE = new GroupMode();

    private GroupMode() {
        this.type = null;
        this.keyIdGroupMode = null;
        this.segmentsCount = 0;
        this.parent = null;
    }

    private GroupMode(GroupModeType type, KeyIdGroupMode keyIdGroupMode, int segmentsCount) {
        this(type, keyIdGroupMode, segmentsCount, NONE);
    }

    private GroupMode(GroupModeType type, KeyIdGroupMode keyIdGroupMode, int segmentsCount, GroupMode parent) {
        if (segmentsCount <= 0) {
            throw new IllegalArgumentException("segmentsCount must be grater than zero");
        }
        this.type = Objects.requireNonNull(type);
        this.keyIdGroupMode = Objects.requireNonNull(keyIdGroupMode);
        this.segmentsCount = segmentsCount;
        this.parent = parent;
    }

    public GroupModeType getType() {
        return type;
    }

    public int getSegmentsCount() {
        return segmentsCount;
    }

    public GroupMode getParent() {
        return parent;
    }

    public boolean isNone() {
        return this == NONE;
    }

    public KeyIdGroupMode getKeyIdGroupMode() {
        return keyIdGroupMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, keyIdGroupMode, segmentsCount, parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof GroupMode) {
            GroupMode other = GroupMode.class.cast(obj);
            return Objects.equals(this.getType(), other.getType())
                && Objects.equals(this.getKeyIdGroupMode(), other.getKeyIdGroupMode())
                && Objects.equals(this.getSegmentsCount(), other.getSegmentsCount())
                && Objects.equals(this.getParent(), other.getParent());
        }

        return false;
    }

    // BUILDERS ////////

    public static GroupMode none() {
        return NONE;
    }

    public static GroupMode build(GroupModeType type, KeyIdGroupMode idMode, int segmentsCount) {
        return new GroupMode(type, idMode, segmentsCount);
    }

    public static class Builder {

        public GroupMode none() {
            return GroupMode.none();
        }

        public BuilderParent mode(GroupModeType type, KeyIdGroupMode keyIdGroupMode, int segmentsCount) {
            return new BuilderParent(type, keyIdGroupMode, segmentsCount);
        }

        public BuilderParent prefix(KeyIdGroupMode keyIdGroupMode, int segmentsCount) {
            return mode(GroupModeType.PREFIX, keyIdGroupMode, segmentsCount);
        }

        public BuilderParent prefixAndIdIncludeGroup(int segmentsCount) {
            return mode(GroupModeType.PREFIX, KeyIdGroupMode.INCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent prefixExcludeLastSegmentsAndIdIncludeGroup(int segmentsCount) {
            return mode(GroupModeType.PREFIX_EXCLUDE_LAST_SEGMENTS, KeyIdGroupMode.INCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent prefixAndIdExcludeGroup(int segmentsCount) {
            return mode(GroupModeType.PREFIX, KeyIdGroupMode.EXCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent prefixExcludeLastSegmentsAndIdExcludeGroup(int segmentsCount) {
            return mode(GroupModeType.PREFIX_EXCLUDE_LAST_SEGMENTS, KeyIdGroupMode.EXCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent suffix(KeyIdGroupMode idMode, int segmentsCount) {
            return mode(GroupModeType.SUFFIX, idMode, segmentsCount);
        }

        public BuilderParent suffixAndIdIncludeGroup(int segmentsCount) {
            return mode(GroupModeType.SUFFIX, KeyIdGroupMode.INCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent suffixAndIdExcludeGroup(int segmentsCount) {
            return mode(GroupModeType.SUFFIX, KeyIdGroupMode.EXCLUDE_GROUP, segmentsCount);
        }

    }

    public static class BuilderParent {

        private final Deque<BuilderParameters> parameters = new LinkedList<>();

        private BuilderParent(GroupModeType type, KeyIdGroupMode keyIdGroupMode, int segmentsCount) {
            parameters.push(new BuilderParameters(type, keyIdGroupMode, segmentsCount));
        }

        public BuilderParent parentMode(GroupModeType type, KeyIdGroupMode idMode, int segmentsCount) {
            parameters.push(new BuilderParameters(type, idMode, segmentsCount));
            return this;
        }

        public BuilderParent parentPrefix(KeyIdGroupMode idMode, int segmentsCount) {
            return parentMode(GroupModeType.PREFIX, idMode, segmentsCount);
        }

        public BuilderParent parentPrefixAndIdIncludeGroup(int segmentsCount) {
            return parentMode(GroupModeType.PREFIX, KeyIdGroupMode.INCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent parentPrefixAndIdExcludeGroup(int segmentsCount) {
            return parentMode(GroupModeType.PREFIX, KeyIdGroupMode.EXCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent parentSuffix(KeyIdGroupMode idMode, int segmentsCount) {
            return parentMode(GroupModeType.SUFFIX, idMode, segmentsCount);
        }

        public BuilderParent parentSuffixAndIdIncludeGroup(int segmentsCount) {
            return parentMode(GroupModeType.SUFFIX, KeyIdGroupMode.INCLUDE_GROUP, segmentsCount);
        }

        public BuilderParent parentSuffixAndIdExcludeGroup(int segmentsCount) {
            return parentMode(GroupModeType.SUFFIX, KeyIdGroupMode.EXCLUDE_GROUP, segmentsCount);
        }

        public GroupMode build() {
            GroupMode current = null;
            while (!parameters.isEmpty()) {
                BuilderParameters params = parameters.pop();

                if (current == null) {
                    current = new GroupMode(params.getType(), params.getIdMode(), params.getSegmentsCount());
                } else {
                    current = new GroupMode(params.getType(), params.getIdMode(), params.getSegmentsCount(), current);
                }
            }

            return current;
        }

    }

    private static class BuilderParameters {

        private final GroupModeType type;
        private final KeyIdGroupMode idMode;
        private final int segmentsCount;

        private BuilderParameters(GroupModeType type, KeyIdGroupMode idMode, int segmentsCount) {
            this.type = type;
            this.idMode = idMode;
            this.segmentsCount = segmentsCount;
        }

        GroupModeType getType() {
            return type;
        }

        int getSegmentsCount() {
            return segmentsCount;
        }

        KeyIdGroupMode getIdMode() {
            return idMode;
        }

    }

}
