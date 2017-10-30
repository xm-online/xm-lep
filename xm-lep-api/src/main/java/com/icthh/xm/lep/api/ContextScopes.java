package com.icthh.xm.lep.api;

/**
 * The {@link ContextScopes} class with system scope names.
 */
public final class ContextScopes {

    public static final String THREAD = "lep.system.thread";

    public static final String EXECUTION = "lep.system.execution";

    /**
     * Private utils class constructor, to prevent instantiation with reflection API.
     */
    private ContextScopes() {
        throw new IllegalAccessError("not permitted access for utils class");
    }

}
