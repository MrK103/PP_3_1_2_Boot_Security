package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;


@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String showLogin() {
        return "login";
    }


    @GetMapping("/user")
    public String pageForUser(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "user";
    }
    @GetMapping("registration/new")
    public String pageregistration(User user, Model model) {
        model.addAttribute("listRoles",roleService.findAllRole());
        return "registration";
    }

    @PostMapping("registration/new")
    public String pageRegisterUser(@RequestParam("role") ArrayList<Long> roles,
                                   @ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("User with name \"%s\" is already exist!", user.getUsername())));
            return "create";
        }
        user.setRoles(roleService.findByIdRoles(roles));
        userService.save(user);
        return "redirect:/login";
    }
}
