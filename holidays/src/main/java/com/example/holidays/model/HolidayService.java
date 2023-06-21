package com.example.holidays.model;

import com.example.holidays.repository.HolidayRepo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayService {

    private final HolidayRepo repo;
    private final Environment environment;

    @Autowired
    public HolidayService(HolidayRepo repo, Environment environment) {
        this.repo = repo;
        this.environment = environment;
    }

    public String getApiKey() {
        // Retrieve the API key from the application.properties file
        return environment.getProperty("google.api.key");
    }
    public List<Holiday> fetchAndSaveHolidays(String countryCode, int year) throws IOException {
        String apiKey = getApiKey();
        if (apiKey == null) {
            throw new RuntimeException("API key not found");
        }

        String requestUrl = "https://www.googleapis.com/calendar/v3/calendars/" +
                countryCode + "@holiday.calendar.google.com/events" +
                "?key=" + apiKey +
                "&timeMin=" + year + "-01-01T00:00:00Z" +
                "&timeMax=" + year + "-12-31T23:59:59Z";

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the response and extract holiday details
            JsonObject responseJson = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray holidayArray = responseJson.getAsJsonArray("items"); // Access the "items" array
            List<Holiday> holidays = new ArrayList<>();

            for (JsonElement element : holidayArray) {
                JsonObject holidayObject = element.getAsJsonObject();
                String name = holidayObject.get("summary").getAsString();
                String date = holidayObject.get("start").getAsJsonObject().get("date").getAsString();

                // Create a new Holiday object and save it in the database
                Holiday holiday = new Holiday(countryCode, year, date, name);
                holidays.add(repo.save(holiday));
            }


            return holidays;
        } else {
            throw new RuntimeException("Failed to fetch holidays. Response code: " + responseCode);
        }
    }
}
