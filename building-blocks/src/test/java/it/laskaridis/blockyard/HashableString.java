package it.laskaridis.blockyard;

import it.laskaridis.blockyard.digests.Digest;
import it.laskaridis.blockyard.digests.Digestable;

import java.util.Objects;

import static it.laskaridis.blockyard.digests.Digests.HASH256;

final class HashableString implements Digestable {

    private final String str;

    public static final HashableString of(String str) {
        return new HashableString(str);
    }

    public HashableString(String str) {
        this.str = str;
    }

    @Override
    public Digest getDigest() {
        return HASH256.digest(this.str.getBytes());
    }

    @Override
    public String toString() {
        return this.str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashableString that = (HashableString) o;
        return str.equals(that.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }
}
