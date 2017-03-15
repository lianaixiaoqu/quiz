package com.hzbuvi.quiz.questionsheet.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WANG, RUIQING on 11/3/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class VerifyAnswerResult {

	private Integer historyId;
	private Integer questionId;
	private Integer sequence;
	private List<Integer> correctAnswer;
	private String  correctAnswerStr;
	private Integer score;
	private boolean correct;





	//false
	public VerifyAnswerResult(Integer questionId,Integer sequence, List<Integer> correctAnswer ) {
		this.questionId = questionId;
		this.sequence = sequence;
		this.correctAnswer = correctAnswer;
		this.score = 0;
		this.correct = false;

	}

	// true
	public VerifyAnswerResult(Integer score) {
		this.score = score;
		this.correct = true;
	}

	public Integer getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	public List<Integer> getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(List<Integer> correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getCorrectAnswerStr() {
		return correctAnswerStr;
	}

	public void setCorrectAnswerStr(String correctAnswerStr) {
		this.correctAnswerStr = correctAnswerStr;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
