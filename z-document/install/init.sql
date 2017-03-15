



INSERT INTO BACKGROUNDUSER (ID, LOGINNAME, PASSWORD) VALUES (8410, 'admin', '123456');



INSERT INTO ACTIVITY (ID, CHAPTERS, CREATETIME, CREATEUSERID, DAILYEND, DAILYSTART, DESCRIPTION, EASYPERSECTION, ENDTIME, HARDPERSECTION, ISDELETE, NAME, NORMALPERSECTION, QUESTIONSHEETS, QUESTIONS, QUESTIONSPERSECTION, SECTIONLIMITTIME, SECTIONS, SECTIONSPERCHAPTER, STARTTIME) VALUES (1, 4, TO_DATE('2016-11-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, '180000', '090000', '知识微答', 3, TO_DATE('2016-11-24 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 0, '知识微答', 3, 4, 100, 10, 199, 16, 4, TO_DATE('2016-11-02 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));




INSERT INTO EXCELIMPORT (ID, CLASSNAME, KEYNAME, KEYTYPE, NAME, STARTCOL, STARTROW) VALUES (1, 'com.hzbuvi.quiz.questionbank.service.QuestionImport', 'categoryName,serialNumber,questionContent,typeName,knowledgeName,difficultyName,analysis,rightAnswer,a,b,c,d,e,f,h,i,j,k', 'string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string,string', '題庫导入', 0, 3);
INSERT INTO EXCELIMPORT (ID, CLASSNAME, KEYNAME, KEYTYPE, NAME, STARTCOL, STARTROW) VALUES (2, 'com.hzbuvi.quiz.organization.service.OrganizationService', 'organizationId,organizationName,organizationLevel,organizationLevelName,organizationPath,parentId', 'number,string,number,string,string,number', '组织信息导入', 0, 2);
INSERT INTO EXCELIMPORT (ID, CLASSNAME, KEYNAME, KEYTYPE, NAME, STARTCOL, STARTROW) VALUES (3, 'com.hzbuvi.quiz.organization.service.OrganizationUserService', 'jobNumber,name,position,phoneNumber,email,workStation,organizationId,organizationPath', 'string,string,string,string,string,string,number,string', '组织人员信息导入', 0, 2);




INSERT INTO BIZCONDITION (ID, BIZCLASS, CODE, NAME, PARAMETERCNT, PARAMETERS) VALUES (1, 'com.hzbuvi.quiz.usersection.biz.UnlockChapterCondition', 'unlockChapter', '阿德', 1, '231');



update BACKGROUNDUSER set PASSWORD = 'ujJTh2rta8ItSm/1PYQGxq2GQZXtFEq1yHYhtsIztUi66uaVbfNG7IwX9eoQ817jy8UUeX7X3dMUVGTioLq0Ew=='  where PASSWORD = '123456';



ALTER TABLE ACTIVITY ADD isCurrent INT DEFAULT 0 NULL;

update ACTIVITY set  isCurrent = 1 where id = 1;

