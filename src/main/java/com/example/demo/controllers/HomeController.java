package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//Tells springboot that this is a controller that contains route

@Controller
public class HomeController {

    //When the user go to the / URL, this method will run
    @GetMapping("/")
    // Tell Springboot this method returns a HTTP response
    @ResponseBody
    public String Helloworld(){
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/about-us")
    // If the route is not marked with responsebody, we are using templates. Return relative file path to resources/templates
    // Model model parameter is automatically passed to aboutUs when is called by Spring. This is
    // the view model, allow us to inject variables into our template.
    
    public String aboutUs(Model model){
        LocalDateTime currentDateTime = LocalDateTime.now();

        // the view model is automatically passed to the template
        // any attributes in it will be available as variables

        model.addAttribute("currentDateTime", currentDateTime);
        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contactUs(){
        return "contact-us";
    }
}
