package com.example.codefellowship.Controllers;

import com.example.codefellowship.Model.AppUser;
import com.example.codefellowship.Model.Post;
import com.example.codefellowship.Repositories.AppUserRepository;
import com.example.codefellowship.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppUserController {
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PostRepository postRepository;
    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView signUpUser(@RequestParam String username, @RequestParam String password,@RequestParam String firstName,@RequestParam String lastName,@RequestParam String dateOfBirth,@RequestParam String bio){
        AppUser appUser = new AppUser(username, encoder.encode(password),firstName,lastName,dateOfBirth,bio,"ROLE_USER");
        appUserRepository.save(appUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(appUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
    @GetMapping("/")
    public String home(Principal principal,Model model){
        if (principal!=null) {
            AppUser user = appUserRepository.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("posts",postRepository.findAll());
        return "index";
    }
    @GetMapping("/user/{user}")
    public String userPage(@PathVariable AppUser user,Model model,Principal principal){
        AppUser authUser=appUserRepository.findByUsername(principal.getName());
        model.addAttribute("authUser",authUser);

        model.addAttribute("user",user);
        return "user";
    }
    @PostMapping("/user")
    public RedirectView addPost(String post,String userName) {
        AppUser user = appUserRepository.findByUsername(userName);
        Post post1=new Post(post,user);
        postRepository.save(post1);
        return new RedirectView("/user/"+user.getId());
    }
    @PostMapping("/user/follow")
    public RedirectView addFollower(int followedUser, Principal p) {
        AppUser primaryUser = appUserRepository.findByUsername(p.getName());
        primaryUser.addFollower(appUserRepository.findById(followedUser).get());
        appUserRepository.save(primaryUser);
        return new RedirectView("/user/"+followedUser);
    }
    @PostMapping("/users/unfollow")
    public RedirectView removeFollower(int unfollowedUser, Principal p) {
        AppUser primaryUser = appUserRepository.findByUsername(p.getName());
        primaryUser.removeFollower(appUserRepository.findById(unfollowedUser).get());
        appUserRepository.save(primaryUser);
        return new RedirectView("/user/"+unfollowedUser);
    }
    @GetMapping("/feed/{user}")
    public String feed(Model model,Principal principal){
        AppUser authUser=appUserRepository.findByUsername(principal.getName());
        model.addAttribute("authUser",authUser);
        return "feed";
    }
}
