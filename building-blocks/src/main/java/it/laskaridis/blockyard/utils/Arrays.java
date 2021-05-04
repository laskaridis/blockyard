package it.laskaridis.blockyard.utils;

public final class Arrays {

    private Arrays() { }

    @SuppressWarnings("unchekced")
    public static byte[] merge(byte[] a, byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
