package com.app.ipo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import com.app.ipo.dto.MerchantDto;
import com.app.ipo.dto.UploadDto;
import com.app.ipo.model.MerchantEntity;
import com.app.ipo.model.MerchantUpdateEntity;
import com.app.ipo.model.UploadEntity;
import com.app.ipo.model.UserEntity;
import com.app.ipo.repository.MerchantRepository;
import com.app.ipo.repository.MerchantUpdateRepository;
import com.app.ipo.repository.UploadRepository;
import com.app.ipo.repository.UserRepository;
import com.app.ipo.service.MerchantService;

@Service
public class MerchantServiceImpl implements MerchantService
{
	public static final String SHEET = "Sheet1";
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	public MerchantRepository merchantRepository;
	
	@Autowired
	public UploadRepository uploadRepository;
	
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public MerchantUpdateRepository merchantUpdateRepository;
	
	@Value("${upload.file.path}")
	private String filePath;
	
	@Value("${db.data.limit}")
	private Integer dataLimit;
	
	@Value("${data.initial.status}")
	private String initialStatus;
	
	@Value("${data.approved.status}")
	private String approvedStatus;
	
	@Value("${data.rejected.status}")
	private String rejectedStatus;

	public Path root;
	
	@Override
	public void init() 
	{
	    try 
	    {
	    	root = Paths.get(filePath);
	    	
	    	Files.createDirectories(Paths.get(filePath));
	    }
	    catch (IOException e) 
	    {
	      throw new RuntimeException("Could not initialize folder for upload!");
	    }
	}
	
	@Override
	public String uploadFile(MultipartFile file, Model model, HttpSession session) 
	{
		String message = "";
		boolean recordStatus = Boolean.FALSE;
		
		try
		{
			Long userId = Long.parseLong(session.getAttribute("y").toString());
			
			if(null != file && null != file.getOriginalFilename() && !file.getOriginalFilename().toString().equalsIgnoreCase(""))
	    	{
	    		if(file.getOriginalFilename().toString().contains("."))
	    		{
	    			String[] fileparts = file.getOriginalFilename().toString().split("\\.");
	    			
	    			if(null != fileparts[1] && fileparts[1].equalsIgnoreCase("xlsx") || fileparts[1].equalsIgnoreCase("xls"))
	    			{
	    				if(file.getSize() > 0)
	    				{
	    					try 
	    					{
	    					    Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
	    					      
	    					    UploadDto objUploadDto = new UploadDto();
	  	    	    			objUploadDto.setUserId(userId);
	  	    	    			objUploadDto.setFile_name(fileparts[0]);
	  	    	    			objUploadDto.setFile_extension(fileparts[1]);
	  	    	    			objUploadDto.setMessage("");
	  	    	    			objUploadDto.setStatus(Boolean.FALSE);
	  	    	    			objUploadDto.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
	  	    	    			
	  	    	    			UploadEntity objUploadEntity = modelMapper.map(objUploadDto, UploadEntity.class);
	  	    	    			
	  	    	    			UploadEntity uploadEntity = uploadRepository.save(objUploadEntity);
	  	    	    			
	  	    	    			LinkedList<MerchantDto> objMerchantDtoList = doProcessUpload(file, userId, uploadEntity.getUploadId());
	  	    					
	  	    					if(null != objMerchantDtoList && !objMerchantDtoList.isEmpty())
	  	    					{
	  	    						for(MerchantDto merchantDto : objMerchantDtoList)
	  	    						{
	  	    							Optional<MerchantEntity> merchantEntity = merchantRepository.findByVPA(merchantDto.getVpa());
	  	    							
	  	    							if(merchantEntity.isPresent())
	  	    							{
	  	    								recordStatus = Boolean.TRUE;
	  	    								
	  	    								merchantDto.setRecordStatus(Boolean.TRUE);
	  	    							}
	  	    							else
	  	    							{
	  	    								merchantDto.setRecordStatus(Boolean.FALSE);
	  	    							}
	  	    						}
	  	    						
	  	    						if(!recordStatus)
	  	    						{
	  		    						List<MerchantEntity> merchantEntityList = objMerchantDtoList
	  		    								  .stream()
	  		    								  .map(user -> modelMapper.map(user, MerchantEntity.class))
	  		    								  .collect(Collectors.toList());
	  		    						
	  		    						merchantRepository.save(merchantEntityList);
	  		    						
	  		    						message = "Uploaded the file successfully!";
	  	    						}
	  	    						else
	  	    						{
	  	    							message = "The Uploaded file, have some existing records.";
	  	    						}
	  	    						
	  	    						try(FileInputStream excelFile = new FileInputStream(new File(filePath + file.getOriginalFilename())))
	  	    						{
	  	    							try(Workbook workbook = new XSSFWorkbook(excelFile))
	  	    							{
	  	    								Sheet sheet = workbook.getSheet(SHEET);
	  	    								
		  	    						    Iterator<Row> rows = sheet.iterator();

		  	    						    int rowNumber = 0;
		  	    						    
		  	    						    int count = 0;
		  	    						  
		  	    						    while (rows.hasNext())
		  	    						    {
		  	    						        Row currentRow = rows.next();
		  	    						        
			  	    						    if (rowNumber == 0)
			  	    							{
			  	    						    	CellStyle headerStyle = workbook.createCellStyle();
			  	    						    	headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
			  	    						    	
			  	    						    	Cell headerCell = currentRow.createCell(currentRow.getLastCellNum());
			  	    						    	headerCell.setCellValue("Reason");
			  	    						    	headerCell.setCellStyle(headerStyle);
			  	    						    				  	    						    	 
			  	    						    	rowNumber++;
			  	    						    	count++;
			  	    						    	continue;
			  	    							}
			  	    						    
			  	    						    Iterator<Cell> cellsInRow = currentRow.iterator();
				  	    						
			  	    						    int cellIdx = 0;
			  	    						  
			  	    						    while (cellsInRow.hasNext()) 
			  	    						    {
			  	    						    	@SuppressWarnings("unused")
													Cell currentCell = cellsInRow.next();
			  	    						    	
			  	    						    	if(cellIdx == 23)
			  	    						        {
			  	    						    		Cell cell = currentRow.createCell(24);
			  	    						    		
			  	    						    		int c = (count - 1);
			  	    						    		
			  	    						    		System.out.println("Count:"+ c);
			  	    						    		
			  	    						    		if(objMerchantDtoList.get(c).getRecordStatus())
				  	  	  	    						{
			  	    						    			cell.setCellValue("Record already present in our database.");	
				  	  	  	    						}
			  	    						    		else
			  	    						    		{
			  	    						    			cell.setCellValue("Record looks fine.");	
			  	    						    		}
			  	    						        }
			  	    						    	
			  	    						    	cellIdx++;		
			  	    						    }
			  	    						    
			  	    						    count++;
		  	    						    }
			  	    						    
		  	    						    try(FileOutputStream out = new FileOutputStream(filePath + file.getOriginalFilename()))
		  	    						    {
		  	    						    	workbook.write(out);
		  	    						    }
		  	    						    catch (Exception e) 
		  	    						    {
		  	    						    	e.printStackTrace();
		  	    						    }
	  	    							}
	  	    							catch (Exception e) 
		  	    						{
											e.printStackTrace();
										}
	  	    						}
	  	    						catch (Exception e) 
	  	    						{
										e.printStackTrace();
									}
	  	    					}
	  	    					else
	  	    					{
	  	    						message = "No Record Found!";
	  	    					}
	  	    					
	  	    					if(uploadEntity.getUploadId() > 0)
	  	    	    			{
	  	    	    				objUploadDto.setUploadId(uploadEntity.getUploadId());
	  	    	    				objUploadDto.setMessage(message);
	  	    	    				
	  	    	    				if(!recordStatus)
	  	    						{
	  	    	    					objUploadDto.setStatus(Boolean.TRUE);
	  	    						}
	  	    	    				else
	  	    	    				{
	  	    	    					objUploadDto.setStatus(Boolean.FALSE);
	  	    	    				}
	  	    	        			
	  	    	        			objUploadDto.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
	  	    	        			
	  	    	        			objUploadEntity = modelMapper.map(objUploadDto, UploadEntity.class);
	  	    	        			
	  	    	        			uploadRepository.saveAndFlush(objUploadEntity);
	  	    	    			}
	    					} 
	    					catch (Exception e)
	    					{
	    					      if (e instanceof FileAlreadyExistsException) 
	    					      {
	    					    	  message = "The Uploaded file of that name already exists.";
	    					      }
	    					}
	    				}
	    				else
	    				{
	    					 message = "Please verify the upload file, it's empty.";
	    				}
	    			}
	    			else
	    			{
	    				 message = "Kindly upload xlsx or xls file only.";
	    			}
	    		}
	    		else
	    		{
	    			 message = "Uploaded file name should contain file extension.";
	    		}
	    	}
	    	else
	    	{
	    		 message = "Kindly choose the mandatory file to upload.";
	    	}
		}
		catch (Exception e) 
	    {
			e.printStackTrace();
			
	    	message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
	    }
		
		return message;
	}

	private LinkedList<MerchantDto> doProcessUpload(MultipartFile file, Long userId, Long uploadId) 
	{
		LinkedList<MerchantDto> objMerchantDtoList = new LinkedList<MerchantDto>();
		
		try(Workbook workbook = new XSSFWorkbook(file.getInputStream()))
		{
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			int rowNumber = 0;
			
			while (rows.hasNext()) 
			{
			  Row currentRow = rows.next();
			  
			  if (rowNumber == 0)
			  {
				  rowNumber++;
				  continue;
			  }
			
			  Iterator<Cell> cellsInRow = currentRow.iterator();
			
			  MerchantDto objMerchantDto = new MerchantDto();
			  
			  int cellIdx = 0;
			  
			  while (cellsInRow.hasNext()) 
			  {
			     Cell currentCell = cellsInRow.next();
			
			     switch (cellIdx)
			     {
		          	case 0:
		        	  objMerchantDto.setName(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
		            break;
		            
		          	case 1:
		          		objMerchantDto.setMarketing_name(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 2:
		          		objMerchantDto.setAddress(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 3:
		          		objMerchantDto.setCity(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 4:
		          		objMerchantDto.setState(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 5:

		          		if (currentCell.getCellType() == CellType.NUMERIC) 
		          		{
		          			objMerchantDto.setPincode(currentCell.getNumericCellValue() > 0 ? (long) currentCell.getNumericCellValue() + ""  : "0");
		          		}
		          		else if (currentCell.getCellType() == CellType.STRING) 
		          		{
		          			objMerchantDto.setPincode(null != currentCell.getStringCellValue() ? Long.parseLong(currentCell.getStringCellValue()) + "" : "");
		          		}
		          		
			        break;
			        
		          	case 6:
		          		
		          		if (currentCell.getCellType() == CellType.NUMERIC) 
		          		{
		          			objMerchantDto.setMobile_no(currentCell.getNumericCellValue() > 0 ? (long) currentCell.getNumericCellValue() + ""  : "0");
		          		}
		          		else if (currentCell.getCellType() == CellType.STRING) 
		          		{
		          			objMerchantDto.setMobile_no(null != currentCell.getStringCellValue() ? Long.parseLong(currentCell.getStringCellValue()) + "" : "");
		          		}
		          		
			        break;
			        
		          	case 7:
		          		objMerchantDto.setLandline_no(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 8:
		          		objMerchantDto.setDirector(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 9:
		          		objMerchantDto.setCategory(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 10:
		          		
		          		if (currentCell.getCellType() == CellType.NUMERIC) 
		          		{
		          			objMerchantDto.setMcc(currentCell.getNumericCellValue() > 0 ? (long) currentCell.getNumericCellValue() + ""  : "0");
		          		}
		          		else if (currentCell.getCellType() == CellType.STRING) 
		          		{
		          			objMerchantDto.setMcc(null != currentCell.getStringCellValue() ? Long.parseLong(currentCell.getStringCellValue()) + "" : "");
		          		}
		          		
			        break;
			        
		          	case 11:
		          		objMerchantDto.setMcc_desc(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");		          		
			        break;
			        
		          	case 12:
		          		objMerchantDto.setType_of_integration(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 13:
		          		objMerchantDto.setVpa(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 14:
		          		objMerchantDto.setGst_mumber(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 15:
		          		objMerchantDto.setPan(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 16:
		          		objMerchantDto.setMerchant_official_id(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 17:
		          		objMerchantDto.setMerchant_website(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 18:
		          		objMerchantDto.setBank_name(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 19:
		          		objMerchantDto.setIfsc_code(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 20:
		          		objMerchantDto.setNodal_account_no(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 21:
		          		objMerchantDto.setPool_account_no(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 22:
		          		objMerchantDto.setPool_ifsc_code(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
			        
		          	case 23:
		          		objMerchantDto.setSym(null != currentCell.getStringCellValue() ? currentCell.getStringCellValue() : "");
			        break;
		            
		          	default:
		                break;		              
			     }
			  	
			     cellIdx++;		
			     
			     objMerchantDto.setUserId(userId);
			     objMerchantDto.setUploadId(uploadId);
			     objMerchantDto.setStatus(initialStatus);
			     objMerchantDto.setReason("");
			     objMerchantDto.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
			  }
			  
			  objMerchantDtoList.add(objMerchantDto);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return objMerchantDtoList;
	}
	
	@Override
	public Resource load(String filename) 
	{
	    try 
	    {
	      Path file = root.resolve(filename);
	      
	      Resource resource = new UrlResource(file.toUri());

	      if (resource.exists() || resource.isReadable()) 
	      {
	        return resource;
	      } 
	      else 
	      {
	        throw new RuntimeException("Could not read the file!");
	      }
	    }
	    catch (MalformedURLException e) 
	    {
	      throw new RuntimeException("Error: " + e.getMessage());
	    }
	}

	@Override
	public String viewUploadedFiles(UploadDto uploadDto, Model model, HttpSession session) 
	{
		Long userId = Long.parseLong(session.getAttribute("y").toString());
		
		List<UploadEntity> objUploadEntity = uploadRepository.findByUserID(userId, dataLimit);
		
		if(null != objUploadEntity)
		{
			List<UploadDto> uploadDtoList = objUploadEntity
					  .stream()
					  .map(user -> modelMapper.map(user, UploadDto.class))
					  .collect(Collectors.toList());
			
			for(UploadDto upload: uploadDtoList)
			{
				Optional<UserEntity> userEntity = userRepository.findByUserId(upload.getUserId());
				
				if(userEntity.isPresent())
				{
					upload.setFile_full_name(upload.getFile_name() + "." + upload.getFile_extension());
					upload.setUser_name(userEntity.get().getName());
				} 
			}
			
			model.addAttribute("uploads", uploadDtoList);
		}
		
		return "viewFiles";
	}

	@Override
	public String searchMerchant(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session) 
	{
		List<MerchantEntity> objMerchantEntity = null;
		
		if(null != merchantDto && null != merchantDto.getVpa() && !merchantDto.getVpa().equalsIgnoreCase(""))
		{
			objMerchantEntity = merchantRepository.findMerchantByVPA(merchantDto.getVpa());
		}
		else
		{
			objMerchantEntity = merchantRepository.findAllMerchant(dataLimit);
		}
		
		List<MerchantDto> merchantDtoList = objMerchantEntity
				  .stream()
				  .map(user -> modelMapper.map(user, MerchantDto.class))
				  .collect(Collectors.toList());
					
		model.addAttribute("merchants", merchantDtoList);
		
		return "searchMerchant";
	}

	@Override
	public String viewMerchant(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		List<MerchantEntity> objMerchantEntity = merchantRepository.findAllMerchantByStatus(initialStatus, dataLimit);
		
		List<MerchantDto> merchantDtoList = objMerchantEntity
				  .stream()
				  .map(user -> modelMapper.map(user, MerchantDto.class))
				  .collect(Collectors.toList());
					
		model.addAttribute("merchants", merchantDtoList);
		
		return "viewMerchant";
	}

	@Override
	public String recordProcess(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		if(null != merchantDto && (null != merchantDto.getCheckedData() && !merchantDto.getCheckedData().equalsIgnoreCase(""))) 
		{
			String [] merchantIds = merchantDto.getCheckedData().split(",");
			
			for(String merchantId: merchantIds)
			{
				Optional<MerchantEntity> objMerchantEntity = merchantRepository.findMerchantById(Long.valueOf(merchantId));
				
				if(objMerchantEntity.isPresent())
				{
					MerchantEntity merchant = objMerchantEntity.get();
					merchant.setStatus(merchantDto.getOperation());
					merchant.setReason(null != merchantDto.getReason() ? merchantDto.getReason() : "");
					
					merchantRepository.saveAndFlush(merchant);
				}
			}			
		}
		else
		{
			 ObjectError error = new ObjectError("globalError", "Kindly choose atleast any one of the records to perform this operation.");
		     result.addError(error);
		     
		     merchantDto.setReason("");
		}
		
		List<MerchantEntity> objMerchantEntity = merchantRepository.findAllMerchantByStatus(initialStatus, dataLimit);
		
		List<MerchantDto> merchantDtoList = objMerchantEntity
				  .stream()
				  .map(user -> modelMapper.map(user, MerchantDto.class))
				  .collect(Collectors.toList());
					
		model.addAttribute("merchants", merchantDtoList);
		
		return "viewMerchant";
	}

	@Override
	public String viewProcessed(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session) 
	{
		List<String> operation = Arrays.asList(approvedStatus, rejectedStatus);
		
		List<MerchantEntity> objMerchantEntity = merchantRepository.findAllMerchantByProcessedStatus(operation, dataLimit);
		
		List<MerchantDto> merchantDtoList = objMerchantEntity
				  .stream()
				  .map(user -> modelMapper.map(user, MerchantDto.class))
				  .collect(Collectors.toList());
					
		model.addAttribute("merchants", merchantDtoList);
		
		return "viewProcessed";
	}

	@Override
	public String updateMerchantData(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session) 
	{
		List<String> operation = Arrays.asList(approvedStatus, rejectedStatus);
		
		List<MerchantEntity> objMerchantEntity = merchantRepository.findAllMerchantByProcessedStatus(operation, dataLimit);
		
		List<MerchantDto> merchantDtoList = objMerchantEntity
				  .stream()
				  .map(user -> modelMapper.map(user, MerchantDto.class))
				  .collect(Collectors.toList());
		
		List<MerchantUpdateEntity> objMerchantUpdateEntity = merchantUpdateRepository.findAllMerchantUpdateByStatus(Arrays.asList(initialStatus));
		
		for(MerchantUpdateEntity merchantUpdateEntity : objMerchantUpdateEntity)
		{
			merchantDtoList = merchantDtoList.stream().filter(x -> x.getMerchantId() != merchantUpdateEntity.getMerchantId()).collect(Collectors.toList());
		}
					
		model.addAttribute("merchants", merchantDtoList);
		
		return "updateMerchantData";
	}
	
	@Override
	public String update(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session) 
	{
		Long userId = Long.parseLong(session.getAttribute("y").toString());
		
		if(null != merchantDto && (null != merchantDto.getCheckedData() && !merchantDto.getCheckedData().equalsIgnoreCase(""))) 
		{
			String [] merchantIds = merchantDto.getCheckedData().split(",");
			
			for(String merchantId: merchantIds)
			{
				Optional<MerchantEntity> objMerchantEntity = merchantRepository.findMerchantById(Long.valueOf(merchantId));
				
				if(objMerchantEntity.isPresent())
				{
					MerchantUpdateEntity objMerchantUpdateEntity = new MerchantUpdateEntity();
					objMerchantUpdateEntity.setMerchantId(objMerchantEntity.get().getMerchantId());
					objMerchantUpdateEntity.setRequestedUserId(userId);
					objMerchantUpdateEntity.setReason(null != merchantDto.getReason() ? merchantDto.getReason() : "");
					objMerchantUpdateEntity.setStatus(initialStatus);
					objMerchantUpdateEntity.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
					
					merchantUpdateRepository.saveAndFlush(objMerchantUpdateEntity);
				}
			}
			
			merchantDto.setReason("");
			 
			ObjectError error = new ObjectError("globalError", "Your Request submitted successfully!");
		    result.addError(error);
		    
		    List<String> operation = Arrays.asList(approvedStatus, rejectedStatus);
			
			List<MerchantEntity> objMerchantEntity = merchantRepository.findAllMerchantByProcessedStatus(operation, dataLimit);
			
			List<MerchantDto> merchantDtoList = objMerchantEntity
					  .stream()
					  .map(user -> modelMapper.map(user, MerchantDto.class))
					  .collect(Collectors.toList());
			
			List<MerchantUpdateEntity> objMerchantUpdateEntity = merchantUpdateRepository.findAllMerchantUpdateByStatus(Arrays.asList(initialStatus));
			
			for(MerchantUpdateEntity merchantUpdateEntity : objMerchantUpdateEntity)
			{
				merchantDtoList = merchantDtoList.stream().filter(x -> x.getMerchantId() != merchantUpdateEntity.getMerchantId()).collect(Collectors.toList());
			}
						
			model.addAttribute("merchants", merchantDtoList);
		}
		else
		{
			 ObjectError error = new ObjectError("globalError", "Kindly choose atleast any one of the records to perform this operation.");
		     result.addError(error);
		     
		     merchantDto.setReason("");
		}
		
		return "updateMerchantData";
	}

	@Override
	public String viewUpdated(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session) 
	{
		List<String> operation = Arrays.asList(approvedStatus, rejectedStatus);
			
		List<MerchantEntity> objMerchantEntity = merchantRepository.findAllMerchantByProcessedStatus(operation, dataLimit);
		
		for(MerchantEntity merchantEntity : objMerchantEntity)
		{
			Long count = 0L;
			
			Optional<BigInteger> objMerchantUpdateEntity = merchantUpdateRepository.findAllMerchantUpdateCount(Arrays.asList(initialStatus, approvedStatus, rejectedStatus), merchantEntity.getMerchantId());
			
			if(objMerchantUpdateEntity.isPresent())
			{				
				count = count + objMerchantUpdateEntity.get().longValue();
				
				merchantEntity.setCount(count);
				
				Optional<MerchantUpdateEntity> merchantUpdateEntity = merchantUpdateRepository.findAllMerchantUpdateStatus(Arrays.asList(initialStatus, approvedStatus, rejectedStatus), merchantEntity.getMerchantId());
				
				if(objMerchantUpdateEntity.isPresent())
				{
					merchantEntity.setUpdatedStatus(merchantUpdateEntity.get().getStatus());
				}
			}
			else
			{
				merchantEntity.setCount(count);
			}
		}
		
		List<MerchantDto> merchantDtoList = objMerchantEntity
				  .stream()
				  .map(user -> modelMapper.map(user, MerchantDto.class))
				  .collect(Collectors.toList());
		
		merchantDtoList = merchantDtoList.stream().filter(x -> x.getCount() > 0).collect(Collectors.toList());
			
		model.addAttribute("merchants", merchantDtoList);
		
		return "viewUpdated";
	}

	@Override
	public String viewRequest(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		List<MerchantDto> merchantDtoList = new ArrayList<>();
				
		List<MerchantUpdateEntity> objMerchantUpdateEntity = merchantUpdateRepository.findAllMerchantUpdateByStatus(Arrays.asList(initialStatus));
		
		for(MerchantUpdateEntity merchantUpdateEntity : objMerchantUpdateEntity)
		{
			Optional<MerchantEntity> objMerchantEntity = merchantRepository.findMerchantById(Long.valueOf(merchantUpdateEntity.getMerchantId()));
			
			if(objMerchantEntity.isPresent())
			{
				MerchantDto objMerchantDto = modelMapper.map(objMerchantEntity.get(), MerchantDto.class);
				objMerchantDto.setReason(merchantUpdateEntity.getReason());
				
				merchantDtoList.add(objMerchantDto);
			}
		}
		
		model.addAttribute("merchants", merchantDtoList);
		
		return "viewRequest";
	}

	@Override
	public String processRequest(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		Long userId = Long.parseLong(session.getAttribute("y").toString());
		
		if(null != merchantDto && (null != merchantDto.getCheckedData() && !merchantDto.getCheckedData().equalsIgnoreCase(""))) 
		{
			String [] merchantIds = merchantDto.getCheckedData().split(",");
			
			for(String merchantId: merchantIds)
			{
				Optional<MerchantEntity> objMerchantEntity = merchantRepository.findMerchantById(Long.valueOf(merchantId));
				
				if(objMerchantEntity.isPresent())
				{
					List<String> operation = Arrays.asList(initialStatus);
					
					Optional<MerchantUpdateEntity> objMerchantUpdateEntity = merchantUpdateRepository.findMerchantUpdateByMerchantId(Long.valueOf(merchantId), operation);
					
					MerchantUpdateEntity merchantUpdateEntity = objMerchantUpdateEntity.get();
					merchantUpdateEntity.setMerchantUpdateId(merchantUpdateEntity.getMerchantUpdateId());
					merchantUpdateEntity.setApproverUserId(userId);
					merchantUpdateEntity.setStatus(merchantDto.getOperation());
					merchantUpdateEntity.setUpdatedDate(java.sql.Date.valueOf(LocalDate.now()));
					
					merchantUpdateRepository.saveAndFlush(merchantUpdateEntity);
				}
			}
		    
		    List<MerchantDto> merchantDtoList = new ArrayList<>();
			
			List<MerchantUpdateEntity> objMerchantUpdateEntity = merchantUpdateRepository.findAllMerchantUpdateByStatus(Arrays.asList(initialStatus));
			
			for(MerchantUpdateEntity merchantUpdateEntity : objMerchantUpdateEntity)
			{
				Optional<MerchantEntity> objMerchantEntity = merchantRepository.findMerchantById(Long.valueOf(merchantUpdateEntity.getMerchantId()));
				
				if(objMerchantEntity.isPresent())
				{
					MerchantDto objMerchantDto = modelMapper.map(objMerchantEntity.get(), MerchantDto.class);
					objMerchantDto.setReason(merchantUpdateEntity.getReason());
					
					merchantDtoList.add(objMerchantDto);
				}
			}
			
			model.addAttribute("merchants", merchantDtoList);
			
			ObjectError error = new ObjectError("globalError", "Your Request submitted successfully!");
		    result.addError(error);
		}
		else
		{
			 ObjectError error = new ObjectError("globalError", "Kindly choose atleast any one of the records to perform this operation.");
		     result.addError(error);
		     
		     merchantDto.setReason("");
		}
		
		return "viewRequest";
	
	}

	@Override
	public String verify(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session) 
	{
		if(null != merchantDto && null != merchantDto.getVpa() && null != merchantDto.getMerchantOption())
		{
			if(merchantDto.getMerchantOption().equalsIgnoreCase("Add For Both NSE and BSE"))
			{
				if(merchantDto.getVpa().contains("@"))
				{
					String [] vpaparts = merchantDto.getVpa().split("@");
					
					String name = vpaparts[0].substring(vpaparts[0].length()-3, vpaparts[0].length());
					
					if(name.equalsIgnoreCase("NSE") || name.equalsIgnoreCase("BSE"))
					{
						Optional<MerchantEntity> merchantEntity = merchantRepository.findByVPA(merchantDto.getVpa());
						
						if(merchantEntity.isPresent())
						{
							ObjectError error = new ObjectError("globalError", "Kindly choose different VPA, The entered VPA is already present in our system.");
						    result.addError(error);
						}	
						else
						{
							ObjectError error = new ObjectError("globalError", "You can option, this valid VPA.");
						    result.addError(error);
						}
					}
					else
					{
						boolean con = Boolean.FALSE;
								
						name = vpaparts[0] + "nse" + "@" + vpaparts[1];
						
						Optional<MerchantEntity> merchantEntity = merchantRepository.findByVPA(name);
						
						if(merchantEntity.isPresent())
						{
							con = Boolean.TRUE;
						}
						
						name = vpaparts[0] + "bse" + "@" + vpaparts[1];
						
						merchantEntity = merchantRepository.findByVPA(name);
						
						if(merchantEntity.isPresent())
						{
							con = Boolean.TRUE;
						}
						
						name = vpaparts[0] + "NSE" + "@" + vpaparts[1];
						
						merchantEntity = merchantRepository.findByVPA(name);
						
						if(merchantEntity.isPresent())
						{
							con = Boolean.TRUE;
						}
						
						name = vpaparts[0] + "BSE" + "@" + vpaparts[1];
						
						merchantEntity = merchantRepository.findByVPA(name);
						
						if(merchantEntity.isPresent())
						{
							con = Boolean.TRUE;
						}
						
						if(!con)
						{
							ObjectError error = new ObjectError("globalError", "You can option, this valid VPA.");
						    result.addError(error);
						}
						else
						{
							ObjectError error = new ObjectError("globalError", "Kindly choose different VPA, The entered VPA is already present in our system.");
						    result.addError(error);
						}
					}
				}
				else
				{
					ObjectError error = new ObjectError("globalError", "Kindly an enter valid VPA.");
				    result.addError(error);
				}
			}
			else
			{
				Optional<MerchantEntity> merchantEntity = merchantRepository.findByVPA(merchantDto.getVpa());
					
				if(merchantEntity.isPresent())
				{
					ObjectError error = new ObjectError("globalError", "Kindly choose different VPA, The entered VPA is already present in our system.");
				    result.addError(error);
				}
				else
				{
					ObjectError error = new ObjectError("globalError", "You can option, this valid VPA.");
				    result.addError(error);
				}
			}			
		}
		else
		{
			 ObjectError error = new ObjectError("globalError", "Kindly enter add merchant option and VPA to verify VPA.");
		     result.addError(error);
		}
	     
		return "addMerchant";
	}

	@Override
	public String add(MerchantDto merchantDto, BindingResult result, Model model, HttpSession session)
	{
		Long userId = Long.parseLong(session.getAttribute("y").toString());
		
		if(null != merchantDto 
				&& (null != merchantDto.getName() && !merchantDto.getName().equalsIgnoreCase(""))
				&& (null != merchantDto.getMarketing_name() && !merchantDto.getMarketing_name().equalsIgnoreCase(""))
				&& (null != merchantDto.getAddress() && !merchantDto.getAddress().equalsIgnoreCase(""))
				&& (null != merchantDto.getCity() && !merchantDto.getCity().equalsIgnoreCase(""))
				&& (null != merchantDto.getState() && !merchantDto.getState().equalsIgnoreCase(""))
				&& (null != merchantDto.getPincode() && !merchantDto.getPincode().equalsIgnoreCase(""))
				&& (null != merchantDto.getDirector() && !merchantDto.getDirector().equalsIgnoreCase(""))
				&& (null != merchantDto.getMcc() && !merchantDto.getMcc().equalsIgnoreCase(""))
				&& (null != merchantDto.getMcc_desc() && !merchantDto.getMcc_desc().equalsIgnoreCase(""))
				&& (null != merchantDto.getType_of_integration() && !merchantDto.getType_of_integration().equalsIgnoreCase(""))
				&& (null != merchantDto.getGst_mumber() && !merchantDto.getGst_mumber().equalsIgnoreCase(""))
				&& (null != merchantDto.getPan() && !merchantDto.getPan().equalsIgnoreCase(""))
				&& (null != merchantDto.getIfsc_code() && !merchantDto.getIfsc_code().equalsIgnoreCase(""))
				&& (null != merchantDto.getNodal_account_no() && !merchantDto.getNodal_account_no().equalsIgnoreCase(""))
				&& (null != merchantDto.getVpa() && !merchantDto.getVpa().equalsIgnoreCase(""))
				&& null != merchantDto.getMerchantOption())
		{
			UploadDto objUploadDto = new UploadDto();
   			objUploadDto.setUserId(userId);
   			objUploadDto.setFile_name("-");
   			objUploadDto.setFile_extension("-");
   			objUploadDto.setMessage("Uploaded By Merchant Creation Form");
   			objUploadDto.setStatus(Boolean.TRUE);
   			objUploadDto.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
   			
   			UploadEntity objUploadEntity = modelMapper.map(objUploadDto, UploadEntity.class);
   			
   			UploadEntity uploadEntity = uploadRepository.save(objUploadEntity);
   			
   			if(null != uploadEntity && uploadEntity.getUploadId() > 0)
   			{
   				MerchantEntity merchantEntity = modelMapper.map(merchantDto, MerchantEntity.class);
   				merchantEntity.setUserId(userId);
   				merchantEntity.setUploadId(uploadEntity.getUploadId());
   				merchantEntity.setStatus(initialStatus);
   				merchantEntity.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));
   				
   				if(merchantDto.getMerchantOption().equalsIgnoreCase("Add For Both NSE and BSE"))
   				{
   					if(merchantDto.getVpa().contains("@"))
   					{
   						String [] vpaparts = merchantDto.getVpa().split("@");
   						
   						String name = vpaparts[0].substring(vpaparts[0].length()-3, vpaparts[0].length());
   						
   						if(name.equalsIgnoreCase("NSE") || name.equalsIgnoreCase("BSE"))
   						{
   							merchantEntity.setVpa(merchantDto.getVpa());
   							
   							merchantRepository.saveAndFlush(merchantEntity);
   						}
   						else
   						{
   							name = vpaparts[0] + "NSE" + "@" + vpaparts[1];
   							
   							merchantEntity.setVpa(name);
   							
   							MerchantEntity merchant = merchantRepository.saveAndFlush(merchantEntity);
   							
   							if(null != merchant)
   							{
   								name = vpaparts[0] + "BSE" + "@" + vpaparts[1];
   								
   								MerchantEntity merchantsEntity = modelMapper.map(merchantDto, MerchantEntity.class);
   								merchantsEntity.setMerchantId(merchant.getMerchantId() + 1);
   								merchantsEntity.setUserId(userId);
   								merchantsEntity.setUploadId(uploadEntity.getUploadId());
   								merchantsEntity.setStatus(initialStatus);
   								merchantsEntity.setRecordedDate(java.sql.Date.valueOf(LocalDate.now()));   								
   								merchantsEntity.setVpa(name);
   	   							
   	   							merchantRepository.saveAndFlush(merchantsEntity);
   							}
   						}
   					}
   				}
   				else
   				{
   					merchantEntity.setVpa(merchantDto.getVpa());
   					
   					merchantRepository.saveAndFlush(merchantEntity);
   				}
   				
   				ObjectError error = new ObjectError("globalError", "Registered successfully!");
   		        result.addError(error);
   			}
   			else
   			{
   				ObjectError error = new ObjectError("globalError", "Your request failed, Due to technical issue. Please try again later!");
   		        result.addError(error);
   			}
		}
		else
		{
			ObjectError error = new ObjectError("globalError", "Kindly fill all mandatory fields!");
	        result.addError(error);
		}
		
		merchantDto.setName("");
		merchantDto.setMarketing_name("");
		merchantDto.setAddress("");
		merchantDto.setCity("");
		merchantDto.setState("");
		merchantDto.setPincode("");
		merchantDto.setDirector("");
		merchantDto.setMcc("");
		merchantDto.setMcc_desc("");
		merchantDto.setType_of_integration("");
		merchantDto.setGst_mumber("");
		merchantDto.setPan("");
		merchantDto.setIfsc_code("");
		merchantDto.setNodal_account_no("");
		merchantDto.setVpa("");
		 		
		return "addMerchant";
	}
}