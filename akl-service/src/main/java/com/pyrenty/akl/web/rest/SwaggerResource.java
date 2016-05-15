package com.pyrenty.akl.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SwaggerResource {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getSwaggerPage() {
        return "index";
    }
}
