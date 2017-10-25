package com.icthh.xm.lep.api.commons;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.icthh.xm.lep.api.LepResourceKey;
import com.icthh.xm.lep.api.Version;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The {@link UrlLepResourceKey} class.
 */
public class UrlLepResourceKey implements LepResourceKey {

    public static final String LEP_PROTOCOL = "lep";
    public static final int DEFAULT_PORT = -1;

    public static final String PARAM_VERSION = "version";

    private static final String URL_PARAM_VALUE_SPLITTER = "=";
    private static final String URL_PARAMS_SPLITTER = "&";

    private final URL url;
    private final Version version;

    public UrlLepResourceKey(URL url) {
        this.url = Objects.requireNonNull(url, "url can't be null");

        Map<String, List<String>> params = splitQuery(this.url);
        this.version = getVersionValue(params);
    }

    private static Version getVersionValue(Map<String, List<String>> params) {
        List<String> versionValues = params.get(PARAM_VERSION);
        if (versionValues == null || versionValues.isEmpty()) {
            return null;
        } else {
            if (versionValues.size() > 1) {
                throw new IllegalArgumentException("More than one value for 'version' URL param: " + versionValues);
            }
            String strVersion = versionValues.iterator().next();
            return new DefaultVersion(strVersion);
        }
    }

    private static Map<String, List<String>> splitQuery(URL url) {
        String query = url.getQuery();
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyMap();
        }

        return Arrays.stream(query.split(URL_PARAMS_SPLITTER))
            .map(UrlLepResourceKey::splitQueryParameter)
            .collect(Collectors.groupingBy(SimpleImmutableEntry::getKey,
                                           LinkedHashMap::new,
                                           mapping(Map.Entry::getValue, toList())));
    }

    private static SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf(URL_PARAM_VALUE_SPLITTER);
        final String key = (idx > 0) ? it.substring(0, idx) : it;
        final String value = (idx > 0 && it.length() > idx + 1) ? it.substring(idx + 1) : null;
        return new SimpleImmutableEntry<>(key, value);
    }

    public UrlLepResourceKey(String urlSpec) {
        this(urlSpec, null);
    }

    public UrlLepResourceKey(String urlSpec,
                             URLStreamHandler urlStreamHandler) {
        this(buildUrl(urlSpec, urlStreamHandler));
    }

    public UrlLepResourceKey(String protocol,
                             String host,
                             String resource,
                             URLStreamHandler urlStreamHandler) {
        this(protocol, host, DEFAULT_PORT, resource, urlStreamHandler);
    }

    public UrlLepResourceKey(String protocol,
                             String host,
                             int port,
                             String resource,
                             URLStreamHandler urlStreamHandler) {
        this(buildUrl(protocol, host, port, resource, urlStreamHandler));
    }

    public static UrlLepResourceKey valueOfUrlResourcePath(String path) {
        return valueOfUrlResourcePath(path, null);
    }

    public static UrlLepResourceKey valueOfUrlResourcePath(String path,
                                                           URLStreamHandler urlStreamHandler) {
        return new UrlLepResourceKey(LEP_PROTOCOL + ":" + path, urlStreamHandler);
    }

    private static URLStreamHandler buildDefaultStreamHandler() {
        return new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                return buildDefaultURLConnection(url);
            }
        };
    }

    private static URLConnection buildDefaultURLConnection(URL url) {
        return new URLConnection(url) {
            @Override
            public void connect() throws IOException {
                throw new IOException("Cannot connect to " + this.url);
            }
        };
    }

    @Override
    public String getId() {
        return getUrl().toString();
    }

    @Override
    public Version getVersion() {
        return version;
    }

    public URL getUrl() {
        return url;
    }

    private static URL buildUrl(String urlSpec,
                                URLStreamHandler handler) {
        try {
            if (handler == null && urlSpec.startsWith(LEP_PROTOCOL + ":")) {
                handler = buildDefaultStreamHandler();
            }

            return new URL(null, urlSpec, handler);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Can't build URL by LEP resource spec: "
                                                   + urlSpec
                                                   + " because of error: "
                                                   + e.getMessage(), e);
        }
    }

    private static URL buildUrl(String protocol,
                                String host,
                                int port,
                                String resourceKeyId,
                                URLStreamHandler handler) {
        handler = getUrlStreamHandler(protocol, handler);

        try {
            return new URL(protocol, host, port, resourceKeyId, handler);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Error while creating LEP key: " + e.getMessage(), e);
        }
    }

    private static URLStreamHandler getUrlStreamHandler(String protocol, URLStreamHandler handler) {
        if (handler == null && LEP_PROTOCOL.equals(protocol)) {
            handler = buildDefaultStreamHandler();
        }
        return handler;
    }

    public String getUrlResourcePath() {
        return getUrl().getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof UrlLepResourceKey) {
            UrlLepResourceKey other = UrlLepResourceKey.class.cast(obj);
            return Objects.equals(this.getId(), other.getId());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
