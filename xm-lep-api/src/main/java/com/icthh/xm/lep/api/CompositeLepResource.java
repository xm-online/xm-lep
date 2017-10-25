package com.icthh.xm.lep.api;

import java.util.List;

/**
 * The {@link CompositeLepResource} interface.
 */
public interface CompositeLepResource extends LepResource {

    List<LepResource> getChildren();

}
