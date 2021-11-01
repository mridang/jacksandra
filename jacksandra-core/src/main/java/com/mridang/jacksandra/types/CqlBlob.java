package com.mridang.jacksandra.types;

import java.io.Serializable;
import java.util.Arrays;

import com.datastax.oss.protocol.internal.util.Bytes;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class CqlBlob implements Serializable {

    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final byte[] bytes;

    public CqlBlob(byte[] bytes) {
        this.bytes = bytes;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CqlBlob)) return false;

        CqlBlob cqlBlob = (CqlBlob) o;

        return Arrays.equals(bytes, cqlBlob.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return Bytes.toHexString(bytes);
    }
}
