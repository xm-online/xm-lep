package com.icthh.xm.lep.api.commons;

import com.icthh.xm.lep.api.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * The {@link DefaultMethodSignature} class.
 */
public class DefaultMethodSignature implements MethodSignature {

    private static final Class<?>[] CLASSES_EMPTY_ARRAY = new Class<?>[0];

    private static final String[] STRINGS_EMPTY_ARRAY = new String[0];

    private String name;
    private int modifiers;
    private Class<?> declaringClass;
    private Class<?>[] parameterTypes;
    private String[] parameterNames;
    private Class<?>[] exceptionTypes;
    private Class<?> returnType;
    private Method method;
    private String declaringClassName;

    private static <T> T[] arraysCopyOf(T[] original, Supplier<T[]> nullArrayAction) {
        return (original == null) ? nullArrayAction.get() : Arrays.copyOf(original, original.length);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    @Override
    public String getDeclaringClassName() {
        return this.declaringClassName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return arraysCopyOf(parameterTypes, () -> CLASSES_EMPTY_ARRAY);
    }

    @Override
    public String[] getParameterNames() {
        return arraysCopyOf(parameterNames, () -> STRINGS_EMPTY_ARRAY);
    }

    @Override
    public Class<?>[] getExceptionTypes() {
        return arraysCopyOf(exceptionTypes, () -> CLASSES_EMPTY_ARRAY);
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public void setDeclaringClass(Class<?> declaringClass) {
        this.declaringClass = declaringClass;
        this.declaringClassName = (declaringClass != null) ? declaringClass.getName() : null;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = arraysCopyOf(parameterTypes, () -> null);
    }

    public void setParameterNames(String[] parameterNames) {
        this.parameterNames = arraysCopyOf(parameterNames, () -> null);
    }

    public void setExceptionTypes(Class<?>[] exceptionTypes) {
        this.exceptionTypes = arraysCopyOf(exceptionTypes, () -> null);
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
