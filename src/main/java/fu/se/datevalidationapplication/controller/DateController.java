package fu.se.datevalidationapplication.controller;

import fu.se.datevalidationapplication.service.DateUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/date")
public class DateController {

    @Autowired
    private DateUtilities dateUtilities;

    @GetMapping("/days-in-month")
    public int getDaysInMonth(@RequestParam int month, @RequestParam int year) {
        return dateUtilities.dayInMonth(month, year);
    }

    @GetMapping("/validate")
    public boolean validateDate(@RequestParam int day,
                                @RequestParam int month,
                                @RequestParam int year) {
        return dateUtilities.checkDate(day, month, year);
    }
}