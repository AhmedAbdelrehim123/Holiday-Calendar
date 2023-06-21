package com.example.holidays.controller;

import com.example.holidays.model.Holiday;
import com.example.holidays.model.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import java.io.IOException;


@RestController
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    @Autowired
    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/{countryCode}/{year}")
    public ResponseEntity<List<Holiday>> getHolidays(@PathVariable String countryCode, @PathVariable int year) {
        try {
            String apiKey = holidayService.getApiKey();
            List<Holiday> holidays = holidayService.fetchAndSaveHolidays(countryCode, year);
            return new ResponseEntity<>(holidays, HttpStatus.OK);
        } catch (IOException e) {
            // Handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
