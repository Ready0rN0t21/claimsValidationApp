package com.alliance.claimsvalidationapp.controller;

import com.alliance.claimsvalidationapp.entity.User;
import com.alliance.claimsvalidationapp.service.ClaimService;
import com.alliance.claimsvalidationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ClaimService claimService;

    @PostMapping("/addUser")
    public ModelAndView registerUserController(@ModelAttribute User user){

        user.setUserStatus("active");
        User savedUser = userService.registerUserService(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listOfUsers" ,userService.getAllUserService());
        modelAndView.addObject("listOfClaims", claimService.getAllClaims());
        modelAndView.setViewName("accountingPage");

        return modelAndView;
    }

    @GetMapping("/login")
    public String indexPage(){
        return "login";
    }

    @PostMapping("/loginRedirect")
    @ResponseBody
    public String loginUserController(@ModelAttribute User user, HttpSession httpSession){
        User sessionUser = userService.loginUserService(user.getEmail(), user.getPassword());
        System.out.println(sessionUser);
        if(sessionUser != null){
            httpSession.setAttribute("User", sessionUser);
            if(sessionUser.getUsertype().contains("Accounting")){
                return "accounting";
            } else {
                return "employee";
            }
        } else {
            return "User does not exist";
        }
    }

    @GetMapping("/homepageAcc")
    public ModelAndView accountingPageRedirect(HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listOfUsers", userService.getAllUserService());
        modelAndView.addObject("listOfClaims", claimService.getAllClaims());
        modelAndView.addObject("user", httpSession.getAttribute("User"));
        modelAndView.setViewName("accountingPage");
        return modelAndView;
    }

    @GetMapping("/homepageEmp")
    public ModelAndView employeePageRedirect(HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", httpSession.getAttribute("User"));
        modelAndView.setViewName("employeePage");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logoutUserController(HttpServletRequest request){
        HttpSession sessionUser = request.getSession(false);
        if (sessionUser != null) {
            sessionUser.invalidate();
        }
        return "redirect:/user/login";
    }

    @PostMapping("/deleteSessionUser")
    public String deleteSessionUserController(Long id){
        userService.deleteUserService(id);
        return "login";
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public void deleteUserController(Long id){
        userService.deleteUserService(id);
    }


    @PostMapping("/editSessionUserPassword")
    @ResponseBody
    public User editSessionUserPasswordController(Long id, String password){
        return userService.editSessionUserPasswordService(id, password);
    }

    @PostMapping("/validateSessionUserPassword")
    @ResponseBody
    public String validateSessionUserPassword(Long id, String password){
        return userService.validateSessionUserPasswordService(id, password);
    }

    @PostMapping("/editSessionName")
    @ResponseBody
    public void editSessionNameController(Long id, String firstName, String lastName, HttpSession httpSession){
        User user = userService.editSessionNameService(id, firstName, lastName);
        httpSession.setAttribute("User", user);
    }


}
