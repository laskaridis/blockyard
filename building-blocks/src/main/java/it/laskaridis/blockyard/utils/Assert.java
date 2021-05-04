package it.laskaridis.blockyard.utils;

import java.util.Collection;

public class Assert {

    public static void notNull(Object value, String msg) {
        if (value == null)
            throw new IllegalArgumentException(msg);
    }

    public static void notEmpty(Collection elements, String msg) {
        if (elements.isEmpty())
            throw new IllegalArgumentException(msg);
    }
}
