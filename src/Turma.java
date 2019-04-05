

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Turma {
	private String className;
	private String course;
	private String disciplineName;
	private String group;
	private String year;
	private String semester;
	private String goals;
	private String methods;
	private String evaluation;
	private ArrayList<String> references;
	private File file;
	private HeaderNames maxField;
	
	public enum TURMAS_DATA {PLANO_DE_CURSO, FREQUENCIA, ATA, NOTA, PAGELA}
	
	private Hashtable<TURMAS_DATA, String> linksHashTable;
	private HashMap<String, Student> studentsHash;
	private ArrayList<Student> students;

	public Turma() {
		this.linksHashTable = new Hashtable<TURMAS_DATA, String>();
		this.students = new ArrayList<Student>();
		this.references = new ArrayList<String>();
	}
	
	public Turma(String className, String course, String disciplineName,
			String group, Hashtable<TURMAS_DATA, String> linksHashTable, String year, String semester) {
		super();
		this.className = className;
		this.course = course;
		this.disciplineName = disciplineName;
		this.group = group;
		this.linksHashTable = linksHashTable;
		this.year = year;
		this.semester = semester;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	
	public void addReference(String reference) {
		this.references.add(reference);
	}

	public ArrayList<String> getReferences() {
		return references;
	}

	public void setReferences(ArrayList<String> references) {
		this.references = references;
	}

	public HeaderNames getMaxField() {
		return maxField;
	}

	public void setMaxField(HeaderNames maxField) {
		this.maxField = maxField;
	}

	public void setMaxData(HeaderNames maxField) {
		this.maxField = maxField;
	}
	
	public HeaderNames  getMaxData() {
		return this.maxField;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public String getCourse() {
		return course;
	}


	public void setCourse(String course) {
		this.course = course;
	}


	public String getDisciplineName() {
		return disciplineName;
	}


	public void setDisciplineName(String disciplineName) {
		this.disciplineName = disciplineName;
	}


	public String getGroup() {
		return group;
	}


	public void setGroup(String group) {
		this.group = group;
	}


	public Hashtable<TURMAS_DATA, String> getLinksHashTable() {
		return linksHashTable;
	}


	public void setLinksHashTable(Hashtable<TURMAS_DATA, String> linksHashTable) {
		this.linksHashTable = linksHashTable;
	}


	public void setLink(TURMAS_DATA linkName, String webElement) {
		this.linksHashTable.put(linkName, webElement);
	}
	
	public void setNewStudent(Student s) {
		this.students.add(s);
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}
	
	public void setStudents(HashMap<String, Student> studentsHash) {
		this.studentsHash = studentsHash;
	}
	
	public HashMap<String, Student> getStudentsHash() {
		return studentsHash;
	}

	public String getFullName() {
		return this.getYear() + "-" + this.getSemester() + " - " + this.getClassName() + "-" + this.getCourse() + "-" + this.getGroup();
	}

	@Override
	public String toString() {
		String references = "";
		for(String eachReference : this.references) {
			references = references + eachReference + "\n";
		}
		return "Turma [className=" + className + ", course=" + course
				+ ", disciplineName=" + disciplineName + ", group=" + group
				+ ", year=" + year + ", semester=" + semester + ", goals="
				+ goals + ", methods=" + methods + ", evaluation=" + evaluation
				+ ", file=" + file
				+ ", maxField=" + maxField + ", linksHashTable="
				+ linksHashTable + ", students=" + students + ", " + references + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result
				+ ((disciplineName == null) ? 0 : disciplineName.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result
				+ ((linksHashTable == null) ? 0 : linksHashTable.hashCode());
		result = prime * result
				+ ((semester == null) ? 0 : semester.hashCode());
		result = prime * result
				+ ((students == null) ? 0 : students.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getFullName().equals(((Turma)obj).getFullName()))  {
			System.out.println("FOI IGUAL " + this.getFullName() + " = " + ((Turma)obj).getFullName());
			return true;
		} else {
			System.out.println("NAO EH IGUAL " + this.getFullName() + " = " + ((Turma)obj).getFullName());
			return false;
		}
	}
	
	
	
}
