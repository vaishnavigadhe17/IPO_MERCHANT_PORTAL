package com.app.ipo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.ipo.dto.AdminDto;
import com.app.ipo.dto.UserDto;

@Controller
public class WebController 
{	
   @RequestMapping(value = "/index")
   public String index(@Valid UserDto userDto, BindingResult result, Model model) 
   {
	   ObjectError error = new ObjectError("globalError", "");
       result.addError(error);
       
      return "index";
   }
  
   @RequestMapping(value = "/admin")
   public String admin(@Valid AdminDto adminDto, BindingResult result, Model model) 
   {
	   ObjectError error = new ObjectError("globalError", "");
       result.addError(error);
   
      return "admin";
   }
   
   @RequestMapping(value = "/employee")
   public String employee(@Valid UserDto userDto, BindingResult result, Model model) 
   {
	   ObjectError error = new ObjectError("globalError", "");
       result.addError(error);
       
      return "employee";
   }
   
   @RequestMapping(value = "/signout")
   public String signout() 
   {
      return "signout";
   }
   
   @RequestMapping(value = "/logout")
   public String logout(@Valid UserDto userDto, BindingResult result, Model model, HttpSession session) 
   {
	  ObjectError error = new ObjectError("globalError", "Logged out succesfully!");
      result.addError(error);
      
      //session  close
      session.getAttribute("x");
         
      //invalidate session or destroy session
      session.invalidate();
       
      return "index";
   }
}