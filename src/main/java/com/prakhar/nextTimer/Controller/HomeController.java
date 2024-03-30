package com.prakhar.nextTimer.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@CrossOrigin
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

}
