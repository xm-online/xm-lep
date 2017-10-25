package com.icthh.xm.lep.api;

import java.util.EventObject;

/**
 * The {@link LepProcessingEvent} class.
 */
public abstract class LepProcessingEvent extends EventObject {

    private static final long serialVersionUID = 7588582878649167270L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the LEP manager on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public LepProcessingEvent(LepManager source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepManager getSource() {
        return LepManager.class.cast(source);
    }

    public static abstract class BaseLepProcessingEvent extends LepProcessingEvent {

        private final transient LepKey key;
        private final transient LepMethod method;

        public BaseLepProcessingEvent(LepManager source,
                                      LepKey key,
                                      LepMethod method) {
            super(source);
            this.key = key;
            this.method = method;
        }

        public final LepKey getKey() {
            return key;
        }

        public final LepMethod getMethod() {
            return method;
        }

    }

    public static class BeforeProcessingEvent extends BaseLepProcessingEvent {

        private static final long serialVersionUID = 6302184998762436336L;

        public BeforeProcessingEvent(LepManager source,
                                     LepKey key,
                                     LepMethod method) {
            super(source, key, method);
        }

    }

    public static class BeforeExecutionEvent extends BaseLepProcessingEvent {

        private static final long serialVersionUID = -8080910211496219122L;

        public BeforeExecutionEvent(LepManager source,
                                    LepKey key,
                                    LepMethod method) {
            super(source, key, method);
        }

    }

    public static class AfterExecutionEvent extends BaseLepProcessingEvent {

        private static final long serialVersionUID = -7900329326131385686L;

        private final transient Object resultValue;

        public AfterExecutionEvent(LepManager source,
                                   LepKey key,
                                   LepMethod method,
                                   Object resultValue) {
            super(source, key, method);
            this.resultValue = resultValue;
        }

        public Object getResultValue() {
            return resultValue;
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

    public static class AfterProcessingEvent extends BaseLepProcessingEvent {

        private static final long serialVersionUID = -1615716251427743997L;

        private final Exception exception;
        private final transient Object resultValue;

        public AfterProcessingEvent(LepManager source,
                                    LepKey key,
                                    LepMethod method,
                                    Exception exception,
                                    Object resultValue) {
            super(source, key, method);
            this.resultValue = resultValue;
            this.exception = exception;
        }

        public Object getResultValue() {
            return resultValue;
        }

        public boolean isError() {
            return exception == null;
        }

        public Exception getException() {
            return exception;
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

}
