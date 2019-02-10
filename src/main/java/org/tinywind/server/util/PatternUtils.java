package org.tinywind.server.util;

import java.util.regex.Pattern;

/**
 * @author tinywind
 * @since 2017-11-30
 */
public class PatternUtils {
    public static boolean isEmail(String string) {
        return Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", string);
    }
}
