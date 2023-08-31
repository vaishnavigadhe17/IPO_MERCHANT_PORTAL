package com.app.ipo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.ipo.dto.AdminDto;
import com.app.ipo.service.AdminService;

@Controller
@RequestMapping(path = "/admin")
public class AdminController 
{
	@Autowired
	public AdminService adminService;
	
	@PostMapping(path = "/login")
    public String loginUser(@Valid AdminDto adminDto, BindingResult result, Model model, HttpSession session) 
	{
		return adminService.loginUser(adminDto, result, model, session);
    }
}
