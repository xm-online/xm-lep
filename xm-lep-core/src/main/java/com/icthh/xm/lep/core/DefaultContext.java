package com.icthh.xm.lep.core;

import com.icthh.xm.lep.api.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The {@link DefaultContext} class.
 */
public class DefaultContext implements Context {

    private final Map<String, Object> values = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getNames() {
        return Collections.unmodifiableSet(values.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String name) {
        return values.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(String name) {
        return values.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getValue(String name, Class<T> castToType) {
        return castToType.cast(getValue(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String name, Object value) {
        values.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getValues() {
        return Collections.unmodifiableMap(values);
    }

}
