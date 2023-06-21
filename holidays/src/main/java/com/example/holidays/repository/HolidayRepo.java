package com.example.holidays.repository;

import com.example.holidays.model.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepo extends MongoRepository<Holiday, String> {

    @Query(value = "{}", fields = "{ 'apiKey' : 1 }")
    String getApiKey();

    @Query(value = "{}", fields = "{ 'countryCode' : 1 }")
    String getCountryCode();
}

