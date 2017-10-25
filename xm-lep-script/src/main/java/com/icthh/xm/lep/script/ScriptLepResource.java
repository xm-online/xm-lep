package com.icthh.xm.lep.script;

import com.icthh.xm.lep.api.LepResource;
import com.icthh.xm.lep.api.LepResourceDescriptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The {@link ScriptLepResource} class.
 */
public class ScriptLepResource implements LepResource, InputStreamSupplier {

    public static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

    private static final int SCRIPT_PREVIEW_MAX_LENGTH = 360;
    private static final int SCRIPT_PREVIEW_LINES_COUNT = 5;

    private LepResourceDescriptor descriptor;
    private String encoding;
    private String scriptText;
    private InputStreamSupplier textStreamSupplier;

    public ScriptLepResource(LepResourceDescriptor descriptor,
                             String encoding,
                             String scriptText) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor can't be null");
        this.scriptText = scriptText;
        setEncoding(encoding);
    }

    public ScriptLepResource(LepResourceDescriptor descriptor,
                             String encoding,
                             InputStreamSupplier textStreamSupplier) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor can't be null");
        this.textStreamSupplier = Objects.requireNonNull(textStreamSupplier, "textStreamSupplier can't be null");
        setEncoding(encoding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(this.getClass().getSimpleName());
        buffer.append(" [");
        buffer.append("descriptor: ").append(Objects.toString(descriptor)).append(", ");
        buffer.append("encoding: ").append(Objects.toString(encoding)).append(", ");
        buffer.append("scriptText (preview): ").append(getPreview());
        buffer.append("]");
        return buffer.toString();
    }

    private String getPreview() {
        try {
            return (textStreamSupplier == null)
                ? getPreview(scriptText)
                : getPreview(textStreamSupplier.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Error while getting LEP resource input stream: " + e.getMessage(), e);
        }
    }

    private static String getPreview(String scriptText) {
        return StringUtils.getPreview(scriptText, SCRIPT_PREVIEW_LINES_COUNT,
                                      true, SCRIPT_PREVIEW_MAX_LENGTH);
    }

    private String getPreview(InputStream is) {
        return StringUtils.getPreview(is, encoding, SCRIPT_PREVIEW_LINES_COUNT,
                                      true, SCRIPT_PREVIEW_MAX_LENGTH);
    }

    @Override
    public LepResourceDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public <V> V getValue(Class<? extends V> valueClass) {
        if (String.class.equals(valueClass)) {
            return valueClass.cast(getScriptText());
        }

        if (InputStream.class.equals(valueClass)) {
            try {
                return valueClass.cast(getInputStream());
            } catch (IOException e) {
                throw new IllegalStateException("Error while getting LEP resource input stream: " + e.getMessage(), e);
            }
        }

        throw new IllegalArgumentException(getClass().getSimpleName() + " - can't cast value to type "
                                               + valueClass.getCanonicalName()
                                               + ", only String.class and InputStream.class are supported");
    }

    public String getEncoding() {
        return encoding;
    }

    public String getScriptText() {
        if (textStreamSupplier == null) {
            return scriptText;
        } else {
            try {
                return read(getInputStream());
            } catch (IOException e) {
                throw new IllegalStateException("Error while reading LEP resource input stream: " + e.getMessage(), e);
            }
        }
    }

    private String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input, getEncoding()))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public byte[] getScriptBytes() throws IOException {
        Charset encoding = Charset.forName(getEncoding());

        if (textStreamSupplier == null) {
            return getScriptText().getBytes(encoding);
        } else {
            InputStream inputStream = textStreamSupplier.getInputStream();
            try {
                //BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, encoding));
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                return buffer.toByteArray();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // nop
                }
            }
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (textStreamSupplier == null) {
            return new NonRequiredCloseLazyInputStream() {

                @Override
                InputStream buildDelegate() throws IOException {
                    return new ByteArrayInputStream(getScriptBytes());
                }

            };
        } else {
            return textStreamSupplier.getInputStream();
        }
    }

    private void setEncoding(String encoding) {
        Objects.requireNonNull(encoding, "encoding can't be null");
        this.encoding = encoding;
    }

}
