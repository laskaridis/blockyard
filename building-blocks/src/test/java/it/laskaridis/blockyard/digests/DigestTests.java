package it.laskaridis.blockyard.digests;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DigestTests {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullImageValue() {
        new Digest(null, Digests.HASH256.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullDigestAlgorithm() {
        new Digest("test".getBytes(), null);
    }

    @Test
    public void shouldCreateCorrectHashCodeImage() {
        byte[] expectedDigestValue = "test".getBytes();
        String expectedDigestType = Digest.HASH256.toString();
        Digest image = new Digest(expectedDigestValue, expectedDigestType);
        assertArrayEquals(image.getValueAsByteArray(), expectedDigestValue);
        assertEquals(image.getType(), expectedDigestType);
    }
}