package fu.se.datevalidationapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig
@DisplayName("DayInMonth Function Tests")
public class DayInMonthTest {

    @Autowired
    private DateUtilities dateUtilities;

    @BeforeEach
    void setUp() {
        // Setup code if needed
    }

    @Test
    @DisplayName("Should return correct days for valid months in non-leap year")
    void testDayInMonth_ValidMonths_NonLeapYear() {
        // January - 31 days
        assertThat(dateUtilities.dayInMonth(1, 2023)).isEqualTo(31);

        // February - 28 days (non-leap year)
        assertThat(dateUtilities.dayInMonth(2, 2023)).isEqualTo(28);

        // March - 31 days
        assertThat(dateUtilities.dayInMonth(3, 2023)).isEqualTo(31);

        // April - 30 days
        assertThat(dateUtilities.dayInMonth(4, 2023)).isEqualTo(30);

        // May - 31 days
        assertThat(dateUtilities.dayInMonth(5, 2023)).isEqualTo(31);

        // June - 30 days
        assertThat(dateUtilities.dayInMonth(6, 2023)).isEqualTo(30);

        // July - 31 days
        assertThat(dateUtilities.dayInMonth(7, 2023)).isEqualTo(31);

        // August - 31 days
        assertThat(dateUtilities.dayInMonth(8, 2023)).isEqualTo(31);

        // September - 30 days
        assertThat(dateUtilities.dayInMonth(9, 2023)).isEqualTo(30);

        // October - 31 days
        assertThat(dateUtilities.dayInMonth(10, 2023)).isEqualTo(31);

        // November - 30 days
        assertThat(dateUtilities.dayInMonth(11, 2023)).isEqualTo(30);

        // December - 31 days
        assertThat(dateUtilities.dayInMonth(12, 2023)).isEqualTo(31);
    }

    @Test
    @DisplayName("Should return 29 for February in leap years")
    void testDayInMonth_February_LeapYear() {
        // Standard leap year
        assertThat(dateUtilities.dayInMonth(2, 2024)).isEqualTo(29);

        // Century leap year (divisible by 400)
        assertThat(dateUtilities.dayInMonth(2, 2000)).isEqualTo(29);

        // Year divisible by 4
        assertThat(dateUtilities.dayInMonth(2, 2020)).isEqualTo(29);
    }

    @Test
    @DisplayName("Should return 28 for February in non-leap years")
    void testDayInMonth_February_NonLeapYear() {
        // Regular non-leap year
        assertThat(dateUtilities.dayInMonth(2, 2023)).isEqualTo(28);

        // Century non-leap year (not divisible by 400)
        assertThat(dateUtilities.dayInMonth(2, 1900)).isEqualTo(28);

        // Year not divisible by 4
        assertThat(dateUtilities.dayInMonth(2, 2021)).isEqualTo(28);
    }

    @Test
    @DisplayName("Should return -1 for invalid months")
    void testDayInMonth_InvalidMonths() {
        // Month 0
        assertThat(dateUtilities.dayInMonth(0, 2023)).isEqualTo(-1);

        // Month 13
        assertThat(dateUtilities.dayInMonth(13, 2023)).isEqualTo(-1);

        // Negative month
        assertThat(dateUtilities.dayInMonth(-1, 2023)).isEqualTo(-1);

        // Large invalid month
        assertThat(dateUtilities.dayInMonth(100, 2023)).isEqualTo(-1);
    }

    @ParameterizedTest
    @DisplayName("Should return 31 for months with 31 days")
    @ValueSource(ints = {1, 3, 5, 7, 8, 10, 12})
    void testDayInMonth_MonthsWith31Days(int month) {
        assertThat(dateUtilities.dayInMonth(month, 2023)).isEqualTo(31);
    }

    @ParameterizedTest
    @DisplayName("Should return 30 for months with 30 days")
    @ValueSource(ints = {4, 6, 9, 11})
    void testDayInMonth_MonthsWith30Days(int month) {
        assertThat(dateUtilities.dayInMonth(month, 2023)).isEqualTo(30);
    }

    @ParameterizedTest
    @DisplayName("Should handle leap years correctly")
    @CsvSource({
            "2024, 29", // Leap year
            "2023, 28", // Non-leap year
            "2000, 29", // Century leap year
            "1900, 28", // Century non-leap year
            "2020, 29", // Leap year
            "2021, 28"  // Non-leap year
    })
    void testDayInMonth_FebruaryVariousYears(int year, int expectedDays) {
        assertThat(dateUtilities.dayInMonth(2, year)).isEqualTo(expectedDays);
    }
}