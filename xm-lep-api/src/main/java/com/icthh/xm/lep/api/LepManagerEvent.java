package com.icthh.xm.lep.api;

import java.util.EventObject;

/**
 * The {@link LepManagerEvent} class.
 */
public abstract class LepManagerEvent extends EventObject {

    private static final long serialVersionUID = 4133799496566067343L;

    /**
     * Constructs a prototypical LepManagerEvent.
     *
     * @param source the LEP manager on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public LepManagerEvent(LepManager source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LepManager getSource() {
        return LepManager.class.cast(source);
    }

    public static class LepManagerInitedEvent extends LepManagerEvent {

        private static final long serialVersionUID = 4909644386291338918L;

        /**
         * Constructs a LEP manager inited event.
         *
         * @param source the LEP manager on which the Event initially occurred
         * @throws IllegalArgumentException if source is null
         */
        public LepManagerInitedEvent(LepManager source) {
            super(source);
        }

    }

    public static class LepManagerDestroyedEvent extends LepManagerEvent {

        private static final long serialVersionUID = 1169447366656949803L;

        /**
         * Constructs a LEP manager destroyed event.
         *
         * @param source the LEP manager on which the Event initially occurred
         * @throws IllegalArgumentException if source is null
         */
        public LepManagerDestroyedEvent(LepManager source) {
            super(source);
        }

    }

}
