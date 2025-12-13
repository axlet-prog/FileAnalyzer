package com.example;

import java.util.List;
import lombok.Builder;

@Builder
public record Result(
    Long minInt,
    Long maxInt,
    Double avgInt,
    Long sumInt,
    List<Long> integers,
    Double minDouble,
    Double maxDouble,
    Double avgDouble,
    Double sumDouble,
    List<Double> doubles,
    Long maxLength,
    Long minLength,
    List<String> strings
) {
}