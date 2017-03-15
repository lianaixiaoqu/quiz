package com.hzbuvi.quiz.organization.service;

import com.hzbuvi.page.util.PageResult;
import com.hzbuvi.page.util.PageUtil;
import com.hzbuvi.quiz.excelimport.ExcelImportBiz;
import com.hzbuvi.quiz.organization.entity.DeleteEnum;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.entity.OrganizationUser;
import com.hzbuvi.quiz.organization.repository.OrganizationRepository;
import com.hzbuvi.quiz.organization.repository.OrganizationUserRepository;
import com.hzbuvi.util.basic.MapUtil;
import com.hzbuvi.util.basic.ValueUtil;

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
import javax.print.attribute.standard.JobName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangDianDian on 2016/10/27.
 */
@Service
public class OrganizationUserService {
    @Autowired
    private OrganizationUserRepository organizationUserRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ExcelImportBiz excelImportBiz;
    private static int defaultPageSize=10;

    public  synchronized  String inputFromExcel(String excelFilePath){
        List<Map<String,Object>> datas = excelImportBiz.getData(excelFilePath,this.getClass());
        List<OrganizationUser> organizationUsers = new ArrayList<>();
        List<String> deljobNUmberList=new ArrayList<>();
		Map<String,OrganizationUser> inpoutUserMap = new HashMap<>();

        for (Map<String,Object > oru : datas) {
            OrganizationUser organizationUser=(OrganizationUser) MapUtil.mapToObject(oru,OrganizationUser.class);
            organizationUser.setIsDelete(DeleteEnum.NOT_DELETE);
			organizationUser.trimJobNumber();
//            organizationUsers.add(organizationUser);
            if (ValueUtil.notEmpity(organizationUser.getJobNumber())){
                deljobNUmberList.add(organizationUser.getJobNumber());
				inpoutUserMap.put(organizationUser.getJobNumber(),organizationUser);
            }
        }


     	if(deljobNUmberList.size()>0){
        	List<OrganizationUser> list= organizationUserRepository.findByJobNumberIn(deljobNUmberList);
//         organizationUserRepository.delete(list);
			for (int i = 0; i < list.size(); i++) {
				organizationUsers.add(list.get(i).format(inpoutUserMap.get(list.get(i).getJobNumber())));
				deljobNUmberList.remove(list.get(i).getJobNumber());
			}
			for (int i = 0; i < deljobNUmberList.size(); i++) {
				organizationUsers.add( inpoutUserMap.get(deljobNUmberList.get(i)) );
			}
        }
        organizationUserRepository.save(organizationUsers);
        return "导入成功";
    }



    public String  addOrganization(OrganizationUser organizationUser){
       String job=organizationUser.getJobNumber();
        List<Integer> idd=organizationUserRepository.findIdByJobNumberAndIsDelete(job,DeleteEnum.NOT_DELETE);
        if(organizationUserRepository.findIdByJobNumberAndIsDelete(job,DeleteEnum.NOT_DELETE).size()>0){//存在没删
            return "fail";
        }

		if (organizationUserRepository.findIdByJobNumberAndIsDelete(job,DeleteEnum.DELETED).size()>0){//存在删了
            OrganizationUser organizationUser1=organizationUserRepository.findByJobNumber(job);
        if(organizationUser.getPosition().equals("")){
            organizationUser1.setPosition(" ");
        }
            organizationUser1.setIsDelete(DeleteEnum.NOT_DELETE);
        Integer organizationId=organizationUser.getOrganizationId();
		if (null != organizationId) {
            organizationUser1.setOrganizationName(organizationRepository.findByOrganizationId(organizationId).getOrganizationName());
		}
        organizationUserRepository.save(organizationUser1);
        return "success";
		} else{
            if(organizationUser.getPosition().equals("")){
                organizationUser.setPosition(" ");
            }
            organizationUser.setIsDelete(DeleteEnum.NOT_DELETE);
            Integer organizationId=organizationUser.getOrganizationId();
            if (null != organizationId) {
                organizationUser.setOrganizationName(organizationRepository.findByOrganizationId(organizationId).getOrganizationName());
            }
            organizationUserRepository.save(organizationUser);
            return "success";

        }
    }
    public String delete(Integer id)  {
        OrganizationUser organizationUser=organizationUserRepository.findOne(id);
        organizationUser.setIsDelete(DeleteEnum.DELETED);
        organizationUserRepository.save(organizationUser);
        return "success";
    }
    public List<Object> updateLoad(Integer id){
        OrganizationUser organizationUser=organizationUserRepository.findOne(id);
        List<Organization> list1=organizationRepository.findAll();
        List<Object> list=new ArrayList<>();
        list.add(organizationUser);
        list.add(list1);
        return list;
    }
    public List<Organization> showall(){
        List<Organization> list=organizationRepository.findAll();
        return list;
    }
    public String updateSave(OrganizationUser organizationUserNew){
            int oldId=organizationUserNew.getId();
            OrganizationUser organizationUser=organizationUserRepository.findOne(oldId);
            organizationUser.setIsDelete(DeleteEnum.NOT_DELETE);
            organizationUser.setEmail(organizationUserNew.getEmail());
//            organizationUser.setName(organizationUserNew.getName());
            Integer organizationId=organizationUserNew.getOrganizationId();
            if (null != organizationId) {
                organizationUser.setOrganizationId(organizationId);
                organizationUser.setOrganizationName(organizationRepository.findByOrganizationId(organizationId).getOrganizationName());
            }
            organizationUser.setOrganizationPath(organizationUserNew.getOrganizationPath());
            organizationUser.setPhoneNumber(organizationUserNew.getPhoneNumber());
            if(organizationUserNew.getPosition().equals("")){
                organizationUser.setPosition(" ");
            }else{
                organizationUser.setPosition(organizationUserNew.getPosition());
            }
            organizationUser.setWorkStation(organizationUserNew.getWorkStation());
            organizationUserRepository.save(organizationUser);
           return "sucess";
    }


    public PageResult searchAll(Integer pageNumber) {
        if(null==pageNumber){
            pageNumber=0;
        }
        Pageable pageable = new PageRequest(ValueUtil.coalesce(pageNumber,0),defaultPageSize);
        Page<OrganizationUser> items = organizationUserRepository.findByIsDelete(pageable,DeleteEnum.NOT_DELETE);
        return  PageUtil.toPage(items,pageNumber);
    }
    public  Map<String,Object> show(Map<String,String> parm){
        String page = ValueUtil.coalesce( parm.get("page"),"0");
        String jobNumber= parm.get("jobNumber");
        String name= parm.get("name");
        String organizationName = parm.get("organizationId");
        String position=parm.get("position");
        Map<String,String> map1=new HashMap<>();
        if (ValueUtil.notEmpity(jobNumber)) {
            map1.put("jobNumber",jobNumber);
        }
        if (ValueUtil.notEmpity(name)) {
            map1.put("name",name);
        }
        if (ValueUtil.notEmpity(organizationName)) {
            map1.put("organizationName",organizationName);
        }
        if (ValueUtil.notEmpity(position)) {
            map1.put("position",position);
        }
        Pageable pageable =new PageRequest(Integer.valueOf(page),defaultPageSize);
//        Integer number=Integer.parseInt(jobNumber);
        Page<OrganizationUser> questionbankPage = organizationUserRepository.findAll(getWhereClause(jobNumber,name,organizationName,position), pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("page",questionbankPage);
        map.put("condition",map1);
        return map;
    }

    private Specification<OrganizationUser> getWhereClause(String jobNumber,String name,String organizationName,String position) {
        return new Specification<OrganizationUser>() {
            @Override
            public Predicate toPredicate(Root<OrganizationUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(jobNumber) ) {
                    predicate.getExpressions().add( cb.equal(root.<String>get("jobNumber"),jobNumber));
                }
                if (ValueUtil.notEmpity(name)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("name"),"%"+name+"%") );
                }
                if (ValueUtil.notEmpity(organizationName)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("organizationName"),"%"+organizationName+"%") );
                }
                if (ValueUtil.notEmpity(position)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("position"),"%"+position+"%") );
                }
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
                return predicate;
            }
        };
    }

    public OrganizationUser findByJobNumber(String jobNumber){
		return organizationUserRepository.findByJobNumber(jobNumber);
	}

    public Integer findIdByJobNumber(String jobNumber){
		OrganizationUser user = organizationUserRepository.findByJobNumber(jobNumber);
		if (null!= user) {
			return user.getId();
		} else {
			return 1;
		}
	}

	public List<OrganizationUser>  findByJobNumber(List<String> jobNumberList){
		return organizationUserRepository.findByJobNumberIn(jobNumberList);
	}

	public Map<Integer,String> getJobNumber(List<Integer> userId){

		List<OrganizationUser> organizationUsers = new ArrayList<>();


		boolean flag = true;
		int i=0;
		while (flag) {
			int startIndex = i++ * 900;
			int endIndex = startIndex + 900 ;
			if (endIndex>userId.size()){
				endIndex = userId.size() -1 ;
			}
			if (endIndex > startIndex) {
				List<OrganizationUser> data = organizationUserRepository.findAll(userId.subList(startIndex,endIndex));
				if (null == data || data.size() == 0) {
					flag = false;
				} else {
					organizationUsers.addAll(data);
				}
			} else {
				flag = false;
			}
		}


		Map<Integer,String> numberMap = new HashMap<>();
		organizationUsers.forEach( o -> numberMap.put( o.getId(),o.getJobNumber() ) );
		return numberMap;
	}

}


