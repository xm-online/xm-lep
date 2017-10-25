package com.icthh.xm.lep.api;

import java.util.List;
import java.util.Set;

/**
 * The {@link ExtensionService} interface.
 */
public interface ExtensionService {

    /**
     * Return resource key for specified extension key.
     * If extension has no resource or extension with specified key doesn't exist the method
     * returns {@code null}.
     *
     * @param extensionKey             the extension key
     * @param extensionResourceVersion extension resource version or {@code null} if not used
     * @return resource key or {@code null} if not found
     */
    LepResourceKey getResourceKey(LepKey extensionKey, Version extensionResourceVersion);

    /**
     * Returns all extensions and its resource descriptors.
     *
     * @return all extensions and its resource descriptors
     */
    List<ExtensionResourceDescriptor> getExtensionResourceDescriptors();

    /**
     * Returns extension and its resource descriptor.
     *
     * @param extensionKey the extension key
     * @return extension and its resource descriptor
     */
    ExtensionResourceDescriptor getExtensionResourceDescriptor(LepKey extensionKey);

    /**
     * Return extension by key.
     *
     * @param key LEP key
     * @return extension instance
     */
    Extension getExtension(LepKey key);

    /**
     * Returns all extensions.
     *
     * @return all extensions
     */
    List<Extension> getExtensions();

    /**
     * Returns extensions grouped by group key.
     *
     * @param groupKeys group filter by key, can be {@code null} or empty
     * @return extensions grouped by group key
     */
    List<ExtensionGroup> getExtensionGroups(Set<LepKey> groupKeys);

    /**
     * Deletes extension by key.
     *
     * @param key LEP key
     */
    void deleteExtension(LepKey key);

}
