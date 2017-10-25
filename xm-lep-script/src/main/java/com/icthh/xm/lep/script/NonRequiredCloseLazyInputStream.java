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

    @Override
    public int read() throws IOException {
        return getDelegate().read();
    }

    @Override
    public int read(byte b[]) throws IOException {
        return getDelegate().read(b);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return getDelegate().read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return getDelegate().skip(n);
    }

    @Override
    public int available() throws IOException {
        return getDelegate().available();
    }

    @Override
    public boolean markSupported() {
        try {
            return getDelegate().markSupported();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void mark(int readAheadLimit) {
        try {
            getDelegate().mark(readAheadLimit);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void reset() throws IOException {
        getDelegate().reset();
    }

    @Override
    public void close() {
        // nop
    }

}
