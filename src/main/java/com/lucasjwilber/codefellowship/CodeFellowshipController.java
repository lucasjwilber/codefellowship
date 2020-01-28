package com.lucasjwilber.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class CodeFellowshipController {

    @Autowired
    ApplicationUserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String getHome(Authentication auth, Model m) {
        if (auth != null) {
            m.addAttribute("username", auth.getName());
        } else {
            m.addAttribute("username", "Visitor");
        }
        return "homepage";
    }

    @GetMapping("/signup")
    public String renderSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView submitSignup(ApplicationUser user) {
//        String username, String password, String firstName, String lastName, Date dateOfBirth
        user.setPassword(passwordEncoder.encode(user.password));
        userRepo.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/");
    }

    @GetMapping("/login")
    public String renderLogin() {
        return "login";
    }

//    @PostMapping("/login")
//    public RedirectView login(String username, String password) {
//
//        userRepo.fin
//
//        return new RedirectView("/");
//    }
}
