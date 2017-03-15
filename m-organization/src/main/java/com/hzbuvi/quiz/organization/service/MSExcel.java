package com.hzbuvi.quiz.organization.service;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;



/**   Twitter : @taylorwang789 
 * Creat time : Apr 20, 2016    10:28:53 PM
 */
public class MSExcel {
	
	
	/**   Twitter : @taylorwang789 
	 * Creat time : Apr 20, 2016    11:28:59 PM
	 * @param excelFilePath
	 * @param keyName  id,name,age,amt,cnt,createdate
	 * @param keyType  number,string,number,number,number,date
	 * @param startRow  start on row 3
	 * @param startCol  start on col 4
	 * @return
	 */
	public static List<Map<String,Object>> parseExcel(String excelFilePath,String keyName, String keyType , int startRow , int startCol){
        
		String key[] = keyName.split(",");
		String type[] = keyType.split(",");
		String typeOrg[] = keyType.split(",");

		 FileInputStream inputStream;
		 Workbook workbook = null;
		try {
			   inputStream = new FileInputStream(new File(excelFilePath));
		       if (excelFilePath.toLowerCase().endsWith("xlsx")) {
		            workbook = new XSSFWorkbook(inputStream);
		        } else if (excelFilePath.toLowerCase().endsWith("xls")) {
		            workbook = new HSSFWorkbook(inputStream);
		        } else {
		        	System.out.println("not excel file ");
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}

		List<Map<String,Object>> workbook_lsit = new ArrayList<>();
		
		 //  sheets 
		 for(int i=0;i<workbook.getNumberOfSheets();i++){
		     Sheet firstSheet = workbook.getSheetAt(i);
		     Iterator<Row> rowIterator = firstSheet.iterator();

		     for(int j=1;j<startRow;j++){
		    	 rowIterator.next();
		     }
    	     
		     // loop  rows 
		     while (rowIterator.hasNext()) {
		    	 Map<String,Object> rowMap = new HashMap<>();
		    	 List<Boolean> flag = new ArrayList<>();
    	         Row row = rowIterator.next();
    	         Iterator<Cell> cellIterator = row.cellIterator();

    	        for(int l=0;l<startCol;l++){
    	        	cellIterator.next();
    	        }

    	        // cells 
    	        for(int k=0;k<type.length;k++){

    	        	  if(type[k].equals("skip")){
    	        		  cellIterator.next();
    	        		  continue;
    	        	  }

    	        	  Cell cell ;
    	        	  String currentType = type[k];
    	        	  
    	        	  if(currentType.startsWith("date")){
    	        		  if(currentType.equals("date")){
    	        			  	cell = cellIterator.next();
      						  	Date date ;
      						  	if( HSSFDateUtil.isCellDateFormatted(cell)  ){
      						  		date = cell.getDateCellValue();
      						  		rowMap.put(key[k], date );
      						  	}else{
      						  		rowMap.put(key[k] , null );
      					      		 flag.add(false);
      						  	}
    	        		  }else if(currentType.equals("datesql")){
    	        			  	cell = cellIterator.next();
      						  	java.sql.Date datesql;
      						  	if( HSSFDateUtil.isCellDateFormatted(cell)  ){
      						  		datesql = new java.sql.Date(cell.getDateCellValue().getTime());
      						  		rowMap.put(key[k], datesql );
      						  	}else{
      						  		rowMap.put(key[k] , null );
      					      		 flag.add(false);
      						  	}
    	        		  }else{ 
    	        			  String  dateType = currentType.substring(currentType.indexOf("_")+1);
    	        			  SimpleDateFormat sdf = new SimpleDateFormat(dateType);
    	        			  cell = cellIterator.next();
    						  Date date ;
    						  if( HSSFDateUtil.isCellDateFormatted(cell)  ){
    						  		date = cell.getDateCellValue();
    						  		rowMap.put(key[k] , "'"+sdf.format(date)+"'" );
    						  }else{
      						  		rowMap.put(key[k] , null );
      					      		 flag.add(false);
    						  }
    	        			  
    	        		  }
    	        		  
    	        	  }else{
    	        		  
    	        	  
    	        	  switch (currentType) {
    					case "number":
    						cell = cellIterator.next();
    					     if(cell.getCellType() == Cell.CELL_TYPE_STRING){
    					    	 rowMap.put( key[k] ,  Integer.parseInt(cell.getStringCellValue()));
    					     }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
    					    	 rowMap.put( key[k] ,  cell.getNumericCellValue());
    					     }else{
    					    	 rowMap.put( key[k] ,  0);
    					    	 flag.add(false);
    					     }
    						break;
    					case "string":
    						cell = cellIterator.next();
    						 if(cell.getCellType() == Cell.CELL_TYPE_STRING){
    					    	 rowMap.put( key[k] ,  cell.getStringCellValue());
    					     }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
    					    	 rowMap.put( key[k] ,  cell.getNumericCellValue()+"");
    					     }else{
    					    	 rowMap.put( key[k] ,  " " );
    					    	 flag.add(false);
    					     }
    						break;
    					case "boolean":
    						cell = cellIterator.next();
    						 if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
    							 rowMap.put(key[k], cell.getBooleanCellValue());
    					     }else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
    					    	 rowMap.put( key[k] ,  cell.getStringCellValue() );
    					     }else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
    					    	 double  num = cell.getNumericCellValue();
    					    	 if(num==0){
    					    		 rowMap.put( key[k] , false );
    					    	 }else{
    					    		 rowMap.put( key[k] , true );
    					    	 }
    					     }else{
    					    	 rowMap.put( key[k] ,  false );
    					    	 flag.add(false);
    					     }
    						break;
//    					case "date":
//    						cell = cellIterator.next();
//    						Date date ;
//    						if( HSSFDateUtil.isCellDateFormatted(cell)  ){
//    							date = cell.getDateCellValue();
//    							rowMap.put(key[k], date );
//    						}else{
//    							rowMap.put(key[k] , null );
//    					    	 flag.add(false);
//    						}
//    						break;
//    					case "datesql":
//    						cell = cellIterator.next();
//    						java.sql.Date datesql;
//    						if( HSSFDateUtil.isCellDateFormatted(cell)  ){
//    							datesql = new java.sql.Date(cell.getDateCellValue().getTime());
//    							rowMap.put(key[k], datesql );
//    						}else{
//    							rowMap.put(key[k] , null );
//    					    	 flag.add(false);
//    						}
//    						break;
    					case "skip":
    						 cellIterator.next();
    						break;
    						
    					default:
    						rowMap.put(key[k],typeOrg[k]);
    						break;
    					}
    	        	  }
    	        	 
    	         } // end cells 
    	        if(!flag.contains(false)){
    	        	workbook_lsit.add(rowMap);
    	        }
    	     } // end rows 
		 } // end sheet 
		 return workbook_lsit;
	}
	
	
	public static String parseExcelToInsertSql(String excelFilePath,String keyName, String keyType , int startRow , int startCol,String tableName){
		List<Map<String,Object>> data_list = parseExcel(excelFilePath, keyName, keyType, startRow, startCol);
		StringBuffer sqlbuffer = new StringBuffer();
		sqlbuffer.append(" insert  into  "+tableName+"  ( ");
		String key[] = keyName.toLowerCase().split(",");
		String type[] = keyType.toLowerCase().split(",");
		
		for(int i=0;i<type.length;i++){
			if(!type[i].equals("skip") && i!=0){
				sqlbuffer.append(","+key[i]);
			}else if(i==0){
				sqlbuffer.append(key[i]);
			}
		}
		sqlbuffer.append(" )   values  ");
		
		
		for(int i=0;i<data_list.size();i++){
			if(i==0){ sqlbuffer.append("  ( "); }else{ sqlbuffer.append(" ,( "); }

			for(int j=0;j<type.length;j++){
				if(j!=0 && !type[j].equals("skip")){ sqlbuffer.append(","); }

				switch (type[j]) {
				case "number":
					sqlbuffer.append(data_list.get(i).get(key[j]));
					break;
				case "string":
					sqlbuffer.append("'"+data_list.get(i).get(key[j])+"'");
					break;
				case "boolean":
					sqlbuffer.append(data_list.get(i).get(key[j]));
					break;
				case "date":
					sqlbuffer.append(data_list.get(i).get(key[j]));
					break;
				case "skip":
					break;
				default:
					sqlbuffer.append(data_list.get(i).get(key[j]));
					break;
				}
			}
			sqlbuffer.append(" ) ");
		}
		return sqlbuffer.toString();
	}
		   

}
