package com.icthh.xm.lep.api;

/**
 * The {@link ObjectAware} interface.
 */
public interface ObjectAware<T> {

    void init(T awareObject);

    void destroy();

}
