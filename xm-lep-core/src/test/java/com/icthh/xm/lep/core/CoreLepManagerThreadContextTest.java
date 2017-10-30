package com.icthh.xm.lep.core;

import static org.junit.Assert.*;

import com.icthh.xm.lep.api.ContextScopes;
import com.icthh.xm.lep.api.ScopedContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * The {@link CoreLepManagerThreadContextTest} class.
 */
public class CoreLepManagerThreadContextTest {

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
    public void throwsNPEOnNullInitThreadContextAction() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("context init action can't be null");

        manager.beginThreadContext(null);
    }

    @Test
    public void getsNullIfThreadContextNotBegin() {
        assertNull(manager.getContext(ContextScopes.THREAD));
    }

    @Test
    public void getsContextIfThreadContextBegin() {
        manager.beginThreadContext(scopedContext -> {
        });
        assertNotNull(manager.getContext(ContextScopes.THREAD));
    }

    @Test
    public void getsSameThreadContextForSameThread() {
        manager.beginThreadContext(scopedContext -> {
            scopedContext.setValue("test", 123);
        });
        ScopedContext context1 = manager.getContext(ContextScopes.THREAD);
        ScopedContext context2 = manager.getContext(ContextScopes.THREAD);

        assertNotNull(context1);
        assertEquals(context1, context2);
        assertEquals(123, context1.getValue("test"));
        assertEquals(123, context2.getValue("test"));
    }

    @Test
    public void getsNewThreadContextOnEachBeginForSameThread() {
        manager.beginThreadContext(scopedContext -> {
        });
        ScopedContext context1 = manager.getContext(ContextScopes.THREAD);

        manager.beginThreadContext(scopedContext -> {
        });
        ScopedContext context2 = manager.getContext(ContextScopes.THREAD);

        manager.beginThreadContext(scopedContext -> {
        });
        ScopedContext context3 = manager.getContext(ContextScopes.THREAD);

        assertNotEquals(context1, context2);
        assertNotEquals(context2, context3);
        assertNotEquals(context1, context3);
    }

    @Test
    public void getsSettedValueAfterThreadContextBegin() {
        manager.beginThreadContext(scopedContext -> {
        });
        ScopedContext context = manager.getContext(ContextScopes.THREAD);

        context.setValue("greeting", "hello");
        assertEquals("hello", context.getValue("greeting"));
    }

    @Test
    public void resetValueAfterThreadContextBeginReturnsLast() {
        manager.beginThreadContext(scopedContext -> {
        });
        ScopedContext context = manager.getContext(ContextScopes.THREAD);

        context.setValue("greeting", "hello");
        context.setValue("greeting", "hi");
        assertEquals("hi", context.getValue("greeting"));
    }

    @Test
    public void returnsNullThreadContextAfterEndContextForSameThread() {
        manager.beginThreadContext(scopedContext -> {
        });
        ScopedContext context = manager.getContext(ContextScopes.THREAD);
        assertNotNull(context);

        manager.endThreadContext();
        context = manager.getContext(ContextScopes.THREAD);
        assertNull(context);
    }

    @Test
    public void returnsNullThreadContextAfterManagerDestroy() {
        manager.beginThreadContext(scopedContext -> {
        });
        manager.destroy();

        ScopedContext context = manager.getContext(ContextScopes.THREAD);
        assertNull(context);
    }

}
