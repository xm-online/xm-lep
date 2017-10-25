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

    public DefaultLepMethod(Object target,
                            MethodSignature methodSignature,
                            Object[] methodArgValues) {
        this.target = target;
        this.methodSignature = Objects.requireNonNull(methodSignature, "methodSignature can't be null");
        this.methodArgValues = (methodArgValues == null) ? null
            : Arrays.copyOf(methodArgValues, methodArgValues.length);
    }

    public DefaultLepMethod(Object target,
                            MethodSignature methodSignature) {
        this(target, methodSignature, null);
    }


    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    @Override
    public Object[] getMethodArgValues() {
        if (methodArgValues == null) {
            return EMPTY_OBJ_ARRAY;
        }
        return Arrays.copyOf(methodArgValues, methodArgValues.length);
    }

}
