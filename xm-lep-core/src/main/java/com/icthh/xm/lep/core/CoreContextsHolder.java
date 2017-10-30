package com.icthh.xm.lep.core;

import com.icthh.xm.lep.api.ContextScopes;
import com.icthh.xm.lep.api.ContextsHolder;
import com.icthh.xm.lep.api.ScopedContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The {@link CoreContextsHolder} class.
 */
public class CoreContextsHolder implements ContextsHolder {

    private static volatile Map<String, ThreadLocal<ScopedContext>> contexts = new HashMap<>();

    static {
        contexts.put(ContextScopes.THREAD, new ThreadLocal<>());
        contexts.put(ContextScopes.EXECUTION, new ThreadLocal<>());
    }

    private static ThreadLocal<ScopedContext> getThreadLocalContext(String scope) {
        return contexts.get(scope);
    }

    @Override
    public ScopedContext getContext(String scope) {
        Objects.requireNonNull(scope, "scope can't be null");

        ThreadLocal<ScopedContext> threadLocalContext = getThreadLocalContext(scope);
        if (threadLocalContext == null) {
            throw new IllegalArgumentException("Unsupported context scope name: " + scope);
        }

        return threadLocalContext.get();
    }

    private ScopedContext beginContext(String scope) {
        ThreadLocal<ScopedContext> threadLocalContext = getThreadLocalContext(scope);
        threadLocalContext.set(new DefaultScopedContext(scope));
        return threadLocalContext.get();
    }

    private void endContext(String scope) {
        ThreadLocal<ScopedContext> threadLocalContext = getThreadLocalContext(scope);
        threadLocalContext.remove();
    }

    ScopedContext beginThreadContext() {
        return beginContext(ContextScopes.THREAD);
    }

    void endThreadContext() {
        endContext(ContextScopes.THREAD);
    }

    void beginExecutionContext() {
        beginContext(ContextScopes.EXECUTION);
    }

    void endExecutionContext() {
        endContext(ContextScopes.EXECUTION);
    }

}
