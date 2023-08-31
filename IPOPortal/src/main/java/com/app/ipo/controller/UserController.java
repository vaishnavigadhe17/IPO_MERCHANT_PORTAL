package com.app.ipo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.ipo.dto.UserDto;
import com.app.ipo.repository.CategoryRepository;
import com.app.ipo.service.UserService;

@Controller
@RequestMapping(path = "/user")
public class UserController {
	@Autowired
	public UserService userService;
	
	@Autowired
	public CategoryRepository categoryRepository;

	@PostMapping(path = "/add")
	public String addNewUser(@Valid UserDto userDto, BindingResult result, Model model) {
		return userService.addNewUser(userDto, result, model);
	}

	@PostMapping(path = "/login")
	public String loginUser(@Valid UserDto userDto, BindingResult result, Model model, HttpSession session) {
		return userService.loginUser(userDto, result, model, session);
	}

	@GetMapping("/view")
	public String viewUser(@Valid UserDto userDto, Model model, HttpSession session) {
		return userService.viewUser(userDto, model, session);
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id, Model model) {
		return userService.delete(id, model);
	}

	@RequestMapping(value = "/signin")
	public String signin(@Valid UserDto userDto, BindingResult result, Model model) {
		ObjectError error = new ObjectError("globalError", "");
		result.addError(error);

		model.addAttribute("categorys", categoryRepository.findAllCategory());

		return "signin";
	}


	@RequestMapping(value = "/viewEmployee")
	public String employee(@Valid UserDto userDto, Model model, HttpSession session) {
		/*
		 * ObjectError error = new ObjectError("globalError", "");
		 * result.addError(error);
		 */

		model.addAttribute("categorys", categoryRepository.findAllCategory());

		return userService.employee(userDto, model,session);
	}
	
	@GetMapping("/available")
	public String available(@Valid UserDto userDto, Model model, HttpSession session) {
		return userService.available(userDto, model, session);
	}
	
	@RequestMapping(value = "/forgetPassword")
	public String forgetPassword(@Valid UserDto userDto, BindingResult result, Model model) {
		ObjectError error = new ObjectError("globalError", "");
		result.addError(error);

		return "forgetPassword";
	}
	
	@PostMapping("/showPassword")
	public String showPassword(@Valid UserDto userDto, BindingResult result, Model model, HttpSession session) 
	{
		return userService.showPassword(userDto, result, model, session);
	}
}