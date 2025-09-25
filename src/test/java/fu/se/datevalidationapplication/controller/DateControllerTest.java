package fu.se.datevalidationapplication.controller;

import fu.se.datevalidationapplication.service.DateUtilities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DateController.class)
@DisplayName("Date Controller Integration Tests")
public class DateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DateUtilities dateUtilities;

    @Test
    @DisplayName("Should return days in month via REST API")
    void testGetDaysInMonthEndpoint() throws Exception {
        when(dateUtilities.dayInMonth(1, 2023)).thenReturn(31);

        mockMvc.perform(get("/api/date/days-in-month")
                        .param("month", "1")
                        .param("year", "2023"))
                .andExpect(status().isOk())
                .andExpect(content().string("31"));
    }

    @Test
    @DisplayName("Should validate date via REST API")
    void testValidateDateEndpoint() throws Exception {
        when(dateUtilities.checkDate(15, 1, 2023)).thenReturn(true);

        mockMvc.perform(get("/api/date/validate")
                        .param("day", "15")
                        .param("month", "1")
                        .param("year", "2023"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}