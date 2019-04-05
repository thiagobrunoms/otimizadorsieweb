

public class Student {
	private String name;
	private String code;
	private String score1;
	private String score2;
	private String scoreReposition;
	private String scoreFinal;
	private StudentWebElements studentWebElements;
	
	public Student() {}
	
	public Student(String name, String code, String score1, String score2, String scoreReposition, String scoreFinal) {
		super();
		this.name = name;
		this.code = code;
		this.score1 = score1;
		this.score2 = score2;
		this.scoreReposition = scoreReposition;
		this.scoreFinal = scoreFinal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getScore1() {
		return score1;
	}

	public void setScore1(String score1) {
		this.score1 = score1;
	}

	public String getScore2() {
		return score2;
	}

	public void setScore2(String score2) {
		this.score2 = score2;
	}

	public String getScoreReposition() {
		return scoreReposition;
	}

	public void setScoreReposition(String scoreReposition) {
		this.scoreReposition = scoreReposition;
	}

	public String getScoreFinal() {
		return scoreFinal;
	}

	public void setScoreFinal(String scoreFinal) {
		this.scoreFinal = scoreFinal;
	}
	
	

	public StudentWebElements getStudentWebElements() {
		return studentWebElements;
	}

	public void setStudentWebElements(StudentWebElements studentWebElements) {
		this.studentWebElements = studentWebElements;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", code=" + code + ", score1="
				+ score1 + ", score2=" + score2 + ", scoreReposition="
				+ scoreReposition + ", scoreFinal=" + scoreFinal + "]";
	}

	
	
	
	
}
