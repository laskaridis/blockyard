package it.laskaridis.blockyard.digests;

public interface DigestAlgorithm {

    Digest digest(byte[] preimage);

    default Digest digest(Digest d1, Digest d2) {
        throw new UnsupportedOperationException();
    }
}
