package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.analysis.Analyzer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PatternTests {

    @ParameterizedTest
    @ValueSource(strings = {
        "0",
        "123",
        "-45",
        "2147483647",
        "-2147483648",
        "9223372036854775807",
        "-9223372036854775808",
        "007"
    })
    public void testValidIntegers(String input) {
        assertTrue(Analyzer.INT_PATTERN.matcher(input).matches());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0.0",
        "3.14159",
        "-123.45",
        "123.0",
        "0.0005",
        "1.5E2",
        "2.5e-3",
        "-4.2E+5",
    })
    public void testValidDoubles(String input) {
        assertTrue(Analyzer.DOUBLE_PATTERN.matcher(input).matches());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1.2.3",
        "1,5",
        "1. 5",
        "e10",
        ".",
        "1.2E",
        "Text"
    })
    public void testInvalidDoubles(String input) {
        assertFalse(Analyzer.DOUBLE_PATTERN.matcher(input).matches());
    }
}
