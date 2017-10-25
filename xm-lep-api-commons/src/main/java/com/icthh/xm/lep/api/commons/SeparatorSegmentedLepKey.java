package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.LepKey;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The {@link SeparatorSegmentedLepKey} class.
 */
public class SeparatorSegmentedLepKey implements LepKey {

    /**
     * Default segmented key separator regex.
     */
    public static final String DEFAULT_SEPARATOR = "\\.";

    /**
     * Default segmented key group mode is has no group at all.
     */
    public static final GroupMode DEFAULT_GROUP_MODE = GroupMode.none();

    /**
     * Key identification value.
     */
    private final String id;

    /**
     * Key parts (segments).
     */
    private final String[] segments;

    /**
     * Segments separator.
     */
    private final String separator;

    /**
     * Segments separator regexp.
     */
    private final String separatorRegExp;

    /**
     * Key group extraction mode.
     */
    private final GroupMode groupMode;

    /**
     * Group key for current Lep key.
     */
    private final SeparatorSegmentedLepKey groupKey;

    public SeparatorSegmentedLepKey(String separator, String[] segments, GroupMode groupMode) {
        Objects.requireNonNull(separator, "separator can't be null");
        Objects.requireNonNull(segments, "segments can't be null");
        if (segments.length < 1) {
            throw new IllegalArgumentException("Must be at least one segment");
        }

        this.groupMode = Objects.requireNonNull(groupMode, "groupMode can't be null");
        this.separator = separator;
        this.separatorRegExp = Pattern.quote(this.separator);
        this.segments = Arrays.copyOf(segments, segments.length);

        KeyData keyData = build(separator, segments, groupMode);
        this.groupKey = keyData.groupKey;
        this.id = keyData.id;
    }

    private static class KeyData {
        SeparatorSegmentedLepKey groupKey;
        String id;

        KeyData(SeparatorSegmentedLepKey groupKey, String id) {
            this.groupKey = groupKey;
            this.id = id;
        }
    }

    private static KeyData build(String separator, String[] segments, GroupMode groupMode) {
        // stop group mode recursion
        if (groupMode.isNone()) {
            return new KeyData(null, concatStrArray(separator, segments));
        }

        if (segments.length < groupMode.getSegmentsCount()) {
            throw new IllegalArgumentException("LEP key segments length (" + segments.length
                                                   + ") less then group mode segments count ("
                                                   + groupMode.getSegmentsCount() + ")");
        }

        SeparatorSegmentedLepKey groupKey = buildGroupKey(separator, segments, groupMode);
        String id = buildId(separator, segments, groupMode, groupMode.getType());

        return new KeyData(groupKey, id);
    }

    private static SeparatorSegmentedLepKey buildGroupKey(String separator,
                                                          String[] segments,
                                                          GroupMode groupMode) {
        GroupModeType groupModeType = groupMode.getType();

        // build group key
        int groupFrom;
        int groupTo;
        switch (groupModeType) {
            case PREFIX:
                groupFrom = 0;
                groupTo = groupMode.getSegmentsCount();
                break;

            case SUFFIX:
                groupFrom = segments.length - groupMode.getSegmentsCount();
                groupTo = segments.length;
                break;

            case PREFIX_EXCLUDE_LAST_SEGMENTS:
                groupFrom = 0;
                groupTo = segments.length - groupMode.getSegmentsCount();
                break;

            default:
                throw new IllegalArgumentException("Unknown group mode type: " + groupModeType);
        }

        String[] groupSegments = Arrays.copyOfRange(segments, groupFrom, groupTo);
        return new SeparatorSegmentedLepKey(separator,
                                            groupSegments,
                                            groupMode.getParent());
    }

    private static String buildId(String separator, String[] segments, GroupMode groupMode, GroupModeType groupModeType) {
        // build ID
        int idFrom;
        int idTo;
        switch (groupMode.getKeyIdGroupMode()) {
            case EXCLUDE_GROUP:
                switch (groupModeType) {
                    case PREFIX:
                        // id = suffix (group = prefix)
                        idFrom = groupMode.getSegmentsCount();
                        idTo = segments.length;
                        break;

                    case SUFFIX:
                        // id = prefix (group = suffix)
                        idFrom = 0;
                        idTo = segments.length - groupMode.getSegmentsCount();
                        break;

                    case PREFIX_EXCLUDE_LAST_SEGMENTS:
                        idFrom = segments.length - groupMode.getSegmentsCount();
                        idTo = segments.length;
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown key id group mode type: " + groupModeType);
                }
                break;

            case INCLUDE_GROUP:
                // full length (include all segments in ID)
                idFrom = 0;
                idTo = segments.length;
                break;

            default:
                throw new IllegalArgumentException("Unknown group mode type: " + groupMode.getKeyIdGroupMode());
        }

        String[] idSegments = Arrays.copyOfRange(segments, idFrom, idTo);
        return concatStrArray(separator, idSegments);
    }

    public SeparatorSegmentedLepKey(String segments, String separator, GroupMode groupMode) {
        this(Objects.requireNonNull(separator, "separator can't be null"),
             Objects.requireNonNull(segments, "segments can't be null").split(Pattern.quote(separator)),
             groupMode);
    }

    public SeparatorSegmentedLepKey(String segments, String separator) {
        this(segments, separator, DEFAULT_GROUP_MODE);
    }

    public SeparatorSegmentedLepKey(String segments) {
        this(segments, DEFAULT_SEPARATOR);
    }

    public SeparatorSegmentedLepKey(String segments, GroupMode groupMode) {
        this(segments, DEFAULT_SEPARATOR, groupMode);
    }

    public SeparatorSegmentedLepKey(String separator, String[] segments) {
        this(separator, segments, DEFAULT_GROUP_MODE);
    }

    public SeparatorSegmentedLepKey(String separator, List<String> segments, GroupMode groupMode) {
        this(separator,
             segments.toArray(new String[
                                  Objects.requireNonNull(segments, "segments can't be null").size()]),
             groupMode);
    }

    public SeparatorSegmentedLepKey(String separator, List<String> segments) {
        this(separator, segments, DEFAULT_GROUP_MODE);
    }

    public SeparatorSegmentedLepKey(String[] segments, GroupMode groupMode) {
        this(DEFAULT_SEPARATOR, segments, groupMode);
    }

    public SeparatorSegmentedLepKey(String[] segments) {
        this(DEFAULT_SEPARATOR, segments, DEFAULT_GROUP_MODE);
    }

    public SeparatorSegmentedLepKey(List<String> segments, GroupMode groupMode) {
        this(DEFAULT_SEPARATOR, segments, groupMode);
    }

    public SeparatorSegmentedLepKey(List<String> segments) {
        this(DEFAULT_SEPARATOR, segments, DEFAULT_GROUP_MODE);
    }

    private static String concatStrArray(String separator, String[] segments) {
        StringBuilder keyBuilder = new StringBuilder();
        for (String segment : segments) {
            if (keyBuilder.length() > 0) {
                keyBuilder.append(separator);
            }
            keyBuilder.append(segment);
        }
        return keyBuilder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepKey getGroupKey() {
        return groupKey;
    }

    public String[] getSegments() {
        return Arrays.copyOf(segments, segments.length);
    }

    public String getSegment(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index can't be negative (value: " + index + ")");
        }

        return segments[index];
    }

    public int getSegmentsSize() {
        return segments.length;
    }

    public int getGroupSegmentsSize() {
        return (groupKey == null) ? 0 : groupKey.getSegmentsSize();
    }

    public String[] getGroupSegments() {
        return (groupKey == null) ? new String[0] : groupKey.getSegments();
    }

    public String getSeparator() {
        return separator;
    }

    public GroupMode getGroupMode() {
        return groupMode;
    }

    public SeparatorSegmentedLepKey append(String suffix) {
        return append(suffix, this.groupMode);
    }

    public SeparatorSegmentedLepKey append(String[] suffixSegments) {
        String[] segments = arraysConcat(this.segments, suffixSegments);
        return new SeparatorSegmentedLepKey(this.separator, segments, groupMode);
    }

    public SeparatorSegmentedLepKey append(String suffix, GroupMode groupMode) {
        String[] suffixSegments = suffix.split(this.separatorRegExp);
        return append(suffixSegments, groupMode);
    }

    public SeparatorSegmentedLepKey append(String[] suffixSegments, GroupMode groupMode) {
        String[] segments = arraysConcat(this.segments, suffixSegments);
        return new SeparatorSegmentedLepKey(this.separator, segments, groupMode);
    }

    public SeparatorSegmentedLepKey prepend(String prefix) {
        String[] prefixSegments = prefix.split(this.separatorRegExp);
        return prepend(prefixSegments);
    }

    public SeparatorSegmentedLepKey prepend(String[] prefixSegments) {
        String[] segments = arraysConcat(prefixSegments, this.segments);
        return new SeparatorSegmentedLepKey(this.separator, segments, groupMode);
    }

    public SeparatorSegmentedLepKey prepend(String prefix, GroupMode groupMode) {
        String[] prefixSegments = prefix.split(this.separatorRegExp);
        return prepend(prefixSegments, groupMode);
    }

    public SeparatorSegmentedLepKey prepend(String[] prefixSegments, GroupMode groupMode) {
        String[] segments = arraysConcat(prefixSegments, this.segments);
        return new SeparatorSegmentedLepKey(this.separator, segments, groupMode);
    }

    private static String[] arraysConcat(String[] firstArray, String[] secondArray) {
        int resultLength = firstArray.length + secondArray.length;
        String[] resultArray = Arrays.copyOf(firstArray, resultLength);
        System.arraycopy(secondArray, 0, resultArray, firstArray.length, secondArray.length);

        return resultArray;
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

        if (obj instanceof SeparatorSegmentedLepKey) {
            SeparatorSegmentedLepKey other = SeparatorSegmentedLepKey.class.cast(obj);
            return Objects.equals(this.getId(), other.getId())
                && Objects.equals(this.getGroupKey(), other.getGroupKey());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroupKey());
    }

}
