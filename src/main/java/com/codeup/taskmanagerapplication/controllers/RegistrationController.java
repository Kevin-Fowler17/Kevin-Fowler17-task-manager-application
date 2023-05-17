package com.codeup.taskmanagerapplication.controllers;

import com.codeup.taskmanagerapplication.models.User;
import com.codeup.taskmanagerapplication.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute User user, Model model, HttpSession session) {

        User userNameCheck = userDao.findByUsername(user.getUsername());
        User userEmailCheck = userDao.findByEmail(user.getEmail());

        session.setAttribute("stickyUsername", user.getUsername());

        if (userNameCheck != null) {
            model.addAttribute("userExists", true);
        }

        if (userEmailCheck != null) {
            model.addAttribute("userEmailExists", true);
        }

        if (userNameCheck != null || userEmailCheck != null) {
            return "users/registration";
        }

        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "redirect/login";
    }
}
