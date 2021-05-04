package it.laskaridis.blockyard.digests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static it.laskaridis.blockyard.digests.Digests.HASH160;
import static it.laskaridis.blockyard.digests.Digests.HASH256;
import static it.laskaridis.blockyard.utils.Arrays.merge;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DigestUtilsTests {

    private MessageDigest sha256;
    private MessageDigest ripemd160;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        this.sha256 = MessageDigest.getInstance("SHA-256");
        // this.ripemd160 = MessageDigest.getInstance("RIPEMD-160");
    }

    @Test
    public void shouldCreateCorrectHash256Image() {
        byte[] preimage = "this is a test string".getBytes();
        byte[] expectedImage = sha256.digest(sha256.digest(preimage));
        byte[] actualImage = HASH256.digest(preimage).getValueAsByteArray();
        assertArrayEquals(actualImage, expectedImage);
    }

    @Test
    public void shouldCreateCorrectHash256OfTwoDigests() {
        Digest d1 = HASH256.digest("test".getBytes());
        Digest d2 = HASH256.digest("string".getBytes());
        Digest expected = HASH256.digest(merge(d1.getValueAsByteArray(), d2.getValueAsByteArray()));
        Digest actual = HASH256.digest(d1, d2);
        assertEquals(expected, actual);
    }

    @Test @Ignore("not implemented yet")
    public void shouldCreateCorrectHash160Image() {
        byte[] preimage = "this is a test string".getBytes();
        byte[] expectedImage = ripemd160.digest(sha256.digest(preimage));
        byte[] actualImage = HASH160.digest(preimage).getValueAsByteArray();
        assertArrayEquals(actualImage, expectedImage);
    }
}
