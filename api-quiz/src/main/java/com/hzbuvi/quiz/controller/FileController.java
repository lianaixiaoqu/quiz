package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.config.ConstantData;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.organization.service.OrganizationService;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.quiz.questionbank.service.QuestionImport;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by WANG, RUIQING on 11/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
public class FileController {

	public static String path= ConstantData.uploadFilePath ;

	@Autowired
	private QuestionImport questionImport;
	@Autowired
	OrganizationService organizationService;
	@Autowired
	OrganizationUserService organizationUserService;


	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile uploadFile , String uploadType , HttpServletRequest request, HttpServletResponse response) {

		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}

		String type = "";
		if (uploadFile.getOriginalFilename().lastIndexOf(".") != -1) {
			type = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".")+1).toLowerCase() ;
		}
		try {
			String filePath = path+System.currentTimeMillis()+"."+type;
			uploadFile.transferTo(new File(filePath));

			switch (uploadType) {
				case "questionbank" :
					questionImport.importFromExcel(filePath);
					break;
				case "orgnization" :
					organizationService.inputOrganizationFromExcel(filePath);
					break;
				case "orgnizationuser" :
					organizationUserService.inputFromExcel(filePath);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("import","OK");
    }
}
