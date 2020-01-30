package com.lucasjwilber.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class CodeFellowshipController {

    @Autowired
    ApplicationUserRepository userRepo;

    @Autowired
    ApplicationUserPostRepository postRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String getHome(Principal p, Model m) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            m.addAttribute("loggedIn", true);
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
        }
        return "homepage";
    }

    @GetMapping("/signup")
    public String renderSignup(Principal p, Model m) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            m.addAttribute("loggedIn", true);
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
        }
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView submitSignup(ApplicationUser user) {
        if (userRepo.findByUsername(user.username) == null) {
            user.setPassword(passwordEncoder.encode(user.password));
            userRepo.save(user);
            //autologin on signup:
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new RedirectView("/myprofile?new=true");
        } else {
            return new RedirectView("/signup?taken=true");
        }
    }

    @GetMapping("/login")
    public String renderLogin(Principal p, Model m) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            m.addAttribute("loggedIn", true);
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
        }
        return "login";
    }

    @GetMapping("/myprofile")
    public String renderProfile(Principal p, Model m) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            ApplicationUser user = userRepo.findByUsername(p.getName());
            m.addAttribute("user", user);
            m.addAttribute("loggedIn", true);
            return "profile";
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
            return "homepage";
        }
    }

    @PostMapping("/myprofile/{id}/posts/new")
    public RedirectView newPost(@PathVariable long id, Principal p, String body) {
        ApplicationUser user = userRepo.findByUsername(p.getName());
        if (user.id == id) {
            ApplicationUserPost post = new ApplicationUserPost(body, user);
            postRepo.save(post);
        }
        return new RedirectView("/myprofile");
    }

    @GetMapping("/users")
    public String renderUsersPage(Model m) {
        m.addAttribute("users", userRepo.findAll());
        return "allUsers";
    }
}
