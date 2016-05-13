package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.security.SecurityUtils;
import com.pyrenty.akl.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
@RequestMapping("/api")
public class SteamAuthResource {
    @Inject
    private UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/return")
    public String returnBack() {
        User user = userService.getUserWithAuthorities();

        if (user != null && user.getEmail() == null) {
            return "return";
        }

        return "return";
    }
}
