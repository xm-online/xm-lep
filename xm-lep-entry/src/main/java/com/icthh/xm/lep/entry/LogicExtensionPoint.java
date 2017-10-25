package com.icthh.xm.lep.entry;

import com.icthh.xm.lep.api.LepKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation type {@link LogicExtensionPoint} is used to indicate point in code that can
 * be extended by custom implementation.
 * Custom implementation represents by {@link com.icthh.xm.lep.api.LepResource}) interface and
 * can be script, byte code, etc.
 */
@Target( {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicExtensionPoint {

    /**
     * Default group name.
     */
    String DEFAULT_GROUP = "general";

    /**
     * Base LEP key, or complete LEP key if resolver not specified.
     *
     * @return logic extension point key
     */
    String key();

    /**
     * LEP group name, it's just marker for grouping {@link LogicExtensionPoint}s (can be used
     * like Java package name convention).
     *
     * @return LEP group name
     */
    String group() default DEFAULT_GROUP;

    /**
     * LEP key resolver implementation class to determine complete (dynamic) LEP key.
     *
     * @return LEP key resolver implementation class
     */
    Class<? extends LepKeyResolver> resolver() default LepKeyResolver.class;

}
