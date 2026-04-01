package com.br.sccon.api.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static long calculateDiff(LocalDate start, LocalDate end, String unit) {
        return switch (unit.toLowerCase()) {
            case "days" -> ChronoUnit.DAYS.between(start, end);
            case "months" -> ChronoUnit.MONTHS.between(start, end);
            case "years" -> ChronoUnit.YEARS.between(start, end);
            default -> throw new IllegalArgumentException("Unidade de tempo inválida: " + unit);
        };
    }

    public static int getFullYearsBetween(LocalDate start, LocalDate end) {
        return Period.between(start, end).getYears();
    }
}