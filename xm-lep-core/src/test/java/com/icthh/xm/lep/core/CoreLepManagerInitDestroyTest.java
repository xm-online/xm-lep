package com.icthh.xm.lep.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

import com.icthh.xm.lep.api.ExtensionService;
import com.icthh.xm.lep.api.LepExecutor;
import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepKey;
import com.icthh.xm.lep.api.LepManagerServiceAware;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.LepResourceService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * The {@link CoreLepManagerInitDestroyTest} class.
 */
public class CoreLepManagerInitDestroyTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void processLepShouldThrowExceptionWithoutInit() throws LepInvocationCauseException {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("CoreLepManager not initialized (current state: CREATED)");

        CoreLepManager manager = new CoreLepManager();
        manager.processLep(null, null, null, null);
    }

    @Test
    public void processLepShouldThrowExceptionOnDestroyedManager() throws LepInvocationCauseException {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("CoreLepManager not initialized (current state: DESTROYED)");

        ExtensionService es = mock(ExtensionService.class);
        LepResourceService rs = mock(LepResourceService.class);
        LepExecutor executor = mock(LepExecutor.class);

        CoreLepManager manager = new CoreLepManager();
        manager.init(es, rs, executor);
        manager.destroy();
        manager.processLep(null, null, null, null);
    }

    @Test
    public void onInitNullExtensionServiceShouldThrowNPE() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("extensionService can't be null");

        CoreLepManager manager = new CoreLepManager();
        manager.init(null,
                     mock(LepResourceService.class),
                     mock(LepExecutor.class));
    }

    @Test
    public void onInitNullResourceServiceShouldThrowNPE() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("resourceService can't be null");

        CoreLepManager manager = new CoreLepManager();
        manager.init(mock(ExtensionService.class),
                     null,
                     mock(LepExecutor.class));
    }

    @Test
    public void onInitNullExecutorShouldThrowNPE() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("executor can't be null");

        CoreLepManager manager = new CoreLepManager();
        manager.init(mock(ExtensionService.class),
                     mock(LepResourceService.class),
                     null);
    }

    @Test
    public void initDestroyInitShouldSuccessProcessLep() throws LepInvocationCauseException {
        ExtensionService es = mock(ExtensionService.class);
        LepResourceService rs = mock(LepResourceService.class);
        LepExecutor executor = mock(LepExecutor.class);
        when(executor.execute(any(), any(), any(), any())).thenReturn("executor result");

        CoreLepManager manager = new CoreLepManager();
        manager.init(es, rs, executor);
        manager.destroy();
        manager.init(es, rs, executor);
        try {
            Object resultValue = manager.processLep(mock(LepKey.class),
                                                    null,
                                                    null,
                                                    mock(LepMethod.class));
            assertEquals("executor result", resultValue);
        } finally {
            manager.destroy();
        }
    }

    @Test
    public void managerReturnsExecutorResultObject() throws LepInvocationCauseException {
        ExtensionService es = mock(ExtensionService.class);
        LepResourceService rs = mock(LepResourceService.class);
        LepExecutor executor = mock(LepExecutor.class);
        when(executor.execute(any(), any(), any(), any())).thenReturn("executor result");

        CoreLepManager manager = new CoreLepManager();
        manager.init(es, rs, executor);
        try {
            Object resultValue = manager.processLep(mock(LepKey.class),
                                                    null,
                                                    null,
                                                    mock(LepMethod.class));
            assertEquals("executor result", resultValue);
        } finally {
            manager.destroy();
        }
    }

    @Test
    public void initShouldInjectManagerAndDestroyCallDestroyInServices() {
        ExtensionServiceManagerServiceAware es = mock(ExtensionServiceManagerServiceAware.class);
        LepResourceServiceManagerServiceAware rs = mock(LepResourceServiceManagerServiceAware.class);
        LepExecutor executor = mock(LepExecutor.class);

        CoreLepManager manager = new CoreLepManager();
        manager.init(es, rs, executor);
        verify(es, times(1)).init(refEq(manager));
        verify(rs, times(1)).init(refEq(manager));

        manager.destroy();
        verify(es, times(1)).destroy();
        verify(rs, times(1)).destroy();
    }

    private interface ExtensionServiceManagerServiceAware extends ExtensionService, LepManagerServiceAware {

    }

    private interface LepResourceServiceManagerServiceAware extends LepResourceService, LepManagerServiceAware {

    }

}
