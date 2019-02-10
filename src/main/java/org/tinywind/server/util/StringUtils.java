package org.tinywind.server.util;

import java.util.regex.Pattern;

/**
 * @author tinywind
 * @since 2017-05-14
 */
public class StringUtils {
    public static String slice(String str, int end) {
        return slice(str, 0, end);
    }

    public static String slice(final String str, final int begin, final int end) {
        if (end < 0) {
            return slice(str, begin, str.length() + end);
        }
        return str.substring(begin, end);
    }

    public static boolean imatches(String haystack, String regex) {
        return matches(haystack, regex, Pattern.CASE_INSENSITIVE);
    }

    public static boolean matches(String haystack, String regex, int flag) {
        return haystack != null && Pattern.compile(regex, flag).matcher(haystack).find();
    }
}
