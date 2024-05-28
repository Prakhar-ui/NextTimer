package com.prakhar.nextTimer.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Value("${unsplash.api.access-key}")
    private String unsplashAccessKey;

    @GetMapping("/unsplash")
    public ResponseEntity<String> getUnsplashAccessKey() {
        try {
            logger.info("Retrieving Unsplash Access Key Success");
            return ResponseEntity.ok(unsplashAccessKey);
        } catch (Exception e) {
            logger.error("Error retrieving Unsplash Access Key", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
