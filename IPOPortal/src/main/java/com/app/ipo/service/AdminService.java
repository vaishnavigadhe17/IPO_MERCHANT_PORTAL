package com.app.ipo.service;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.app.ipo.dto.AdminDto;

public interface AdminService 
{
	public String loginUser(AdminDto adminDto, BindingResult result, Model model, HttpSession session);
}
