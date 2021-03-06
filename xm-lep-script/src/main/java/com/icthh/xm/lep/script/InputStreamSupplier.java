package com.icthh.xm.lep.script;

import java.io.IOException;
import java.io.InputStream;

/**
 * The {@link InputStreamSupplier} interface.
 */
public interface InputStreamSupplier {

    /**
     * Return an {@link InputStream} for the content of an underlying resource.
     * <br>It is expected that each call creates a <i>fresh</i> stream.
     * <br>This requirement is particularly important when you consider an API such
     * as JavaMail, which needs to be able to read the stream multiple times when
     * creating mail attachments. For such a use case, it is <i>required</i>
     * that each {@code getInputStream()} call returns a fresh stream.
     *
     * @return the input stream for the underlying resource (must not be {@code null})
     * @throws java.io.FileNotFoundException if the underlying resource doesn't exist
     * @throws IOException                   if the content stream could not be opened
     */
    InputStream getInputStream() throws IOException;

}
