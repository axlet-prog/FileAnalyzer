package com.example;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.analysis.Analyzer;
import com.example.analysis.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;


public class AnalyzerTest {

    private BufferedReader createReader(String input) {
        return new BufferedReader(new StringReader(input));
    }

    private static final double delta = 0.00001;

    @Test
    public void validDataTest() throws IOException {
        Analyzer analyzer = new Analyzer();

        String mockInput = "100\n3.14\nHello\n2\n4e10\nSome strings";

        analyzer.analyzeContent(createReader(mockInput));
        Result result = analyzer.buildResult();

        assertNotNull(result.integers());
        assertEquals(2, result.integers().size());
        assertEquals(100L, result.maxInt());
        assertEquals(2L, result.minInt());
        assertEquals(51.0, result.avgInt(), delta);

        assertNotNull(result.doubles());
        assertEquals(2, result.doubles().size());
        assertEquals(40000000000.0, result.maxDouble(), delta);

        assertNotNull(result.strings());
        assertEquals(2, result.strings().size());
        assertEquals("Hello", result.strings().getFirst());
    }

    @Test
    public void boundaryValuesTest() throws IOException {
        Analyzer analyzer = new Analyzer();

        String maxLong = String.valueOf(Long.MAX_VALUE);
        String hugeNumber = "9223372036854775808";

        String input = maxLong + "\n" + hugeNumber;

        analyzer.analyzeContent(createReader(input));
        Result result = analyzer.buildResult();

        assertTrue(result.integers().contains(maxLong));
        assertEquals(Long.MAX_VALUE, result.maxInt());

        assertTrue(
            result.strings().contains(hugeNumber),
            "Слишком большое число должно быть обработано как строка"
        );

        assertEquals(hugeNumber.length(), result.maxLength());

        assertEquals(1, result.integers().size(), "В списке int должно быть только валидное число");
    }

    @Test
    public void onlyIntegerValuesTest() throws IOException {
        Analyzer analyzer = new Analyzer();
        String input = "10\n-5\n0";

        analyzer.analyzeContent(createReader(input));
        Result result = analyzer.buildResult();

        assertEquals(3, result.integers().size());
        assertEquals(-5L, result.minInt());
        assertEquals(BigInteger.valueOf(5), result.sumInt());

        assertNotNull(result.doubles());
        assertTrue(result.doubles().isEmpty());

        assertNotNull(result.strings());
        assertTrue(result.strings().isEmpty());
    }

    @Test
    public void onlyFloatValuesTest() throws IOException {
        Analyzer analyzer = new Analyzer();
        String input = "-2.5\n1.0\n3.14E-2";

        analyzer.analyzeContent(createReader(input));
        Result result = analyzer.buildResult();

        assertEquals(3, result.doubles().size());
        assertEquals(-2.5, result.minDouble(), delta);

        assertTrue(result.integers().isEmpty());
    }

    @Test
    public void onlyStringValuesTest() throws IOException {
        Analyzer analyzer = new Analyzer();
        String input = "text\n12.34.56";

        analyzer.analyzeContent(createReader(input));
        Result result = analyzer.buildResult();

        assertEquals(2, result.strings().size());
        assertEquals(4, result.minLength());
        assertEquals(8, result.maxLength());

        assertTrue(result.integers().isEmpty());
        assertTrue(result.doubles().isEmpty());
    }

    @Test
    public void emptyInputTest() throws IOException {
        Analyzer analyzer = new Analyzer();
        analyzer.analyzeContent(createReader(""));
        Result result = analyzer.buildResult();

        assertTrue(result.integers().isEmpty());
        assertTrue(result.doubles().isEmpty());
        assertTrue(result.strings().isEmpty());
    }
}

