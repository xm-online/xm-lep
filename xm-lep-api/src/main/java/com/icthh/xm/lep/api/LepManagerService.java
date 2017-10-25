package com.icthh.xm.lep.api;

/**
 * The {@link LepManagerService} interface.
 */
public interface LepManagerService extends ContextsHolder {

    ExtensionService getExtensionService();

    LepResourceService getResourceService();

}
