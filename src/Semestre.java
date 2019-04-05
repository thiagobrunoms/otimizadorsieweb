

import java.util.ArrayList;

public class Semestre {
	
	private ArrayList<Turma> turmas;
	private String year;
	private String period;

	public Semestre() {
		this.turmas = new ArrayList<Turma>();
	}
	
	public Semestre(ArrayList<Turma> turmas, String year, String period) {
		this.turmas = turmas;
		this.year = year;
		this.period = period;
	}
	
	public void addNewTurma(Turma t) {
		this.turmas.add(t);
	}

	public ArrayList<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(ArrayList<Turma> turmas) {
		this.turmas = turmas;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Semestre [turmas=");
		String turmasStr = "";
		
		for(Turma t : turmas) {
			turmasStr = turmasStr + "\n" + t;
		}
		
		builder.append(turmasStr);
		builder.append(", year=");
		builder.append(year);
		builder.append(", period=");
		builder.append(period);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
}
