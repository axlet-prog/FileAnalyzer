package com.example.analysis;

import java.math.BigInteger;
import lombok.Builder;

@Builder
public record Result(
    Long intsSize,
    Long minInt,
    Long maxInt,
    Double avgInt,
    BigInteger sumInt,
    Long doublesSize,
    Double minDouble,
    Double maxDouble,
    Double avgDouble,
    Double sumDouble,
    Long stringsSize,
    Long maxLength,
    Long minLength
) {
}