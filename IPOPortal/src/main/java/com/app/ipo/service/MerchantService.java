package com.app.ipo.service;

import javax.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.app.ipo.dto.MerchantDto;
import com.app.ipo.dto.UploadDto;

public interface MerchantService 
{
	public void init();
	 
	public String uploadFile(MultipartFile file, Model model, HttpSession session);

	public Resource load(String filename);

	public String viewUploadedFiles(UploadDto uploadDto, Model model, HttpSession session);

	public String searchMerchant(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String viewMerchant(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String recordProcess(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String viewProcessed(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String update(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String updateMerchantData(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String viewUpdated(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String viewRequest(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String processRequest(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String verify(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);

	public String add(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session);
}
