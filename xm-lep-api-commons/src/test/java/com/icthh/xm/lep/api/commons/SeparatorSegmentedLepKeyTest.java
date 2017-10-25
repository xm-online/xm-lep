package com.icthh.xm.lep.api.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * The {@link SeparatorSegmentedLepKeyTest} class.
 */
public class SeparatorSegmentedLepKeyTest {

    @Test
    public void prefixGroupExclude1SegmentTest() {
        GroupMode groupMode = new GroupMode.Builder().prefixAndIdExcludeGroup(1).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey("_", new String[] {
            "general",
            "dynamic.execute",
            "open-car"
        }, groupMode);
        assertEquals("general", extensionKey.getGroupKey().getId());
        assertEquals("dynamic.execute_open-car", extensionKey.getId());
    }

    @Test
    public void prefixGroupExclude2SegmentsTest() {
        GroupMode groupMode = new GroupMode.Builder().prefixAndIdExcludeGroup(2).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey("_", new String[] {
            "a",
            "b",
            "c"
        }, groupMode);
        assertEquals("a_b", extensionKey.getGroupKey().getId());
        assertEquals("c", extensionKey.getId());
    }

    @Test
    public void suffixGroupExclude1SegmentTest() {
        GroupMode groupMode = new GroupMode.Builder().suffixAndIdExcludeGroup(1).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey("_", new String[] {
            "a",
            "b",
            "c"
        }, groupMode);
        assertEquals("a_b", extensionKey.getId());
        assertEquals("c", extensionKey.getGroupKey().getId());
    }

    @Test
    public void suffixGroupExclude2SegmentsTest() {
        GroupMode groupMode = new GroupMode.Builder().suffixAndIdExcludeGroup(2).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey("_", new String[] {
            "a",
            "b",
            "c"
        }, groupMode);
        assertEquals("a", extensionKey.getId());
        assertEquals("b_c", extensionKey.getGroupKey().getId());
    }

    @Test
    public void prefixGroupExcludeLastSegmentsIncludeGroup() {
        GroupMode groupMode = new GroupMode.Builder().prefixExcludeLastSegmentsAndIdIncludeGroup(1).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey(".", new String[] {
            "com",
            "icthh",
            "xm",
            "lep",
            "createPayment"
        }, groupMode);
        assertEquals("com.icthh.xm.lep", extensionKey.getGroupKey().getId());
        assertEquals("com.icthh.xm.lep.createPayment", extensionKey.getId());
    }

    @Test
    public void prefixGroupExcludeLastSegments2IncludeGroup() {
        GroupMode groupMode = new GroupMode.Builder().prefixExcludeLastSegmentsAndIdIncludeGroup(2).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey(".", new String[] {
            "com",
            "icthh",
            "xm",
            "lep",
            "createPayment"
        }, groupMode);
        assertEquals("com.icthh.xm", extensionKey.getGroupKey().getId());
        assertEquals("com.icthh.xm.lep.createPayment", extensionKey.getId());
    }

    @Test
    public void successAppendSuffixWithoutSeparator() {
        GroupMode groupMode = new GroupMode.Builder().prefixAndIdIncludeGroup(4).build();
        SeparatorSegmentedLepKey extensionKey = new SeparatorSegmentedLepKey("com.icthh.xm.lep.SomeScript",
                                                                             ".",
                                                                             groupMode);
        SeparatorSegmentedLepKey finalKey = extensionKey.append("default");

        assertEquals("com.icthh.xm.lep", finalKey.getGroupKey().getId());
        assertEquals("com.icthh.xm.lep.SomeScript.default", finalKey.getId());
    }

}
