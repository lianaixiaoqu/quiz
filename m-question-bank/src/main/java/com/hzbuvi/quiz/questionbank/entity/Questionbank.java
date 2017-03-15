package com.hzbuvi.quiz.questionbank.entity;

import com.hzbuvi.util.basic.ValueUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Downey.hz on 2016/10/11..
 */

@Entity
@Table(name= "questionbank")
public class Questionbank {
    @Id
	@GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private String questionContent;
    private String answerContent;
    private String rightAnswer;
    private Integer categoryId;
	private String categoryName;
    private Integer knowledgeId;
	private String knowledgeName;
    @Enumerated(EnumType.ORDINAL)
    private  TypeEnum type;
	private  String typeName;
    @Enumerated(EnumType.ORDINAL)
    private  DifficultyEnum difficulty;
	private String difficultyName;
    @Enumerated(EnumType.ORDINAL)
    private DeleteEnum isDelete;
    private String serialNumber;
    private String analysis;

	@Transient
	private List<Answer> answers = new ArrayList<>();
	@Transient
	private Set<Integer> correct = new HashSet<>();
	@Transient
	private Integer sequence;

	@Transient
	private String a;
	@Transient
	private String b;
	@Transient
	private String c;
	@Transient
	private String d;
	@Transient
	private String e;
	@Transient
	private String f;
	@Transient
	private String g;
	@Transient
	private String h;
	@Transient
	private String i;
	@Transient
	private String j;
	@Transient
	private String k;

	private  boolean canPut(String option){
		if ( null != option && ! "".equals( option.trim() )  &&  !"null".equals( option.trim().toLowerCase() ) ) {
			return true;
		} else {
			return false;
		}
	}


	public void putAnswer(){
		this.rightAnswer = this.rightAnswer.toLowerCase();
		if ( canPut( getA() ) ) {
			answers.add(new Answer(getA(),isCorrect("a")));
		}
		if ( canPut(getB()) ) {
			answers.add(new Answer(getB(),isCorrect("b")));
		}
		if ( canPut(getC()) ) {
			answers.add(new Answer(getC(),isCorrect("c")));
		}
		if ( canPut(getD()) ) {
			answers.add(new Answer(getD(),isCorrect("d")));
		}
		if ( canPut(getE()) ) {
			answers.add(new Answer(getE(),isCorrect("e")));
		}
		if ( canPut(getF()) ) {
			answers.add(new Answer(getF(),isCorrect("f")));
		}
		if ( canPut(getG()) ) {
			answers.add(new Answer(getG(),isCorrect("g")));
		}
	}

	private Integer isCorrect(String option){
		if (this.rightAnswer.contains(option)) {
			return 1;
		} else {
			return 0;
		}
	}


	public Questionbank() {
		this.isDelete = DeleteEnum.NOT_DELETE;
	}

	public Questionbank(Object[] objects) {
//				0+" id "
//				1+",questionContent "
//				2+",answerContent "
//				3+",rightAnswer "
//				4+",categoryId "
//				5+",categoryName "
//				6+",knowledgeId "
//				7+",knowledgeName "
//				8+",type "
//				9+",typeName "
//				10+",difficulty "
//				11+",difficultyName "
//				12+",serialNumber "
//				13+",analysis
		this.id = Integer.valueOf(objects[0].toString());
		this.questionContent = objects[1].toString();
		this.answerContent = objects[2].toString();
		this.rightAnswer = objects[3].toString();
		this.categoryId = Integer.valueOf(objects[4].toString());
		this.categoryName = objects[5].toString();
		this.knowledgeId = Integer.valueOf(objects[6].toString());
		this.knowledgeName = objects[7].toString();
		this.type = TypeEnum.getType(objects[8].toString());
		this.typeName = objects[9].toString();
//		this.difficulty = DifficultyEnum.getDifficulty(objects[10].toString());
		this.setDifficultyName(objects[11].toString());
		this.serialNumber = objects[12].toString();
		this.analysis = objects[13].toString();

	}

	public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Integer knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getKnowledgeName() {
		return knowledgeName;
	}

	public void setKnowledgeName(String knowledgeName) {
		this.knowledgeName = knowledgeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
		this.type = TypeEnum.getType(typeName);
	}

	public String getDifficultyName() {
		return difficultyName;
	}

	public void setDifficultyName(String difficultyName) {
		if (null != difficultyName && !difficultyName.toLowerCase().equals("null")) {
			this.difficulty = DifficultyEnum.getDifficulty(difficultyName);
			this.difficultyName = this.difficulty.getValue() ;
		}
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getI() {
		return i;
	}

	public void setI(String i) {
		this.i = i;
	}

	public String getJ() {
		return j;
	}

	public void setJ(String j) {
		this.j = j;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public List<Answer> getAnswers() {
		for (Answer a: answers) {
			a.setQuestionid(this.id);
		}
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		System.out.println(this.id);
		this.answers = answers;
		for (Answer a : this.answers) {
			if (a.getCorrect().equals(1)) {
				this.correct.add(a.getId());
			}
		}
	}


	public Set<Integer> getCorrect() {
		return correct;
	}

	public void setCorrect(Set<Integer> correct) {
		this.correct = correct;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public boolean verifyValue(){
		if ( ValueUtil.notEmpity(this.serialNumber)
				&&  ValueUtil.notEmpity(this.knowledgeId)
				&&  ValueUtil.notEmpity(this.knowledgeName.trim())
				&&  ValueUtil.notEmpity(this.difficulty)
				&&  ValueUtil.notEmpity(this.difficultyName.trim())
				&&  ValueUtil.notEmpity(this.categoryId)
				&&  ValueUtil.notEmpity(this.typeName.trim())
				&&  ValueUtil.notEmpity(this.questionContent.trim())
				&&  ValueUtil.notEmpity(this.categoryName.trim())
				&&  ValueUtil.notEmpity(this.rightAnswer.trim())
				) {
			return true;
		} else {
			return false;
		}
	}

}
