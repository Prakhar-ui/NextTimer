package com.prakhar.nextTimer.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final String externalApiUrl = "https://zenquotes.io/api/today";

    @GetMapping("/today")
    public ResponseEntity<String> getToday() {
        // You can customize headers, add authentication, etc., if needed
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(externalApiUrl, String.class);

        return response;
    }

    @GetMapping("/today-proxy")
    public ResponseEntity<String> getTodayProxy() {
        // Use this method as a proxy to forward the request to the external API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(externalApiUrl, String.class);

        return response;
    }
}
