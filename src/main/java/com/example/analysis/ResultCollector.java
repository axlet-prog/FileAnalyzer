package com.example.analysis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class ResultCollector {

    private Long minLong = Long.MAX_VALUE;
    private Long maxLong = Long.MIN_VALUE;
    private Long longsSize = 0L;
    private BigInteger sumLong = BigInteger.ZERO;

    private Double minDouble = Double.MAX_VALUE;
    private Double maxDouble = -Double.MAX_VALUE;
    private Long doublesSize = 0L;
    private Double sumDouble = 0.0;

    private long maxLength = 0;
    private Long stringsSize = 0L;
    private long minLength = Integer.MAX_VALUE;

    public Result buildResult() {
        Result.ResultBuilder builder = Result.builder();
        builder
            .intsSize(this.longsSize)
            .doublesSize(this.doublesSize)
            .stringsSize(this.stringsSize)
        ;

        if (longsSize > 0) {
            BigDecimal sumDecimal = new BigDecimal(this.sumLong);
            BigDecimal countDecimal = BigDecimal.valueOf(longsSize);
            double avg = sumDecimal.divide(countDecimal, MathContext.DECIMAL64).doubleValue();
            builder
                .minInt(minLong)
                .maxInt(maxLong)
                .sumInt(sumLong)
                .avgInt(avg);
        }
        if (doublesSize > 0) {
            double avgDouble = sumDouble / doublesSize;
            builder
                .minDouble(minDouble)
                .maxDouble(maxDouble)
                .sumDouble(sumDouble)
                .avgDouble(avgDouble);
        }
        if (stringsSize > 0) {
            builder
                .minLength(this.minLength)
                .maxLength(maxLength);
        }

        return builder.build();
    }

    public void processInteger(String integerString) throws NumberFormatException {
        long value = Long.parseLong(integerString);
        longsSize += 1;
        maxLong = Math.max(value, maxLong);
        minLong = Math.min(value, minLong);
        sumLong = sumLong.add(BigInteger.valueOf(value));
    }

    public void processDouble(String doubleString) throws NumberFormatException {
        double value = Double.parseDouble(doubleString);
        doublesSize += 1;
        maxDouble = Math.max(value, maxDouble);
        minDouble = Math.min(value, minDouble);
        sumDouble += value;
    }

    public void processString(String str) {
        stringsSize += 1;
        int len = str.length();
        maxLength = Math.max(len, maxLength);
        minLength = Math.min(len, minLength);
    }
}
