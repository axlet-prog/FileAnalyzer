package com.example;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.analysis.Analyzer;
import com.example.analysis.Result;
import com.example.analysis.ResultCollector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AnalyzerTest {

    private static final double delta = 0.00001;
    private InMemoryOutputWriter outputWriter;
    private ResultCollector resultCollector;
    private Analyzer analyzer;

    private BufferedReader createReader(String input) {
        return new BufferedReader(new StringReader(input));
    }

    @BeforeEach
    public void setupVariables() {
        outputWriter = new InMemoryOutputWriter();
        resultCollector = new ResultCollector();
        analyzer = new Analyzer(options -> outputWriter);
    }

    @Test
    public void validDataTest() throws IOException {
        String mockInput = "100\n3.14\nHello\n2\n4e10\nSome strings";

        analyzer.analyzeContentAndWrite(
            createReader(mockInput),
            outputWriter,
            resultCollector
        );

        Result result = resultCollector.buildResult();

        assertNotNull(outputWriter.getIntegers());
        assertEquals(2, outputWriter.getIntegers().size());
        assertEquals(100L, result.maxInt());
        assertEquals(2L, result.minInt());
        assertEquals(51.0, result.avgInt(), delta);

        assertNotNull(outputWriter.getFloats());
        assertEquals(2, outputWriter.getFloats().size());
        assertEquals(40000000000.0, result.maxDouble(), delta);

        assertNotNull(outputWriter.getStrings());
        assertEquals(2, outputWriter.getStrings().size());
        assertEquals("Hello", outputWriter.getStrings().getFirst());
    }

    @Test
    public void boundaryValuesTest() throws IOException {
        String maxLong = String.valueOf(Long.MAX_VALUE);
        String hugeNumber = "9223372036854775808";

        String input = maxLong + "\n" + hugeNumber;

        analyzer.analyzeContentAndWrite(
            createReader(input),
            outputWriter,
            resultCollector
        );
        Result result = resultCollector.buildResult();

        assertTrue(outputWriter.getIntegers().contains(maxLong));
        assertEquals(Long.MAX_VALUE, result.maxInt());

        assertTrue(
            outputWriter.getStrings().contains(hugeNumber)
        );

        assertEquals(hugeNumber.length(), result.maxLength());

        assertEquals(1, outputWriter.getIntegers().size());
    }

    @Test
    public void onlyIntegerValuesTest() throws IOException {
        Analyzer analyzer = new Analyzer();
        String input = "10\n-5\n0";

        analyzer.analyzeContentAndWrite(
            createReader(input),
            outputWriter,
            resultCollector
        );
        Result result = resultCollector.buildResult();

        assertEquals(3, outputWriter.getIntegers().size());
        assertEquals(-5L, result.minInt());
        assertEquals(BigInteger.valueOf(5), result.sumInt());

        assertNotNull(outputWriter.getFloats());
        assertTrue(outputWriter.getFloats().isEmpty());

        assertNotNull(outputWriter.getStrings());
        assertTrue(outputWriter.getStrings().isEmpty());
    }

    @Test
    public void onlyFloatValuesTest() throws IOException {

        String input = "-2.5\n1.0\n3.14E-2";

        analyzer.analyzeContentAndWrite(
            createReader(input),
            outputWriter,
            resultCollector
        );
        Result result = resultCollector.buildResult();

        assertEquals(3, outputWriter.getFloats().size());
        assertEquals(-2.5, result.minDouble(), delta);

        assertTrue(outputWriter.getIntegers().isEmpty());
    }

    @Test
    public void onlyStringValuesTest() throws IOException {

        String input = "text\n12.34.56";

        analyzer.analyzeContentAndWrite(
            createReader(input),
            outputWriter,
            resultCollector
        );
        Result result = resultCollector.buildResult();

        assertEquals(2, outputWriter.getStrings().size());
        assertEquals(4, result.minLength());
        assertEquals(8, result.maxLength());

        assertTrue(outputWriter.getIntegers().isEmpty());
        assertTrue(outputWriter.getFloats().isEmpty());
    }

    @Test
    public void emptyInputTest() throws IOException {

        analyzer.analyzeContentAndWrite(
            createReader(""),
            outputWriter,
            resultCollector
        );

        assertTrue(outputWriter.getIntegers().isEmpty());
        assertTrue(outputWriter.getFloats().isEmpty());
        assertTrue(outputWriter.getStrings().isEmpty());
    }
}

