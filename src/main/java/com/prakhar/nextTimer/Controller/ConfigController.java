package com.prakhar.nextTimer.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${unsplash.api.access-key}")
    private String unsplashAccessKey;

    @GetMapping("/unsplash")
    public String getUnsplashAccessKey() {
        return unsplashAccessKey;
    }

}
