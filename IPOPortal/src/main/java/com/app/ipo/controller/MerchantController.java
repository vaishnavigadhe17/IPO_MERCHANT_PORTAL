package com.app.ipo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.ipo.dto.MerchantDto;
import com.app.ipo.dto.UploadDto;
import com.app.ipo.service.MerchantService;

@Controller
@RequestMapping(path = "/merchant")
public class MerchantController 
{
	@Autowired
	public MerchantService merchantService;

	@RequestMapping(value = "/upload")
	public String signin(Model model) 
	{
		return "upload";
	}
	
	@PostMapping(path = "/fileUpload")
	public String uploadFile(Model model, HttpSession session, @RequestParam("file") MultipartFile file) 
	{
	    String message = "";

	    try 
	    {
	    	message = merchantService.uploadFile(file, model, session);
	    } 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    	
	    	message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
	    }
	    
	    model.addAttribute("message", message);

	    return "fileupload";
	  }
	
	@GetMapping("/viewFiles")
	public String viewUploadedFiles(@Valid UploadDto uploadDto, Model model, HttpSession session) 
	{
		return merchantService.viewUploadedFiles(uploadDto, model, session);
	}
	
	@GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename, HttpSession session) 
	{
	    Resource file = merchantService.load(filename);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
	        .body(file);
	}
	
	@GetMapping("/search")
    public String searchMerchant(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.searchMerchant(merchantDto, result, model, session);
    }
	
	@PostMapping("/advsearch")
    public String advsearch(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.searchMerchant(merchantDto, result, model, session);
    }
	
	@GetMapping("/view")
    public String viewMerchant(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.viewMerchant(merchantDto, result, model, session);
    }
	
	@PostMapping("/process")
    public String recordProcess(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.recordProcess(merchantDto, result, model, session);
    }
	
	@GetMapping("/viewProcessed")
    public String viewProcessed(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.viewProcessed(merchantDto, result, model, session);
    }
	
	@GetMapping("/updateMerchantData")
    public String updateMerchantData(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.updateMerchantData(merchantDto, result, model, session);
    }
	
	@PostMapping("/update")
    public String update(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.update(merchantDto, result, model, session);
    }
	
	@GetMapping("/viewUpdated")
    public String viewUpdated(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.viewUpdated(merchantDto, result, model, session);
    }
	
	@GetMapping("/viewRequest")
    public String viewRequest(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.viewRequest(merchantDto, result, model, session);
    }
	
	@PostMapping("/processRequest")
    public String processRequest(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
        return merchantService.processRequest(merchantDto, result, model, session);
    }
	
	@RequestMapping(value = "/addMerchant")
	public String addMerchant(@Valid MerchantDto merchantDto, BindingResult result, Model model) 
	{
		ObjectError error = new ObjectError("globalError", "");
		result.addError(error);

		return "addMerchant";
	}
	
	@PostMapping(path = "/verify")
	public String verify(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		return merchantService.verify(merchantDto, result, model, session);
	}
	
	@PostMapping(path = "/add")
	public String add(@Valid MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		return merchantService.add(merchantDto, result, model, session);
	}
}