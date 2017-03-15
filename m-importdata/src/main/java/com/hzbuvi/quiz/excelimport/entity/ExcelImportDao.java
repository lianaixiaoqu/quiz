package com.hzbuvi.quiz.excelimport.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface ExcelImportDao extends JpaRepository<ExcelImport,Integer> {
	ExcelImport findByClassName(String className);
}
