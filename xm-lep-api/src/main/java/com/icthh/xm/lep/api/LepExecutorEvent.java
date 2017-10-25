package com.icthh.xm.lep.api;

import java.util.EventObject;
import java.util.Optional;

/**
 * The {@link LepExecutorEvent} class.
 */
public abstract class LepExecutorEvent extends EventObject {

    private static final long serialVersionUID = 5944631406761316707L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the LEP executor on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public LepExecutorEvent(LepExecutor source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepExecutor getSource() {
        return LepExecutor.class.cast(source);
    }

    public static abstract class BaseLepExecutorEvent extends LepExecutorEvent {

        private static final long serialVersionUID = 8937662902122107331L;

        private final transient LepResourceKey key;
        private final transient LepMethod method;

        public BaseLepExecutorEvent(LepExecutor source,
                                    LepResourceKey key,
                                    LepMethod method) {
            super(source);
            this.key = key;
            this.method = method;
        }

        public final LepResourceKey getKey() {
            return key;
        }

        public final LepMethod getMethod() {
            return method;
        }

    }

    public static class BeforeResourceExecutionEvent extends BaseLepExecutorEvent {

        private static final long serialVersionUID = -5585146833308461025L;

        public BeforeResourceExecutionEvent(LepExecutor source,
                                            LepResourceKey key,
                                            LepMethod method) {
            super(source, key, method);
        }

    }

    public static class AfterResourceExecutionEvent extends BaseLepExecutorEvent {

        private static final long serialVersionUID = -840579017287546847L;

        private final transient ResultObject result;

        public AfterResourceExecutionEvent(LepExecutor source,
                                           LepResourceKey key,
                                           LepMethod method,
                                           ResultObject result) {
            super(source, key, method);

            this.result = result;
        }

        /**
         * Returns result value.
         *
         * @return result value
         */
        public Optional<ResultObject> getResult() {
            return Optional.ofNullable(result);
        }

        // avoid findbug warning on transient
        private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
            throw new java.io.NotSerializableException(getClass().getName());
        }

        // avoid findbug warning on transient
        private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException,
            ClassNotFoundException {
            throw new java.io.NotSerializableException(getClass().getName());
        }

    }

    public static class ResultObject {

        private final boolean present;
        private final Object value;
        private final Exception exception;

        public ResultObject(Exception exception) {
            this.present = false;
            this.value = null;
            this.exception = exception;
        }

        public ResultObject(Object value) {
            this.present = true;
            this.value = value;
            this.exception = null;
        }

        public Object getValue() {
            return value;
        }

        public boolean isValue() {
            return present;
        }

        public boolean isException() {
            return !isValue();
        }

        /**
         * Gets method result exception.
         *
         * @return method result exception
         */
        public Optional<Exception> getException() {
            return Optional.ofNullable(exception);
        }
    }


}
