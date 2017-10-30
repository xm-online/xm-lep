package com.icthh.xm.lep.script;

import java.io.IOException;
import java.io.InputStream;

/**
 * The {@link NonRequiredCloseLazyInputStream} class.
 */
public abstract class NonRequiredCloseLazyInputStream extends InputStream {

    private InputStream delegate;

    private InputStream getDelegate() throws IOException {
        if (delegate == null) {
            delegate = buildDelegate();
        }
        return delegate;
    }

    abstract InputStream buildDelegate() throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return getDelegate().read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] buffer) throws IOException {
        return getDelegate().read(buffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] buffer, int off, int len) throws IOException {
        return getDelegate().read(buffer, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long count) throws IOException {
        return getDelegate().skip(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
        return getDelegate().available();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported() {
        try {
            return getDelegate().markSupported();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mark(int readAheadLimit) {
        try {
            getDelegate().mark(readAheadLimit);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() throws IOException {
        getDelegate().reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // nop
    }

}
