package com.app.ipo.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import com.app.ipo.dto.UserDto;
import com.app.ipo.model.CategoryEntity;
import com.app.ipo.model.UserEntity;
import com.app.ipo.repository.CategoryRepository;
import com.app.ipo.repository.UserRepository;
import com.app.ipo.service.UserService;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public CategoryRepository categoryRepository;
	
	@Autowired
	ModelMapper modelMapper;
		
	@Override
	public String addNewUser(UserDto userDto, BindingResult result, Model model) 
	{		
		model.addAttribute("categorys", categoryRepository.findAllCategory());
		
		if(null != userDto 
				&& (null != userDto.getName() && !userDto.getName().equalsIgnoreCase(""))
				&& (null != userDto.getUserPhoneno() && userDto.getUserPhoneno() > 0)
				&& (null != userDto.getUserEmailId() && !userDto.getUserEmailId().equalsIgnoreCase(""))
				&& (null != userDto.getUserAddress() && !userDto.getUserAddress().equalsIgnoreCase(""))
				&& (null != userDto.getUserLocation() && userDto.getUserLocation() > 0)
				&& (null != userDto.getCategoryId() && userDto.getCategoryId() > 0)
				&& (null != userDto.getUserName() && !userDto.getUserName().equalsIgnoreCase(""))
				&& (null != userDto.getUserPassword() && !userDto.getUserPassword().equalsIgnoreCase("")))
		{
			if(null != userDto && userDto.getCategoryId() > 0)
			{
				if(!isNumeric(userDto.getUserPhoneno().toString()))
				{
					ObjectError error = new ObjectError("globalError", "Kindly enter 10 digit numeric values in phone number field!");
			        result.addError(error);
					 
					return "signin";
				}
				else if(String.valueOf(userDto.getUserPhoneno()).length() != 10)
				{
					ObjectError error = new ObjectError("globalError", "Kindly enter 10 digit numeric values in phone number field!");
			        result.addError(error);
					 
					return "signin";
				}
				else if(!isValidEmail(userDto.getUserEmailId()))
				{
					ObjectError error = new ObjectError("globalError", "Kindly valid email id!");
			        result.addError(error);
					 
					return "signin";
				}
				else
				{
					Boolean t = Boolean.FALSE;
							
							
					Optional<UserEntity> objUserEntity = userRepository.findByPhoneno(userDto.getUserPhoneno());
					
					if(objUserEntity.isPresent())
					{
						t = Boolean.TRUE;
					}
					
					objUserEntity = userRepository.findByEmailId(userDto.getUserEmailId());
					
					if(!objUserEntity.isPresent())
					{
						t = Boolean.TRUE;
					}
					
					if(t)
					{
						UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
						
						userEntity.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
						
						UserEntity user = userRepository.save(userEntity);
				        
				        if(null != user)
						{
							ObjectError error = new ObjectError("globalError", "Registered successfully!");
					        result.addError(error);
							 
					        return "redirect:/user/view";
						}
					}
					else
					{
						ObjectError error = new ObjectError("globalError", "The provided Email Id or Phone number already exist in our system!");
				        result.addError(error);
				        
						return "signin";
					}
				}
			}
			else
			{
				ObjectError error = new ObjectError("globalError", "Kindly choose employee role!");
		        result.addError(error);
		        
				return "signin";
			}
		}
		else
		{
			ObjectError error = new ObjectError("globalError", "Kindly fill all mandatory fields!");
		    result.addError(error);
		}
        
        return "signin";
	}

	
	
	@Override
	public String loginUser(UserDto userDto, BindingResult result, Model model, HttpSession session) 
	{
		if(null != userDto 
				&& (null != userDto.getUserName() && !userDto.getUserName().equalsIgnoreCase(""))
				&& (null !=userDto.getUserPassword() && !userDto.getUserPassword().equalsIgnoreCase(""))) 
		{
			UserEntity userEntity = userRepository.loginUser(userDto.getUserName(), userDto.getUserPassword());
			
			if(null == userEntity)
			{
				ObjectError error = new ObjectError("globalError", "Incorrect username or password!");
		        result.addError(error);
				 
				return "index";
			}
			else
			{
				session.setAttribute("x", null != userEntity && null != userEntity.getUserName() ? userEntity.getUserName() : "-");
				session.setAttribute("y", null != userEntity && null != userEntity.getUserId() ? userEntity.getUserId() : "-");
				
				if(userEntity.getCategoryId() == 1)
				{
					return "redirect:/merchant/upload";
				}
				else if(userEntity.getCategoryId() == 2)
				{
					return "redirect:/merchant/search";
				}
				else
				{
					return "redirect:/merchant/view";
				}
			}			
		}
		else
		{
			 ObjectError error = new ObjectError("globalError", "Kindly fill both username and password!");
		     result.addError(error);
			 
			 return "index";
		}
	}
	
	@Override
	public String viewUser(UserDto userDto, Model model, HttpSession session)
	{
		List<Long> categoryId = Arrays.asList(1L, 2L, 3L);
		
		List<UserEntity> objUserEntity = userRepository.findUserByCategory(categoryId);
		
		if(null != objUserEntity)
		{
			List<UserDto> userDtoList = objUserEntity
					  .stream()
					  .map(user -> modelMapper.map(user, UserDto.class))
					  .collect(Collectors.toList());
			
			for(UserDto user: userDtoList)
			{
				Optional<CategoryEntity> category = categoryRepository.findByCategoryId(user.getCategoryId());
				
				if(category.isPresent())
				{
					user.setCategoryName(category.get().getCategoryName());
				} 
			}
			
			model.addAttribute("users", userDtoList);
		}
		
		return "adminDashboard";
	}

	
	@Override
	public String employee(UserDto userDto, Model model, HttpSession session)
	{
		List<Long> categoryId = Arrays.asList(1L, 2L, 3L);
		
		List<UserEntity> objUserEntity = userRepository.findUserByCategory(categoryId);
		
		if(null != objUserEntity)
		{
			List<UserDto> userDtoList = objUserEntity
					  .stream()
					  .map(user -> modelMapper.map(user, UserDto.class))
					  .collect(Collectors.toList());
			
			for(UserDto user: userDtoList)
			{
				Optional<CategoryEntity> category = categoryRepository.findByCategoryId(user.getCategoryId());
				
				if(category.isPresent())
				{
					user.setCategoryName(category.get().getCategoryName());
				} 
			}
			
			model.addAttribute("users", userDtoList);
		}
		
		return "viewEmployee";
	}
	
	@Override
	public String available(UserDto userDto, Model model, HttpSession session)
	{
		UserEntity userEntity = userRepository.findByName(session.getAttribute("x").toString());
		
		if(null != userEntity)
		{
			UserDto userDtos = modelMapper.map(userEntity, UserDto.class);
			
			model.addAttribute("users", userDtos);
		}
		
		return "viewRestaurant";
	}

	@Override
	public String delete(long id, Model model) 
	{
		UserEntity userEntity = userRepository.findOne(id);
		
		if(null != userEntity)
		{
			userRepository.delete(id);
		}
		
		return "redirect:/user/view";
	}
	
	public boolean isNumeric(String strNum) 
	{
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		
	    if (strNum == null) 
	    {
	        return false; 
	    }
	    
	    return pattern.matcher(strNum).matches();
	}
	
	private boolean isValidEmail(String userEmailId) 
	{
		String regex = "^(.+)@(.+)$";  
		
	    Pattern pattern = Pattern.compile(regex);  
	    
	    Matcher matcher = pattern.matcher(userEmailId);  
	    
		return matcher.matches();
	}

	@Override
	public String showPassword(UserDto userDto, BindingResult result, Model model, HttpSession session) 
	{
		if(null != userDto 
				&& (null != userDto.getUserPhoneno() && userDto.getUserPhoneno() > 0)
				&& (null != userDto.getUserEmailId() && !userDto.getUserEmailId().equalsIgnoreCase("")))
		{
			if(!isNumeric(userDto.getUserPhoneno().toString()))
			{
				ObjectError error = new ObjectError("globalError", "Kindly enter 10 digit numeric values in phone number field!");
		        result.addError(error);
				 
				return "forgetPassword";
			}
			else if(String.valueOf(userDto.getUserPhoneno()).length() != 10)
			{
				ObjectError error = new ObjectError("globalError", "Kindly enter 10 digit numeric values in phone number field!");
		        result.addError(error);
				 
				return "forgetPassword";
			}
			else if(!isValidEmail(userDto.getUserEmailId()))
			{
				ObjectError error = new ObjectError("globalError", "Kindly enter valid email id!");
		        result.addError(error);
				 
				return "forgetPassword";
			}
			else
			{
				Optional<UserEntity> userEntity = userRepository.findByPhonenoAndEmail(userDto.getUserPhoneno(), userDto.getUserEmailId());
				
				if(userEntity.isPresent())
				{
					ObjectError error = new ObjectError("globalError", "Dear Customer, Your current password is: " + userEntity.get().getUserPassword());
			        result.addError(error);
			        
			        return "showPassword";
				}
				else
				{
					ObjectError error = new ObjectError("globalError", "Kindly enter correct email id or phone number!");
			        result.addError(error);
					 
					return "forgetPassword";
				}
			}
		}
		else
		{
			ObjectError error = new ObjectError("globalError", "Kindly fill all mandatory fields!");
		    result.addError(error);
		    
		    return "forgetPassword";
		}
	}
}