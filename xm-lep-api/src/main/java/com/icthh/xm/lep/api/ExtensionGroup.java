package com.icthh.xm.lep.api;

import java.util.List;

/**
 * The {@link ExtensionGroup} interface.
 */
public interface ExtensionGroup {

    LepKey getGroupKey();

    List<Extension> getExtensions();

}
