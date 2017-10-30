package com.icthh.xm.lep.api;

import java.util.List;

/**
 * The {@link CompositeLepResourceDescriptor} interface.
 */
public interface CompositeLepResourceDescriptor extends LepResourceDescriptor {

    List<LepResourceDescriptor> getChildren();

}
