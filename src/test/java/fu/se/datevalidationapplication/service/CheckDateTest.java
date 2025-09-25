package  fu.se.datevalidationapplication.service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringJUnitConfig
@DisplayName("CheckDate Function Tests")
public class CheckDateTest {

    @Autowired
    private DateUtilities dateUtilities;

    @Test
    @DisplayName("Should return true for valid dates")
    void testCheckDate_ValidDates() {
        // Valid dates in different months
        assertThat(dateUtilities.checkDate(15, 1, 2023)).isTrue();  // Jan 15, 2023
        assertThat(dateUtilities.checkDate(28, 2, 2023)).isTrue();  // Feb 28, 2023 (non-leap)
        assertThat(dateUtilities.checkDate(29, 2, 2024)).isTrue();  // Feb 29, 2024 (leap year)
        assertThat(dateUtilities.checkDate(31, 3, 2023)).isTrue();  // Mar 31, 2023
        assertThat(dateUtilities.checkDate(30, 4, 2023)).isTrue();  // Apr 30, 2023
        assertThat(dateUtilities.checkDate(1, 12, 2023)).isTrue();  // Dec 1, 2023
        assertThat(dateUtilities.checkDate(31, 12, 2023)).isTrue(); // Dec 31, 2023
    }

    @Test
    @DisplayName("Should return false for invalid days")
    void testCheckDate_InvalidDays() {
        // Day 0
        assertThat(dateUtilities.checkDate(0, 1, 2023)).isFalse();

        // Negative day
        assertThat(dateUtilities.checkDate(-1, 1, 2023)).isFalse();

        // Day too large for month
        assertThat(dateUtilities.checkDate(32, 1, 2023)).isFalse();  // Jan has 31 days
        assertThat(dateUtilities.checkDate(31, 4, 2023)).isFalse();  // Apr has 30 days
        assertThat(dateUtilities.checkDate(30, 2, 2023)).isFalse();  // Feb has 28 days (non-leap)
        assertThat(dateUtilities.checkDate(30, 2, 2024)).isFalse();  // Feb has 29 days (leap)
    }

    @Test
    @DisplayName("Should return false for invalid months")
    void testCheckDate_InvalidMonths() {
        // Month 0
        assertThat(dateUtilities.checkDate(15, 0, 2023)).isFalse();

        // Month 13
        assertThat(dateUtilities.checkDate(15, 13, 2023)).isFalse();

        // Negative month
        assertThat(dateUtilities.checkDate(15, -1, 2023)).isFalse();
    }

    @Test
    @DisplayName("Should return false for invalid years")
    void testCheckDate_InvalidYears() {
        // Year 0
        assertThat(dateUtilities.checkDate(15, 1, 0)).isFalse();

        // Negative year
        assertThat(dateUtilities.checkDate(15, 1, -1)).isFalse();
    }

    @Test
    @DisplayName("Should handle February 29 correctly in leap and non-leap years")
    void testCheckDate_February29() {
        // February 29 in leap year - should be valid
        assertThat(dateUtilities.checkDate(29, 2, 2024)).isTrue();
        assertThat(dateUtilities.checkDate(29, 2, 2000)).isTrue();

        // February 29 in non-leap year - should be invalid
        assertThat(dateUtilities.checkDate(29, 2, 2023)).isFalse();
        assertThat(dateUtilities.checkDate(29, 2, 1900)).isFalse();
    }

    @Test
    @DisplayName("Should validate boundary dates correctly")
    void testCheckDate_BoundaryDates() {
        // First day of months
        assertThat(dateUtilities.checkDate(1, 1, 2023)).isTrue();
        assertThat(dateUtilities.checkDate(1, 2, 2023)).isTrue();
        assertThat(dateUtilities.checkDate(1, 12, 2023)).isTrue();

        // Last day of months
        assertThat(dateUtilities.checkDate(31, 1, 2023)).isTrue();  // January 31
        assertThat(dateUtilities.checkDate(28, 2, 2023)).isTrue();  // February 28 (non-leap)
        assertThat(dateUtilities.checkDate(31, 3, 2023)).isTrue();  // March 31
        assertThat(dateUtilities.checkDate(30, 4, 2023)).isTrue();  // April 30
        assertThat(dateUtilities.checkDate(31, 12, 2023)).isTrue(); // December 31
    }

    @ParameterizedTest
    @DisplayName("Should validate dates with parameterized tests")
    @CsvSource({
            "1, 1, 2023, true",     // Valid: Jan 1, 2023
            "31, 1, 2023, true",    // Valid: Jan 31, 2023
            "28, 2, 2023, true",    // Valid: Feb 28, 2023 (non-leap)
            "29, 2, 2024, true",    // Valid: Feb 29, 2024 (leap)
            "31, 4, 2023, false",   // Invalid: Apr 31, 2023 (Apr has 30 days)
            "29, 2, 2023, false",   // Invalid: Feb 29, 2023 (non-leap)
            "0, 1, 2023, false",    // Invalid: Day 0
            "15, 0, 2023, false",   // Invalid: Month 0
            "15, 13, 2023, false",  // Invalid: Month 13
            "15, 1, 0, false",      // Invalid: Year 0
            "32, 1, 2023, false",   // Invalid: Day 32
            "-1, 1, 2023, false",   // Invalid: Negative day
            "15, -1, 2023, false",  // Invalid: Negative month
            "15, 1, -1, false"      // Invalid: Negative year
    })
    void testCheckDate_ParameterizedTests(int day, int month, int year, boolean expected) {
        assertThat(dateUtilities.checkDate(day, month, year)).isEqualTo(expected);
    }
}