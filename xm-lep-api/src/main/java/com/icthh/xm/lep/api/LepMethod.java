package com.icthh.xm.lep.api;

/**
 * The {@link LepMethod} interface.
 */
public interface LepMethod {

    Object getTarget();

    MethodSignature getMethodSignature();

    Object[] getMethodArgValues();

}
