package org.tinywind.server.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatternUtilsSecurityTest {
    @Test
    public void acceptsTheExistingEmailShapeWithLiteralLocalDots() {
        assertTrue(PatternUtils.isEmail("first.last_2@example.co.uk"));
        assertFalse(PatternUtils.isEmail("first!last@example.com"));
        assertFalse(PatternUtils.isEmail("first..last@example.com"));
        assertFalse(PatternUtils.isEmail("first@example"));
    }

    @Test(timeout = 2000)
    public void rejectsLongAdversarialInputsWithoutBacktracking() {
        assertFalse(PatternUtils.isEmail("a".repeat(20000) + "!"));
        assertFalse(PatternUtils.isEmail("a@" + "a.".repeat(20000) + "!"));
    }
}
