package com.icthh.xm.lep.api;

import java.lang.reflect.Method;

/**
 * Represents the signature at a LEP method.
 * This interface parallels {@code java.lang.reflect.Member}.
 * {@code java.lang.reflect.Method} may don't have parameters name if code compiled without adding
 * this info in bytecode, so {@link MethodSignature} interface used.
 */
public interface MethodSignature {

    /**
     * Returns the identifier part of this signature.  For methods this
     * will return the method name.
     *
     * @see java.lang.reflect.Member#getName
     */
    String getName();

    /**
     * Returns the modifiers on this signature represented as an int.  Use
     * the constants and helper methods defined on
     * <code>java.lang.reflect.Modifier</code> to manipulate this, i.e.
     * <pre>
     *     // check if this signature is public
     *     java.lang.reflect.Modifier.isPublic(sig.getModifiers());
     *
     *     // print out the modifiers
     *     java.lang.reflect.Modifier.toString(sig.getModifiers());
     * </pre>
     *
     * @see java.lang.reflect.Member#getModifiers
     * @see java.lang.reflect.Modifier
     */
    int getModifiers();

    /**
     * <p>Returns a <code>java.lang.Class</code> object representing the class,
     * interface, or aspect (proxy) that declared this member.  For intra-member
     * declarations, this will be the type on which the member is declared,
     * not the type where the declaration is lexically written.  Use
     * <code>SourceLocation.getWithinType()</code> to get the type in
     * which the declaration occurs lexically.</p>
     * <p>For consistency with <code>java.lang.reflect.Member</code>, this
     * method named <code>getDeclaringClass()</code>.</p>
     *
     * @see java.lang.reflect.Member#getDeclaringClass
     */
    Class<?> getDeclaringClass();

    /**
     * Returns the fully-qualified name of the declaring type. This is
     * equivalent to calling getDeclaringClass().getName(), but caches
     * the result for greater efficiency.
     */
    String getDeclaringClassName();

    /**
     * Returns an array of {@code Class} objects that represent the formal
     * parameter types, in declaration order, of the method. Returns an array of length
     * 0 if the underlying method takes no parameters.
     *
     * @return the parameter types for the method
     */
    Class<?>[] getParameterTypes();

    /**
     * Returns an array of {@code String} objects that represent parameter names,
     * in declaration order, of the method. Returns an array of length
     * 0 if the underlying method takes no parameters.
     *
     * @return the parameter names for the method
     */
    String[] getParameterNames();

    /**
     * Returns an array of {@code Class} objects that represent the
     * types of exceptions declared to be thrown by the underlying
     * method.  Returns an array of length 0 if the method declares no exceptions in its {@code
     * throws} clause.
     *
     * @return the exception types declared as being thrown by the method
     */
    Class<?>[] getExceptionTypes();

    /**
     * Returns a {@code Class} object that represents the formal return type
     * of the method.
     *
     * @return the return type for the method this object represents
     */
    Class<?> getReturnType();

    /**
     * Returns {@code Method} object reflecting the declared method.
     *
     * @return the {@code Method} object reflecting the declared method.
     */
    Method getMethod();

}
