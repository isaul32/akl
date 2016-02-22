package com.pyrenty.akl.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class SteamAuthResource {
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
