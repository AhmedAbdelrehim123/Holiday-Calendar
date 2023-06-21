import com.example.holidays.model.Holiday;
import com.example.holidays.model.HolidayService;
import com.example.holidays.controller.HolidayController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;

    @Test
    public void testGetHolidays() throws IOException {
        // Create a sample list of holidays
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(new Holiday("usa", 2023, "2023-01-01", "New Year's Day"));
        holidays.add(new Holiday("usa", 2023, "2023-07-04", "Independence Day"));

        // Mock the behavior of the holidayService
        when(holidayService.fetchAndSaveHolidays("usa", 2023)).thenReturn(holidays);

        // Call the getHolidays method
        ResponseEntity<List<Holiday>> responseEntity = holidayController.getHolidays("usa", 2023);

        // Verify the returned ResponseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(holidays, responseEntity.getBody());
    }
}
