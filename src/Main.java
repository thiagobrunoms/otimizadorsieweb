import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.openqa.selenium.NoSuchElementException;

import exceotions.AuthenticationException;


//TODO 
//Se uma celula da planilha estiver fazia na leitura da nota de um aluno, eh lançada exceção. Analisar


public class Main {
	
	private FilesManager ex;
	private SIESelenium seleniumMgn;
	private boolean browserInitialized;
	private boolean userLoggedIn;
	public enum CREATE_TURMA_TYPE_FOR {GRADES, CLASSPLAN};
	public enum PERIOD_TYPES {NOWADAYS, PREVIOUS};
	
	public Main() {
		browserInitialized = false;
		userLoggedIn = false;
		ex = new FilesManager();
		seleniumMgn = new SIESelenium();
	}
	
	public void setUserCredentials(String cpf, String password) {
		seleniumMgn.setCpf(cpf);
		seleniumMgn.setPass(password);
	}
	
	public boolean createFilesForTurmas(String path, String year, String period) throws IOException, NoSuchElementException {
		String newPath;
		try {
			System.out.println("criando path...");
			newPath = ex.createFolder(path, year, period);
			System.out.println("path criado: " + newPath);
			
			if (browserInitialized && userLoggedIn) {
				System.out.println("Browser inicializado e login realizado!");
				ArrayList<Turma> turmas = seleniumMgn.getGroupsByYearSemester(year, period);
				return ex.createExcelForGrades(turmas, newPath);
			} else {
				System.out.println("Browser não inicializado ou login não realizado!");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Turma createTurmaFromFile(String year, String semester, String name, String course, String group, File file, CREATE_TURMA_TYPE_FOR type) throws IOException {
		//2016-1 - CPTA054 - PROGRAMAÇÃO 2 - 60h-CIÊNCIA DA COMPUTAÇÃO.xlsx
		Turma turma = new Turma();
		
		turma.setYear(year);
		turma.setSemester(semester);
		turma.setClassName(name);
		turma.setCourse(course);
		turma.setGroup(group);
		turma.setFile(file);
		
		switch (type) {
			case GRADES:
//				ArrayList<Student> students = ex.readTurma(turma);
				HashMap<String, Student> students = ex.readTurma2(turma);
				turma.setStudents(students);
				for(Student s : students.values()) {
					System.out.println(s);
				}
			break;
//			case CLASSPLAN:
//				ex.getClassPlanFromExcel(turma);
//			break;
		}
		
		System.out.println(turma);
		
		return turma;
	}
	 
	public boolean sendScores(Semestre semester) {
		if (browserInitialized && userLoggedIn)
			return this.seleniumMgn.saveScores2(semester);
		
		return false;
	}
	
	public boolean saveClassPlan(Semestre semestre, PERIOD_TYPES periodType) throws NoSuchElementException {
		if (browserInitialized && userLoggedIn)
			this.seleniumMgn.defineNewClassPlans(semestre, periodType);
		
		return true;
	}
	
	public boolean getClassPlanFromSIE(Semestre semestre, String path, Main.PERIOD_TYPES periodType) throws IOException {
		if (browserInitialized && userLoggedIn) {
			semestre = this.seleniumMgn.getTurmasData(semestre.getYear(), semestre.getPeriod());
			
			if (semestre.getTurmas().size() > 0) {
				semestre = this.seleniumMgn.downloadCPFromSIE(semestre);
				String newPath = ex.createFolder(path, semestre.getYear(), semestre.getPeriod());
				this.ex.createExcelForCP(semestre, newPath);
				return true;
			}
		}
		
		return false;
	}
	
	public void initialize() throws AuthenticationException, Exception {
		if (!browserInitialized && !userLoggedIn) {
			seleniumMgn.start();
			browserInitialized = true;
		}
		
		if (!userLoggedIn) {
			try {
				seleniumMgn.makeLogin();
				if (seleniumMgn.isLoggedIn()) {
					userLoggedIn = true; //deveria ser o selenium - TODO
					seleniumMgn.goToTurmasPage();
				}
			} catch (NoSuchElementException e) {
				throw new AuthenticationException();
			}
		}
	}
	
	public void goToMainPage() {
		if (browserInitialized && userLoggedIn)
			seleniumMgn.goToTurmasPage();
	}
	
	public void logout() {
		if (userLoggedIn) {
			seleniumMgn.logout(3);
			userLoggedIn = false;
		}
			
		seleniumMgn.closeBrowser();
		browserInitialized = false;
	}
	
	public static String toUSDate(String BRDate) throws Exception {
        String[] dateComponents = BRDate.split("/");
        if (dateComponents != null && dateComponents.length > 0)
            return dateComponents[2] + "-" + dateComponents[1] + "-" + dateComponents[0];

        return null;
    }
	 
	public static void main(String[] args) {
		String h = "3";
		String m = "4";
		
//		String s = "201613-10";
//		try {
//			String us = Main.toUSDate(b);
//			System.out.println(us);
//		} catch (Exception e) {
//			System.out.println("retorna falso");
//		}
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		String s = "12/09/2016";
		try {
			Date d = f.parse(s);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			
			System.out.println(c.get(Calendar.DAY_OF_MONTH));
			System.out.println(c.get(Calendar.MONTH));
			System.out.println(c.get(Calendar.YEAR));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		DecimalFormat d = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
//		
//		double d1 = 12.93345;
//		System.out.println(new Double(d.format(d1)));
//		String date = "2017-01-03";
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date d = format.parse(date);
//			
//			LocalDate localDate = LocalDate.parse(date);
//			System.out.println(localDate.getDayOfMonth());
//			System.out.println(localDate.getMonthValue());
//			System.out.println(localDate.getYear());
//			Calendar c = Calendar.getInstance();
//			GregorianCalendar gc = new GregorianCalendar();
//			gc.setTime(d);
//			c.setTime(d);
//			
//			System.out.println("Day: " + gc.get(Calendar.DAY_OF_MONTH));
//			System.out.println("Month: " + gc.get(Calendar.MONTH));
//			System.out.println("yEAR: " + gc.get(Calendar.YEAR));
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		System.out.println("test: ");
//		int d = 3;
//		int m = 1;
//		int y = 2017;
//		
//		LocalDate ld = LocalDate.of(y, m, d);
//		int day = ld.getDayOfMonth();
//		int month = ld.getMonthValue();
//		int year = ld.getYear();
//		
//		String ptBR = (day < 10 ? "0"+day : day)  + "/" + (month < 10 ? "0"+month : month) + "/" + year;
//		String enUS = year  + "-" + (month < 10 ? "0"+month : month) + "-" + (day < 10 ? "0"+day : day);
//		System.out.println( "pt-BR: " + ptBR);
//		System.out.println( "en-US: " + enUS);
//		
//		LocalDate ld2 = LocalDate.parse(enUS);
//		
//		int day2 = ld2.getDayOfMonth();
//		int month2 = ld2.getMonthValue();
//		int year2 = ld2.getYear();
//		String ptBR2 = (day2 < 10 ? "0"+day2 : day2)  + "/" + (month2 < 10 ? "0"+month2 : month2) + "/" + year2;
//		System.out.println( "pt-BR: " + ptBR2);
//		Main main = new Main();
//		
//		ArrayList<Turma> turmas = new ArrayList<Turma>();
//    	String[] components = null;
//    	String period = "";//colocar um campo na view
//    	for(String fileName : filesHash.keySet()) {
//    		components = fileName.split("-");
//    		period = components[1].trim();
//    		Turma t = main.createTurma(components[0].trim(), period, components[2].trim() + " - " + components[3].trim() + " - " + components[4].trim(), 
//    				components[5].split("\\.")[0], filesHash.get(fileName));
//    		turmas.add(t);
//    	}
//    	
//    	Semestre s = new Semestre();
//		s.setTurmas(turmas);
//		if (components != null) {
//			s.setYear(yearInput.getText().trim());
//			s.setPeriod(periodInput.getText().trim() + "º");
//		}
//		String s1 = "2016-1º - TRIN003 - LÓGICA, INFORMÁTICA E COMUNICAÇÃO - 120h-TRONCO INICIAL";
//		String s2 = "2016-1º - TRIN003 - LÓGICA, INFORMÁTICA E COMUNICAÇÃO - 120h-TRONCO INICIAL";
//		
		
		
//		System.out.println(s1.equals(s2));
//		System.out.println();
//		String cpf = "06411272433";
//		String password = "tel2j05!";
//		
		SIESelenium c = new SIESelenium();
//		Excel ex = new Excel();
//		c.makeLogin(cpf, password);
//		c.goToTurmasPage();
//		
//		Excel ex = new Excel();
//		c.makeLogin(cpf, password);
//		c.goToTurmasPage();
		
		//TEMP========
		//	ArrayList<Turma> turmasOnline = c.getGroupsByYearSemester("2016", "1");

		//	ex.createExcelForGrades(turmas);

		//2016-1 - CPTA073 - REDES DE COMPUTADORES 2 - 80h-CIÊNCIA DA COMPUTAÇÃO.xlsx
		//FIM TEMP========
		
//		Main m = new Main();
//		Turma t1 = m.createTurmaFromFile("2016", "1º", "CPTA065 - REDES DE COMPUTADORES 1 - 80h", "CIÊNCIA DA COMPUTAÇÃO", new File("/Users/thiagosales/Dropbox/My_Studies/Ufal/Disciplinas/Redes/Tutoria/2016.1/2016-1º - CPTA065 - REDES DE COMPUTADORES 1 - 80h-CIÊNCIA DA COMPUTAÇÃO.xlsx"));
//		Turma t1 = m.createTurma("2016", "1", "CPTA073 - REDES DE COMPUTADORES 2 - 80h", "CIÊNCIA DA COMPUTAÇÃO");
//		Turma t2 = m.createTurma("2016", "1", "CPTA072 - PARADIGMAS DE LINGUAGENS DE PROGRAMAÇÃO - 60h", "CIÊNCIA DA COMPUTAÇÃO");
//		Turma t3 = m.createTurma("2016", "1", "TRIN003 - LÓGICA, INFORMÁTICA E COMUNICAÇÃO - 120h", "TRONCO INICIAL");

//		ArrayList<Turma> turmas = new ArrayList<Turma>();
//		turmas.add(t1);
//		turmas.add(t2);
//		turmas.add(t3);

//		Semestre s = new Semestre();
//		s.setTurmas(turmas);
//		s.setPeriod("1º");
//		s.setYear("2016");
//
//		c.saveScores(s);
//		
		
		
		
		
		//TEMP========
		//	for(Turma turma : turmas) {
		//		try {
		//			ex.readTurma(turma);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//	}


		//	c.logout();
		//TEMP========

	}
}
