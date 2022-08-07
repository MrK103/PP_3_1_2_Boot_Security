package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class MainController {

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String showLogin() {
        return "login";
    }

    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        if (Objects.equals(errorMessage, "Bad credentials")) errorMessage = "User not found";
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
