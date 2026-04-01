package com.br.sccon.api.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void shouldCalculateDifferenceInDays() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 10);

        long diff = DateUtils.calculateDiff(start, end, "days");

        assertEquals(9, diff);
    }

    @Test
    void shouldReturnFullYearsBetweenDates() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2023, 6, 1);

        int years = DateUtils.getFullYearsBetween(start, end);

        assertEquals(3, years);
    }
}