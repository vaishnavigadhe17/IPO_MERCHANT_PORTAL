package com.app.ipo.service;

import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.app.ipo.dto.UserDto;

public interface UserService 
{
	public String addNewUser(UserDto userDto, BindingResult result, Model model);

	public String loginUser(UserDto userDto, BindingResult result, Model model, HttpSession session);

	public String viewUser(UserDto userDto, Model model, HttpSession session);
	
	public String available(UserDto userDto, Model model, HttpSession session);

	public String delete(long id, Model model);

	public String showPassword(UserDto userDto, BindingResult result, Model model, HttpSession session);

	String employee(UserDto userDto, Model model, HttpSession session);
}
