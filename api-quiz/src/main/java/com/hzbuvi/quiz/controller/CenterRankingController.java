package com.hzbuvi.quiz.controller;

/*import com.hzbui.quiz.centerRanking.repository.CenterRankingRepository;
import com.hzbui.quiz.centerRanking.service.CenterRankingService;
import com.hzbuvi.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;*/
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by WangDianDian on 2016/10/14.
 */
@RestController
@RequestMapping("/centerRanking")
public class CenterRankingController {
  /*  @Autowired
    private CenterRankingService centerRankingService;

    @RequestMapping(value = "/center",method = RequestMethod.GET)
    public String searchIndividual(Integer pageNumber,Integer activityId,Integer userId) {
        return ValueUtil.toJson("center", centerRankingService.searchCenter(pageNumber, activityId, userId));
    }*/
}
