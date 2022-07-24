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
import java.util.ArrayList;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showUsers(@RequestParam(defaultValue = "0") int count, Model model){
        model.addAttribute("users", userService.getAllUser());
        return "users";
    }
    @DeleteMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    @GetMapping("admin/edit/{id}")
    public String pageEditUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user",userService.getById(id));
        model.addAttribute("listRoles",roleService.findAllRole());
        return "edit";
    }

    @PutMapping("admin/edit")
    public String pageEdit(@RequestParam("role")ArrayList<Long> roles,
                           @Valid User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        } else {
            user.setRoles(roleService.findByIdRoles(roles));
            userService.save(user);
            return "redirect:/admin";
        }
    }
    @GetMapping("admin/new")
    public String pageCreateUser(User user, Model model) {
        model.addAttribute("listRoles",roleService.findAllRole());
        return "create";
    }

    @PostMapping("admin/new")
    public String pageCreate(@RequestParam("role") ArrayList<Long> roles,
                             @ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("User with name \"%s\" is already exist!", user.getUsername())));
            return "create";
        }
        user.setRoles(roleService.findByIdRoles(roles));
        userService.save(user);
        return "redirect:/admin";
    }


}
