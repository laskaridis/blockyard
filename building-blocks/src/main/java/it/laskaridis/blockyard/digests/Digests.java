package it.laskaridis.blockyard.digests;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static it.laskaridis.blockyard.utils.Arrays.merge;

public enum Digests implements DigestAlgorithm {

    HASH256(new DigestAlgorithm() {
        @Override
        public Digest digest(byte[] preimage) {
            try {
                final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                return new Digest(sha256.digest(sha256.digest(preimage)), Digest.HASH256);
            } catch (NoSuchAlgorithmException e) {
                throw new UnsupportedOperationException(e.getMessage());
            }
        }

        @Override
        public Digest digest(Digest d1, Digest d2) {
            byte[] preimage = merge(
                    d1.getValueAsByteArray(),
                    d2.getValueAsByteArray()
            );
            return digest(preimage);
        }
    }),

    HASH160(new DigestAlgorithm() {
        @Override
        public Digest digest(byte[] preimage) {
            try {
                final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                final MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD-160");
                return new Digest(ripemd160.digest(sha256.digest(preimage)), Digest.HASH160);
            } catch (NoSuchAlgorithmException e) {
                throw new UnsupportedOperationException(e.getMessage());
            }
        }
    });

    private final DigestAlgorithm algorithm;

    Digests(DigestAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Digest digest(byte[] preimage) {
        return this.algorithm.digest(preimage);
    }

    @Override
    public Digest digest(Digest d1, Digest d2) {
        return this.algorithm.digest(d1, d2);
    }
}
