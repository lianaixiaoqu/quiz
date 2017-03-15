
package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.config.SessionData;
//import com.hzbuvi.quiz.individualRanking.service.IndividualRankingService;
//import com.hzbuvi.quiz.section.entity.UserSection;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by WangDianDian on 2016/10/14.
 */
@RestController
@RequestMapping("/individualRanking")
public class IndividualRankingController {


//package com.hzbuvi.quiz.controller;
//
//import com.hzbuvi.quiz.config.SessionData;
//import com.hzbuvi.quiz.individualRanking.service.IndividualRankingService;
//import com.hzbuvi.quiz.section.entity.UserSection;
//import com.hzbuvi.util.basic.ValueUtil;
//import com.hzbuvi.util.exception.HzbuviException;
//import org.hibernate.mapping.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.Mapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpSession;
//import java.util.Map;
//
///**
// * Created by WangDianDian on 2016/10/14.
// */
//@RestController
//@RequestMapping("/individualRanking")
//public class IndividualRankingController {
//

//    @Autowired
//    private IndividualRankingService individualRankingService;
//
//    @RequestMapping(value = "/individual",method = RequestMethod.GET)
//    public String searchIndividual(Integer pageNumber,Integer activityId,Integer userId) {
//        return ValueUtil.toJson("info", individualRankingService.searchIndividual(pageNumber, activityId, userId));
//    }


//}
//
//
}


