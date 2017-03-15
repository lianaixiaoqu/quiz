package com.hzbuvi.quiz.organization.service;

import com.hzbuvi.quiz.excelimport.ExcelImportBiz;
import com.hzbuvi.quiz.organization.entity.DeleteEnum;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.repository.OrganizationRepository;
import com.hzbuvi.quiz.organization.util.PageUtil;
import com.hzbuvi.util.basic.MapUtil;
import com.hzbuvi.util.basic.PageResult;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Downey.hz on 2016/10/17..
 */
@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ExcelImportBiz excelImportBiz;
    private static int defaultPageSize=10;
    public synchronized String inputOrganizationFromExcel(String excelFilePath){
        List<Map<String,Object>> datas = excelImportBiz.getData(excelFilePath,this.getClass());
        List<Organization> organizations = new ArrayList<>();
        List<Integer> delorganizationIdList=new ArrayList<>();
        for (Map<String,Object > or : datas) {
            Organization organization=(Organization)MapUtil.mapToObject(or,Organization.class);
            organization.setIsDelete(DeleteEnum.NOT_DELETE);
            organizations.add(organization);
            if (ValueUtil.notEmpity(organization.getOrganizationId())){
                delorganizationIdList.add(organization.getOrganizationId());
            }
        }

		List<Integer> existOrganizationIdList =new ArrayList<>();
		List<Organization> toSave = new ArrayList<>();

        if(delorganizationIdList.size()>0){
            List<Organization> list= organizationRepository.findByOrganizationIdIn(delorganizationIdList);
			for (int i = 0; i < list.size() ; i++) {
				existOrganizationIdList.add(list.get(i).getOrganizationId());
			}
//            organizationRepository.delete(list);
			for (int i = 0; i < organizations.size() ; i++) {
				if ( ! existOrganizationIdList.contains( organizations.get(i).getOrganizationId() ) ) {
					toSave.add(organizations.get(i));
				}
			}

        }

        organizationRepository.save(toSave);
        return  "success";
    }

    public Map<String,String> addOrganization(Organization organization){
        Integer organizationId=organization.getOrganizationId();
        HashMap<String,String> map=new HashMap<>();
        if(null!=organizationRepository.findIdByOrganizationId(organizationId)){
            map.put("status","1");
            return map;
        }
        organization.setIsDelete(DeleteEnum.NOT_DELETE);
        organizationRepository.save(organization);
        map.put("status","success");

//        Integer organizationId=organization.getOrganizationId();
//        if(ValueUtil.notEmpity(organizationRepository.findByOrganizationId(organizationId))){
//            map.put("status","1");
//        }else {
//            if(null==organizationRepository.findByOrganizationId(organization.getOrganizationId())){
//                organization.setIsDelete(DeleteEnum.NOT_DELETE);
//                organizationRepository.save(organization);
//                map.put("status","success");
//            }else {
//                map.put("status","failure");
//            }
//        }
       return map;
    }
    public String delete(Integer id){
        Organization organization=organizationRepository.findOne(id);
        organization.setIsDelete(DeleteEnum.DELETED);
        organizationRepository.save(organization);
        return "success";
    }
    public List<Object> updateLoad(Integer id){
        Organization organization=organizationRepository.findOne(id);
        List<Organization> list1=organizationRepository.findAll();
        List<Object> list=new ArrayList<>();
        list.add(organization);
        list.add(list1);
        return list;
    }
    public List<Organization> showall(){
        List<Organization> list=organizationRepository.findAll();
        return list;
    }
    public String updateSave(Organization organizationNew){

        Organization organizationOld=organizationRepository.findOne(organizationNew.getId());
//        organizationOld.setOrganizationId(organizationNew.getOrganizationId());
        organizationOld.setIsDelete(DeleteEnum.NOT_DELETE);
        organizationOld.setOrganizationName(organizationNew.getOrganizationName());
        organizationOld.setParentId(organizationNew.getParentId());
        organizationOld.setOrganizationPath(organizationNew.getOrganizationPath());
        organizationOld.setOrganizationLevel(organizationNew.getOrganizationLevel());
        organizationOld.setOrganizationLevelName(organizationNew.getOrganizationLevelName());
        organizationRepository.save(organizationOld);
        return "success";
    }

    public PageResult searchAll(Integer pageNumber) {
        if(null==pageNumber){
            pageNumber=0;
        }
        Pageable pageable = new PageRequest(ValueUtil.coalesce(pageNumber,0),defaultPageSize);
        Page<Organization> items = organizationRepository.findByIsDelete(pageable,DeleteEnum.NOT_DELETE);
        return  PageUtil.toPage(items,pageNumber);
    }

    public  Map<String,Object> show(Map<String,String> parm) {
        String page = ValueUtil.coalesce( parm.get("page") , "0") ;
        String organizationName= parm.get("organizationName");
        String organizationId=parm.get("organizationId");
        String organizationLevelName= parm.get("organizationLevelName");
        Map<String,String> map1=new HashMap<>();
        if (ValueUtil.notEmpity(organizationName)) {
            map1.put("organizationName",organizationName);
        }
        if (ValueUtil.notEmpity(organizationLevelName)) {
            map1.put("organizationLevelName",organizationLevelName);
        }
        if(ValueUtil.notEmpity(organizationId)){
            map1.put("organizationId",organizationId);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page),defaultPageSize);
        Page<Organization> questionbankPage = organizationRepository.findAll(getWhereClause(organizationId,organizationName,organizationLevelName), pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("page",questionbankPage);
        map.put("condition",map1);
        return map;
    }


    private Specification<Organization> getWhereClause(String organizationId,String organizationName, String organizationLevelName) {
        return new Specification<Organization>() {
            @Override
            public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(organizationName)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("organizationName"), "%" + organizationName.trim() + "%"));
                }
                if (ValueUtil.notEmpity(organizationLevelName)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("organizationLevelName"),"%"+organizationLevelName.trim()+"%") );
                }
                if(ValueUtil.notEmpity(organizationId)){
                    predicate.getExpressions().add( cb.equal(root.<Integer>get("organizationId"),Integer.valueOf(organizationId)) );
                }
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
                return predicate;
            }
        };
    }


}
