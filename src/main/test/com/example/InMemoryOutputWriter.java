package com.example;

import com.example.analysis.OutputWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class InMemoryOutputWriter implements OutputWriter {

    private final List<String> integers = new ArrayList<>();
    private final List<String> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    @Override
    public void writeInteger(String line) {
        integers.add(line);
    }

    @Override
    public void writeFloat(String line) {
        floats.add(line);
    }

    @Override
    public void writeString(String line) {
        strings.add(line);
    }

    @Override
    public void close() {
    }
}
