package com.example.booking.util;

import com.example.booking.exception.DateRangeException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

/**
 * @author mfedechko
 */
@UtilityClass
public class DateUtils {

    public static void validateDateRange(final LocalDate startDate,
                                         final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new DateRangeException("End date must be after start date");
        }
    }
}
