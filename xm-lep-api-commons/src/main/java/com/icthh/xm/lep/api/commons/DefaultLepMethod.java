package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.MethodSignature;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@link DefaultLepMethod} class.
 */
public class DefaultLepMethod implements LepMethod {

    private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

    private final Object target;
    private final MethodSignature methodSignature;
    private final Object[] methodArgValues;

    /**
     * LEP method constructor with method arguments.
     *
     * @param target          method target
     * @param methodSignature method signature
     * @param methodArgValues method argument values
     */
    public DefaultLepMethod(Object target,
                            MethodSignature methodSignature,
                            Object[] methodArgValues) {
        this.target = target;
        this.methodSignature = Objects.requireNonNull(methodSignature, "methodSignature can't be null");
        this.methodArgValues = (methodArgValues == null) ? EMPTY_OBJ_ARRAY
            : Arrays.copyOf(methodArgValues, methodArgValues.length);
    }

    /**
     * LEP method constructor without methods arguments.
     *
     * @param target          method target
     * @param methodSignature method signature
     */
    public DefaultLepMethod(Object target,
                            MethodSignature methodSignature) {
        this(target, methodSignature, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getTarget() {
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getMethodArgValues() {
        return Arrays.copyOf(methodArgValues, methodArgValues.length);
    }

}
