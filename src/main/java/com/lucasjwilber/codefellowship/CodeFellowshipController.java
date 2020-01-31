package com.lucasjwilber.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
            return new RedirectView("/myprofile/?new=true");
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
            return "myprofile";
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
            return "homepage";
        }
    }

    @PostMapping("/users/{id}/posts/new")
    public RedirectView newPost(@PathVariable long id, Principal p, String body) {
        ApplicationUser user = userRepo.findByUsername(p.getName());
        if (user.id == id) {
            ApplicationUserPost post = new ApplicationUserPost(body, user);
            postRepo.save(post);
        }
        return new RedirectView("/myprofile");
    }

    @GetMapping("/users")
    public String renderUsersPage(Model m, Principal p) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            m.addAttribute("loggedIn", true);
            m.addAttribute("users", userRepo.findAll());
            m.addAttribute("followedUsers", userRepo.findByUsername(p.getName()).UsersThisUserFollows);
            m.addAttribute("currentUserName", p.getName());
            return "allUsers";
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
            return "homepage";
        }
    }

    @PostMapping("/users/{id}/follow")
    public RedirectView followUser(@PathVariable long id, Principal p) {
        ApplicationUser selectedUser = userRepo.getOne(id);
        ApplicationUser currentUser = userRepo.findByUsername(p.getName());
        currentUser.UsersThisUserFollows.add(selectedUser);
        userRepo.save(currentUser);
        return new RedirectView("/users");
    }

    public static class UserPost {
        public String username;
        public String body;
        public long userId;
        public String createdAt;
        UserPost(String username, String body, long userId, String createdAt) {
            this.username = username;
            this.body = body;
            this.userId = userId;
            this.createdAt = createdAt;
        }
    }
    @GetMapping("/feed")
    public String getFeed(Principal p, Model m) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            m.addAttribute("loggedIn", true);

            ApplicationUser currentUser = userRepo.findByUsername(p.getName());
            Set<ApplicationUser> followedUsers = currentUser.UsersThisUserFollows;

            if (followedUsers.size() == 0) {
                m.addAttribute("noFollowers", true);
            } else {
                m.addAttribute("noFollowers", false);
                ArrayList<UserPost> allPosts = new ArrayList<>();

                for (ApplicationUser user : followedUsers) {
                    for (ApplicationUserPost post : user.posts) {
                        allPosts.add(new UserPost(user.username, post.body, user.id, post.createdAt));
                    }
                }
                //posts is a list of objects with username and post properties
                m.addAttribute("posts", allPosts);
            }
            return "feed";
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
            return "homepage";
        }
    }

    @GetMapping("/users/{id}")
    public String renderProfile(@PathVariable Long id, Principal p, Model m) {
        if (p != null) {
            m.addAttribute("username", p.getName());
            ApplicationUser user = userRepo.getOne(id);
            m.addAttribute("user", user);
            m.addAttribute("loggedIn", true);
            return "userprofile";
        } else {
            m.addAttribute("username", "Visitor");
            m.addAttribute("loggedIn", false);
            return "homepage";
        }


    }
}
