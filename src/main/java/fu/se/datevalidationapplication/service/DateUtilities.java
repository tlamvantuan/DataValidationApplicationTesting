package fu.se.datevalidationapplication.service;

import org.springframework.stereotype.Service;

@Service
public class DateUtilities {

    /**
     * Returns the number of days in a given month and year
     *
     * @param month Month (1-12)
     * @param year  Year
     * @return Number of days in month, or -1 if invalid month
     */
    public int dayInMonth(int month, int year) {
        if (month < 1 || month > 12) {
            return -1;
        }

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Check for February in leap year
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }

        return daysInMonth[month - 1];
    }

    /**
     * Validates if a given date is valid
     *
     * @param day   Day of month
     * @param month Month (1-12)
     * @param year  Year
     * @return true if date is valid, false otherwise
     */
    public boolean checkDate(int day, int month, int year) {
        if (year < 1 || month < 1 || month > 12 || day < 1) {
            return false;
        }

        int maxDays = dayInMonth(month, year);
        return maxDays != -1 && day <= maxDays;
    }

    /**
     * Checks if a year is a leap year
     *
     * @param year Year to check
     * @return true if leap year, false otherwise
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}