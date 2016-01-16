package com.pyrenty.akl.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SteamController {
    @RequestMapping("/api/login")
    public String login() {
        return "login";
    }
}
