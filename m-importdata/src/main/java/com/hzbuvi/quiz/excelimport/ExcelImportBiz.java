package com.hzbuvi.quiz.excelimport;

import com.hzbuvi.quiz.excelimport.entity.ExcelImport;
import com.hzbuvi.quiz.excelimport.entity.ExcelImportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class ExcelImportBiz {

	@Autowired
	private ExcelImportDao excelImportDao;

	public List<Map<String,Object>>  getData(String excelFilePath,Class clz){
		ExcelImport excelImport = excelImportDao.findByClassName(clz.getName());
		return MSExcel.parseExcel(excelFilePath,excelImport.getKeyName(),excelImport.getKeyType(),excelImport.getStartRow(),excelImport.getStartCol());
	}

}
