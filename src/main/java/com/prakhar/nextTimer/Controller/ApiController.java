package com.prakhar.nextTimer.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final String externalApiUrl = "https://zenquotes.io/api/today";

    @GetMapping("/today")
    public ResponseEntity<String> getToday() {
        try {
            logger.info("Calling /today endpoint");
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(externalApiUrl, String.class);
        } catch (Exception e) {
            logger.error("Error calling /today endpoint", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/today-proxy")
    public ResponseEntity<String> getTodayProxy() {
        try {
            logger.info("Calling /today-proxy endpoint");
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(externalApiUrl, String.class);
        } catch (Exception e) {
            logger.error("Error calling /today-proxy endpoint", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
