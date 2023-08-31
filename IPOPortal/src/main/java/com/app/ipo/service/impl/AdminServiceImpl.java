package com.app.ipo.service.impl;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.app.ipo.dto.AdminDto;
import com.app.ipo.model.AdminEntity;
import com.app.ipo.repository.AdminRepository;
import com.app.ipo.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService
{
	@Autowired
	public AdminRepository adminRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public String loginUser(AdminDto adminDto, BindingResult result, Model model, HttpSession session) 
	{
		if(null != adminDto 
				&& (null != adminDto.getAdminName() && !adminDto.getAdminName().equalsIgnoreCase(""))
				&& (null !=adminDto.getAdminPassword() && !adminDto.getAdminPassword().equalsIgnoreCase(""))) 
		{
			AdminEntity adminEntity = adminRepository.loginUser(adminDto.getAdminName(), adminDto.getAdminPassword());
			
			if(null == adminEntity)
			{
				ObjectError error = new ObjectError("globalError", "Incorrect username or password!");
		        result.addError(error);
				 
				return "admin";
			}
			else
			{
				session.setAttribute("x", null != adminEntity && null != adminEntity.getAdminName() ? adminEntity.getAdminName() : "-");
				session.setAttribute("y", null != adminEntity && null != adminEntity.getAdminId() ? adminEntity.getAdminId() : "-");
				
				return "redirect:/user/view";
			}			
		}
		else
		{
			 ObjectError error = new ObjectError("globalError", "Kindly fill both username and password!");
		     result.addError(error);
			 
			 return "admin";
		}
	}
}
