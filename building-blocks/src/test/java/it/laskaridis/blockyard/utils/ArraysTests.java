package it.laskaridis.blockyard.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArraysTests {

    @Test
    public void shouldMergeTwoArraysTogether() {
        byte[] a = "test".getBytes();
        byte[] b = "string".getBytes();
        byte[] c = Arrays.merge(a, b);
        assertEquals(a.length + b.length, c.length);
        assertEquals("teststring", new String(c));
    }
}
