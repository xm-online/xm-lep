package com.icthh.xm.lep.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.icthh.xm.lep.api.ExtensionService;
import com.icthh.xm.lep.api.LepExecutor;
import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepKey;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.LepProcessingEvent.AfterExecutionEvent;
import com.icthh.xm.lep.api.LepProcessingEvent.AfterProcessingEvent;
import com.icthh.xm.lep.api.LepProcessingEvent.BeforeExecutionEvent;
import com.icthh.xm.lep.api.LepProcessingEvent.BeforeProcessingEvent;
import com.icthh.xm.lep.api.LepProcessingListener;
import com.icthh.xm.lep.api.LepResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

/**
 * The {@link CoreLepManagerProcessingListenerTest} class.
 */
public class CoreLepManagerProcessingListenerTest {

    private CoreLepManager manager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void beforeEachTest() {
        manager = new CoreLepManager();
    }

    @After
    public void afterEachTest() {
        manager.destroy();
    }

    @Test
    public void returnFalseOnRemoveUnregisteredProcessingListener() {
        LepProcessingListener unregisteredListener = mock(LepProcessingListener.class);
        assertFalse(manager.unregisterProcessingListener(unregisteredListener));
    }

    @Test
    public void successProcessingListenerRegistrationUnregistration() {
        LepProcessingListener processingListener = mock(LepProcessingListener.class);
        assertTrue(manager.registerProcessingListener(processingListener));
        assertTrue(manager.unregisterProcessingListener(processingListener));
    }

    @Test
    public void returnTrueOnMultipleRegistrationForSameProcessingListener() {
        LepProcessingListener processingListener = mock(LepProcessingListener.class);
        assertTrue(manager.registerProcessingListener(processingListener));
        assertTrue(manager.registerProcessingListener(processingListener));
        assertTrue(manager.registerProcessingListener(processingListener));

        // avoid memory leak in tests
        manager.unregisterProcessingListener(processingListener);
    }

    @Test
    public void successEventsOrderForProcessingListener() throws LepInvocationCauseException {

        // init manager
        ExtensionService es = mock(ExtensionService.class);
        LepResourceService rs = mock(LepResourceService.class);
        LepExecutor executor = mock(LepExecutor.class);

        manager.init(es, rs, executor);

        // register processing listener
        LepProcessingListener processingListener = mock(LepProcessingListener.class);
        assertTrue(manager.registerProcessingListener(processingListener));

        manager.processLep(mock(LepKey.class),
                           null,
                           null,
                           mock(LepMethod.class));

        InOrder inOrder = inOrder(processingListener);

        inOrder.verify(processingListener, times(1))
            .accept(isA(BeforeProcessingEvent.class));
        inOrder.verify(processingListener, times(1))
            .accept(isA(BeforeExecutionEvent.class));
        inOrder.verify(processingListener, times(1))
            .accept(isA(AfterExecutionEvent.class));
        inOrder.verify(processingListener, times(1))
            .accept(isA(AfterProcessingEvent.class));
    }

    @Test
    public void onExceptionForProcessLepEventsOrderForProcessingListener() throws LepInvocationCauseException {
        // init manager
        ExtensionService es = mock(ExtensionService.class);
        LepResourceService rs = mock(LepResourceService.class);
        LepExecutor executor = mock(LepExecutor.class);
        when(executor.execute(any(), any(), any(), any())).thenThrow(new RuntimeException());

        manager.init(es, rs, executor);

        // register processing listener
        LepProcessingListener processingListener = mock(LepProcessingListener.class);
        assertTrue(manager.registerProcessingListener(processingListener));

        try {
            manager.processLep(mock(LepKey.class),
                               null,
                               null,
                               mock(LepMethod.class));
        } catch (RuntimeException e) {
            // ignore
        }

        InOrder inOrder = inOrder(processingListener);

        inOrder.verify(processingListener, times(1))
            .accept(isA(BeforeProcessingEvent.class));
        inOrder.verify(processingListener, times(1))
            .accept(isA(BeforeExecutionEvent.class));
        inOrder.verify(processingListener, times(1))
            .accept(isA(AfterProcessingEvent.class));
    }

}
