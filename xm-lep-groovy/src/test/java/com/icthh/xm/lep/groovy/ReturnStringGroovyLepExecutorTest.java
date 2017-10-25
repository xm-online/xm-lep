package com.icthh.xm.lep.groovy;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.icthh.xm.lep.api.ContextsHolder;
import com.icthh.xm.lep.api.ExtensionService;
import com.icthh.xm.lep.api.LepInvocationCauseException;
import com.icthh.xm.lep.api.LepKey;
import com.icthh.xm.lep.api.LepManagerService;
import com.icthh.xm.lep.api.LepMethod;
import com.icthh.xm.lep.api.LepResourceKey;
import com.icthh.xm.lep.api.LepResourceService;
import com.icthh.xm.lep.api.MethodSignature;
import com.icthh.xm.lep.api.commons.DefaultLepMethod;
import com.icthh.xm.lep.api.commons.DefaultLepResourceDescriptor;
import com.icthh.xm.lep.api.commons.DefaultMethodSignature;
import com.icthh.xm.lep.api.commons.GroupMode;
import com.icthh.xm.lep.api.commons.SeparatorSegmentedLepKey;
import com.icthh.xm.lep.api.commons.UrlLepResourceKey;
import com.icthh.xm.lep.script.ScriptLepResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * The {@link ReturnStringGroovyLepExecutorTest} class.
 */
public class ReturnStringGroovyLepExecutorTest {

    private GroovyLepExecutor executor;

    @Mock
    private ContextsHolder contextsHolder;

    @Before
    public void onInit() {
        MockitoAnnotations.initMocks(this);
        this.executor = new GroovyLepExecutor(new DefaultScriptNameLepResourceKeyMapper());
    }

    @Test
    public void successExecuteLepWithoutParametersAndReturnStrValue() throws LepInvocationCauseException {
        GroupMode groupMode = new GroupMode.Builder().prefixAndIdIncludeGroup(1).build();
        LepKey extensionLepKey = new SeparatorSegmentedLepKey("general.returnString", groupMode);
        LepResourceKey resourceKey = UrlLepResourceKey.valueOfUrlResourcePath("/commons/ReturnStringScript.groovy");

        ExtensionService extensionService = mock(ExtensionService.class);
        when(extensionService.getResourceKey(eq(extensionLepKey), isNull())).thenReturn(resourceKey);

        LepResourceService resourceService = mock(LepResourceService.class);
        when(resourceService.getResource(notNull(), eq(resourceKey)))
            .thenReturn(buildScript(resourceKey));
        when(resourceService.getResourceDescriptor(notNull(), eq(resourceKey)))
            .thenReturn(buildScriptDescriptor(resourceKey));

        LepManagerService managerService = mock(LepManagerService.class);
        when(managerService.getExtensionService()).thenReturn(extensionService);
        when(managerService.getResourceService()).thenReturn(resourceService);

        MethodSignature methodSignature = new DefaultMethodSignature();
        LepMethod method = new DefaultLepMethod(this, methodSignature);


        Object resultValue = executor.execute(extensionLepKey,
                                              null,
                                              method,
                                              managerService);
        assertEquals("Hello, Logic Extension Point!", resultValue);
    }

    private ScriptLepResource buildScript(LepResourceKey resourceKey) {
        UrlLepResourceKey urlKey = (UrlLepResourceKey) resourceKey;

        try {
            String urlPath = urlKey.getUrlResourcePath();
            Path path = Paths.get(getClass().getResource("/groovy/scripts" + urlPath).toURI());
            String scriptText = new String(Files.readAllBytes(path));

            DefaultLepResourceDescriptor descriptor = buildScriptDescriptor(urlKey);

            return new ScriptLepResource(descriptor, "UTF-8", scriptText);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private DefaultLepResourceDescriptor buildScriptDescriptor(LepResourceKey resourceKey) {
        UrlLepResourceKey urlKey = (UrlLepResourceKey) resourceKey;

        DefaultLepResourceDescriptor descriptor = new DefaultLepResourceDescriptor();
        descriptor.setKey(urlKey);
        descriptor.setCreationTime(new Date(0).toInstant());
        descriptor.setModificationTime(new Date(0).toInstant());
        return descriptor;
    }

}
