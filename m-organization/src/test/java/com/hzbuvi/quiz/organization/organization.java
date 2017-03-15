package com.hzbuvi.quiz.organization;

import com.hzbuvi.quiz.organization.service.OrganizationService;
import org.junit.Test;

/**
 * Created by light on 2016/10/29.
 */
public class organization {
    @Test
    public void get(){
        OrganizationService organizationService=new OrganizationService();
        organizationService.inputOrganizationFromExcel("D:\\ceshi\\zzb.xlsx");
    }
}
