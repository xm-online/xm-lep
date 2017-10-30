package com.icthh.xm.lep.api.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * The {@link UrlLepResourceKeyTest} class.
 */
public class UrlLepResourceKeyTest {

    @Test
    public void validUrlResourceKeyWithVersion() {
        UrlLepResourceKey key = UrlLepResourceKey.valueOfUrlResourcePath("/hello/path?version=1.2.3");
        assertEquals("lep:/hello/path?version=1.2.3", key.getId());
        assertEquals("/hello/path", key.getUrlResourcePath());
        assertEquals("1.2.3", key.getVersion().getId());
    }

}
