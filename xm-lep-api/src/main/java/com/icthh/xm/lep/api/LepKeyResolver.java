package com.icthh.xm.lep.api;

/**
 * Interface for build dynamic LEP keys.
 */
public interface LepKeyResolver {

    /**
     * Resolve dynamic part of LEP key by method arguments.
     *
     * @param baseKey        base LEP key (prefix), can be {@code null}
     * @param method         method data on what LEP call occurs
     * @param managerService LEP manager service
     * @return complete LEP key (baseKey + dynamic part)
     */
    LepKey resolve(LepKey baseKey,
                   LepMethod method,
                   LepManagerService managerService);

}
