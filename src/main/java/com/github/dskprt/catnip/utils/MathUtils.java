package com.github.dskprt.catnip.utils;

import java.math.BigDecimal;

public class MathUtils {

    public static float round(float value, int scale) {
        return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
