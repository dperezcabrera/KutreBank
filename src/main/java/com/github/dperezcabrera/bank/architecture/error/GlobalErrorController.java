package com.github.dperezcabrera.bank.architecture.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalErrorController implements ErrorController {

    @GetMapping("/error")
    public String index() {
        return "/#/error";
    }

    @Override
    public String getErrorPath() {
        return "/#/error";
    }
}
