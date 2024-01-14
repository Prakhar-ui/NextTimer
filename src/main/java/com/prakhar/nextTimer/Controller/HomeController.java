package com.prakhar.nextTimer.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "react";
    }

}
