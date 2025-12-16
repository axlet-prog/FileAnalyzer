package com.example.analysis;

public interface OutputWriter extends AutoCloseable {

    void writeInteger(String line);

    void writeFloat(String line);

    void writeString(String line);
}
