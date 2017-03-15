package com.hzbuvi.quiz.questionsheet.bean;

import com.hzbuvi.quiz.questionbank.entity.Questionbank;

import java.util.*;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class RightAnswer {


	private Integer questionId;
	private Integer sequence;
	private Set<Integer> answerId;
	private List<Integer> answerList = new ArrayList<>();
	private Integer score;


	public VerifyAnswerResult getScore(List<Integer> inputAnswerId){
		Collections.sort(inputAnswerId);

		if (this.answerList.size() == inputAnswerId.size()) {

			for (int i = 0; i < inputAnswerId.size() ; i++) {
				if (! this.answerList.contains(inputAnswerId.get(i)) ) {
					return new VerifyAnswerResult(this.questionId,this.sequence,this.answerList);
				}
			}
				return new VerifyAnswerResult(this.score);
		} else {
			return new VerifyAnswerResult(this.questionId,this.sequence,this.answerList);
		}

	}

	public RightAnswer(Questionbank question,Integer easyScore,Integer normalScore,Integer hardScore) {
		this.questionId = question.getId();
		this.sequence = question.getSequence();
		this.answerId = question.getCorrect();
		switch (question.getDifficulty()){
			case Easy: this.score = easyScore;break;
			case Normal: this.score = normalScore ;break;
			case Difficult: this.score = hardScore;break;
		}
		this.answerId.forEach( i -> answerList.add(i));
		Collections.sort(this.answerList);
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Set<Integer> getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Set<Integer> answerId) {
		this.answerId = answerId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<Integer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Integer> answerList) {
		this.answerList = answerList;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
