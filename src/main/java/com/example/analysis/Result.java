package com.example.analysis;

import java.math.BigInteger;
import java.util.List;
import lombok.Builder;

@Builder
public record Result(
    Long minInt,
    Long maxInt,
    Double avgInt,
    BigInteger sumInt,
    List<String> integers,
    Double minDouble,
    Double maxDouble,
    Double avgDouble,
    Double sumDouble,
    List<String> doubles,
    Long maxLength,
    Long minLength,
    List<String> strings
) {
}