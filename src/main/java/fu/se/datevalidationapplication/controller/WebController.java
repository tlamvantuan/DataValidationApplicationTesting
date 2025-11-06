package fu.se.datevalidationapplication.controller;

import fu.se.datevalidationapplication.service.DateUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @Autowired
    private DateUtilities dateUtilities;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/date-validator")
    public String dateValidator(Model model) {
        model.addAttribute("pageTitle", "Date Validator");
        return "date-validator";
    }

    @GetMapping("/days-calculator")
    public String daysCalculator(Model model) {
        model.addAttribute("pageTitle", "Days in Month Calculator");
        return "days-calculator";
    }

    @PostMapping("/validate-date")
    @ResponseBody
    public ValidationResult validateDate(@RequestParam int day,
                                         @RequestParam int month,
                                         @RequestParam int year) {
        boolean isValid = dateUtilities.checkDate(day, month, year);
        String message = isValid ?
                String.format("%02d/%02d/%d is valid!", day, month, year) :
                String.format("%02d/%02d/%d is invalid!", day, month, year);

        return new ValidationResult(isValid, message, day, month, year);
    }

    @PostMapping("/calculate-days")
    @ResponseBody
    public DaysResult calculateDays(@RequestParam int month, @RequestParam int year) {
        int days = dateUtilities.dayInMonth(month, year);
        boolean isLeapYear = dateUtilities.isLeapYear(year);

        String[] monthNames = {"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
                "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};

        String monthName = (month >= 1 && month <= 12) ? monthNames[month - 1] : "Tháng không hợp lệ";
        String message = days > 0 ?
                String.format("📅 %s năm %d có %d ngày%s", monthName, year, days,
                        (month == 2 && isLeapYear) ? " (năm nhuận)" : "") :
                "❌ Tháng không hợp lệ!";

        return new DaysResult(days, message, month, year, isLeapYear, monthName);
    }

    // DTO Classes
    public static class ValidationResult {
        private boolean valid;
        private String message;
        private int day, month, year;

        public ValidationResult(boolean valid, String message, int day, int month, int year) {
            this.valid = valid;
            this.message = message;
            this.day = day;
            this.month = month;
            this.year = year;
        }

        // Getters and Setters
        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

    public static class DaysResult {
        private int days;
        private String message;
        private int month, year;
        private boolean leapYear;
        private String monthName;

        public DaysResult(int days, String message, int month, int year,
                          boolean leapYear, String monthName) {
            this.days = days;
            this.message = message;
            this.month = month;
            this.year = year;
            this.leapYear = leapYear;
            this.monthName = monthName;
        }

        // Getters and Setters
        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public boolean isLeapYear() {
            return leapYear;
        }

        public void setLeapYear(boolean leapYear) {
            this.leapYear = leapYear;
        }

        public String getMonthName() {
            return monthName;
        }

        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }
    }
}