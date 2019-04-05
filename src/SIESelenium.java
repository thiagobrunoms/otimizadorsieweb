

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class SIESelenium {

    private WebDriver driver;
    private String cpf;
    private String password;
    private String homeLink;
	private boolean isLoggedIn;
	private String turmasURL;
	private Hashtable<HeaderNames, Integer> maxRepeat;
	
    public SIESelenium() {
    	maxRepeat = new Hashtable<HeaderNames, Integer>();
    	maxRepeat.put(HeaderNames.PROVA_AB1, 1);
    	maxRepeat.put(HeaderNames.PROVA_AB2, 1);
    	maxRepeat.put(HeaderNames.PROVA_REPOSICAO, 2);
    	maxRepeat.put(HeaderNames.PROVA_FINAL, 3);
    }
    
    public void start() throws Exception {
    	driver = initChrome();
    }

    private WebDriver initChrome() throws Exception {
    	System.setProperty("webdriver.chrome.driver", "chromedriver");
//    	System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        return new ChromeDriver();
    }
    
    public void closeBrowser() {
    	this.driver.quit();
	}
    
    public boolean isLoggedIn() {
    	return isLoggedIn;
    }
    
    public void setCpf(String cpf) {
    	this.cpf = cpf;
    }
    
    public void setPass(String password) {
    	this.password = password;
    }
    
    public void makeLogin() throws NoSuchElementException {
		driver.get("https://sistemas.ufal.br/academico/login.seam");
		System.out.println("Realizou o get da página.");
		WebElement element = driver.findElement(By.id("loginForm:username"));
		if (element != null) {
			element.clear();
			element.sendKeys(cpf);
			element = driver.findElement(By.id("loginForm:password"));
			if(element != null) {
				element.clear();
				element.sendKeys(password);
				element = driver.findElement(By.id("loginForm:entrar"));
				element.click();
			}
			
			element = driver.findElement(By.linkText("Sair"));
			if (element != null) {
				System.out.println("Login realizado com sucesso!");
				turmasURL = driver.getCurrentUrl();
				isLoggedIn = true;
			} else {
				System.out.println("Login não foi realizado! Verifique usuário e senha!");
				isLoggedIn = false;
			}
			
		}
    }
    
    public void goToTurmasPage() {
    	driver.get("https://sistemas.ufal.br/academico/diariodeclasse/listaturmas.seam?cid=");
    }
    
    
    //TODO Retornar STATUS, pra informar quais turmas foram cadastradas com sucesso, e quais não.
    public boolean defineNewClassPlans(Semestre semester, Main.PERIOD_TYPES periodType) throws NoSuchElementException { //TODO Melhorar os tratamentos de excecoes
    	System.out.println("METHOD: defineNewClassPlans ");
    	Semestre onlineSemester = this.getTurmasData(semester.getYear(), semester.getPeriod());
    	
    	ArrayList<Turma> onlineTurmas = onlineSemester.getTurmas();
    	ArrayList<Turma> turmas = semester.getTurmas();
    	WebElement element = null;
    	
    	for(Turma eachTurmaToSave : turmas) {
    		Turma currentTurma = null;
    		for(Turma eachTurmaOnline : onlineTurmas) {
    			if (eachTurmaOnline.equals(eachTurmaToSave)) {
    				System.out.println("Encontrou turma igual");
    				goToSpecificGroupPage(eachTurmaOnline, Turma.TURMAS_DATA.PLANO_DE_CURSO);
    				currentTurma = eachTurmaToSave;
    				break;
    			}
    		}
    		
    		if (currentTurma != null) {
    		//Verifica se é alteração ou cadastro de novo plano
	    		boolean editContent = false;
	    		WebElement buttonOk = null;
	    		try {
	    			buttonOk = driver.findElement(By.id("cadastroEmenta:inserirEmenta"));
	    		} catch (NoSuchElementException e) {
	    			buttonOk = driver.findElement(By.id("cadastroEmenta:alterarEmenta"));//TODO Se houver uma terceira opcao, o ideal eh fazer novas checagem de excecoes desse tipo
	    			editContent = true;
	    		}
	    		
    		
    			element = driver.findElement(By.id("cadastroEmenta:objetivos"));
    			if (editContent)
    				element.clear();
        		element.sendKeys(currentTurma.getGoals());
        		
        		element = driver.findElement(By.id("cadastroEmenta:metodologia"));
        		if (editContent)
    				element.clear();
        		element.sendKeys(currentTurma.getMethods());
        		
        		element = driver.findElement(By.id("cadastroEmenta:avaliacao"));
        		if (editContent)
    				element.clear();
        		element.sendKeys(currentTurma.getEvaluation());
        		
        		element = driver.findElement(By.id("cadastroEmenta:referencias"));
        		if (editContent)
    				element.clear();
        		String references = "";
        		for(String eachReference : currentTurma.getReferences()) {
        			references = references + eachReference + "\n";
        		}
        		element.sendKeys(references);
        		
        		if (buttonOk != null)
        			buttonOk.click();
        		
        		element = driver.findElement(By.className("rich-messages-label"));
        		if (element.getText().contains("Plano de curso cadastrado com sucesso"))
        			System.out.println("PLANO DE CURSO DA TURMA " + currentTurma.getFullName() + " CADASTRADO COM SUCESSO!");
        		//TODO Adicionar status ok pra essa turma
    		} else
    			System.out.println("Não encontrou turma igual...");
    		
    	}
    	
    	return true; //TODO

    }
    
    private Semestre parseTurmaData(Semestre s, WebElement element) {
    	List<WebElement> trs = element.findElements(By.tagName("tr"));
		System.out.println("Listando TRs e TDs");
		for(WebElement eachRow : trs) {
			List<WebElement> tds = eachRow.findElements(By.tagName("td"));
			int tdIndex = 0;
			Turma turma = new Turma();
			
			System.out.println("Começando nova turma...");
			for(WebElement eachTds : tds) {
				System.out.println("tdSize = " + tdIndex + " - texto atual: " + eachTds.getText() + " - ano ==  " + s.getYear()+ " == " + eachTds.getText().contains(s.getYear()) + 
						" - semestre = " + s.getPeriod() + " == " + eachTds.getText().contains(s.getPeriod()));
				if(tdIndex == 0 && (!eachTds.getText().contains(s.getYear()) || !eachTds.getText().contains(s.getPeriod())) ) {
					System.out.println("semestre: " + eachTds.getText());
					break;
				} else if (tdIndex == 1) {
					System.out.println("curso: " + eachTds.getText());
					turma.setCourse(eachTds.getText());
				} else if(tdIndex == 2) {
					System.out.println("classe: " + eachTds.getText());
					turma.setClassName(eachTds.getText().trim());
				} else if (tdIndex == 3) { 
					System.out.println("turma: " +eachTds.getText());
					turma.setGroup(eachTds.getText());
				} else if(tdIndex == 4) {
					System.out.println("Listando hrefs e nomes de links...");
					List<WebElement> linkElements = eachTds.findElements(By.tagName("a"));
					for(WebElement eachLink : linkElements) {
						String linkName = eachLink.getText();
						System.out.println("linkname: " + linkName);
						
						if (linkName.equals("Plano de Curso"))
							turma.setLink(Turma.TURMAS_DATA.PLANO_DE_CURSO, eachLink.getAttribute("href"));
						else if (linkName.equals("Frequência"))
							turma.setLink(Turma.TURMAS_DATA.FREQUENCIA, eachLink.getAttribute("href"));
						else if (linkName.equals("Ata"))
							turma.setLink(Turma.TURMAS_DATA.ATA, eachLink.getAttribute("href"));
						else if (linkName.equals("Nota"))
							turma.setLink(Turma.TURMAS_DATA.NOTA, eachLink.getAttribute("href"));
						else if (linkName.equals("Pagela"))
							turma.setLink(Turma.TURMAS_DATA.PAGELA, eachLink.getAttribute("href"));
						
					}
					
					System.out.println("ADICIONANDO TURMAS LIDA ONLINE ABAIXO: ");
					System.out.println(turma);
					s.addNewTurma(turma);
				}
				
				turma.setYear(s.getYear());
				turma.setSemester(s.getPeriod());
				
				tdIndex++;//gambiarra! como nao esta pegando pelo atributo do td, o jeito foi contar os tds...
			}
			
		}
		
		System.out.println("LEU TODAS AS TURMAS");
		for(Turma t : s.getTurmas()) {
			System.out.println(t);
		}
		
		return s;
    }
     
    public Semestre getTurmasData(String year, String semester) throws NoSuchElementException {
    	this.goToTurmasPage();
    	Semestre s = new Semestre();
    	s.setYear(year);
    	s.setPeriod(semester);
   
//    	WebElement element = driver.findElement(By.id("dataTableTurmasAnteriores:tb"));
    	WebElement element = driver.findElement(By.id("j_id218:tb"));
    	s = parseTurmaData(s, element);
		
		return s;
    }
    
    //TODO melhorar isso, para que ele possa ir para qualquer pag: ex. Plano de Curso
    public void goToSpecificGroupPage(Turma turma, Turma.TURMAS_DATA type_of_link) {
    	System.out.println("Acessando link " + type_of_link + " de uma turma");
    	System.out.println(turma.getLinksHashTable().get(type_of_link));
    	System.out.println(turma);
    	driver.get(turma.getLinksHashTable().get(type_of_link));
    }
    
    private List<WebElement> getAllStudentesFromGroup() {
    	driver.switchTo().defaultContent();
    	WebElement studentTable = driver.findElement(By.id("formNota:notasDatatable:tb"));
    	System.out.println("Pegou a tabela de alunos da turma..");
    	List<WebElement> allStudents = null;
    	
    	if (studentTable != null) {
    		allStudents = studentTable.findElements(By.tagName("tr"));
    	} else {
    		System.out.println("Tabela nula...");
    	}
    	
    	return allStudents;
    }
    
    private void getAllStudentesFromGroup2(Turma eachTurmaToSave, int flagForRepositionAndFinal) {
    	driver.switchTo().defaultContent();
    	WebElement studentTable = driver.findElement(By.id("formNota:notasDatatable:tb"));
    	System.out.println("Pegou a tabela de alunos da turma..");
    	List<WebElement> allStudents = null;
    	
//    	ArrayList<Student> students = eachTurmaToSave.getStudents();
//    	for(Student student : students) {
//    		WebElement matriculaElement = studentTable.getText()
//    	}
    	
    	System.out.println("OBTENDO REFERENCIAS DOS CAMPOS DE NOVA FORMA!");
    	if (studentTable != null) {
    		allStudents = studentTable.findElements(By.tagName("tr"));
    		HashMap<String, Student> studentsHash = eachTurmaToSave.getStudentsHash();
    		int studentCount = 0;
    		for(WebElement eachTr : allStudents) {
    			String matricula = eachTr.findElement(By.id("formNota:notasDatatable:"+studentCount+":j_id244")).getText();
    			
    			Student s = studentsHash.get(matricula);
    			if (s != null) {
    				StudentWebElements webElements = null;
    				switch (flagForRepositionAndFinal) {

					case 0:
						System.out.println("Adicionando notas da ABI e ABII...");
						WebElement ab1 = eachTr.findElement(By.id("formNota:notasDatatable:"+studentCount+":j_id250")).
						findElement(By.id("formNota:notasDatatable:"+studentCount+":AB1Input"));
				
						WebElement ab2 = eachTr.findElement(By.id("formNota:notasDatatable:"+studentCount+":j_id252")).
						findElement(By.id("formNota:notasDatatable:"+studentCount+":AB2Input"));
						
						webElements = new StudentWebElements(ab1, ab2, null, null);
						break;

					case 1:
						System.out.println("Adicionando notas da reposição e final...");
						WebElement reposicao = eachTr.findElement(By.id("formNota:notasDatatable:"+studentCount+":j_id254")).
						findElement(By.id("formNota:notasDatatable:"+studentCount+":RAInput"));
				
						WebElement finalp = eachTr.findElement(By.id("formNota:notasDatatable:"+studentCount+":j_id256")).
						findElement(By.id("formNota:notasDatatable:"+studentCount+":PFInput"));
						webElements = new StudentWebElements(null, null, reposicao, finalp);
						
						break;
					}
    				
    				s.setStudentWebElements(webElements);
    				studentCount++;
    			} else
    				System.out.println("nao encontrou estudante com essa matricula em hash...");
    		}

    	} else {
    		System.out.println("Tabela nula...");
    	}
    }
    
    //limited group list returns students' code and names, only
    public ArrayList<Student> createLimitedGroupList(Turma turma) {
    	this.goToSpecificGroupPage(turma, Turma.TURMAS_DATA.NOTA);
    	ArrayList<Student> students = turma.getStudents();
    	List<WebElement> allStudents = this.getAllStudentesFromGroup();

    	if (allStudents != null) {
    		int studentsIndex = 0;
    		for(WebElement eachStudent : allStudents) {
    			Student student = new Student();
    			System.out.println(eachStudent.getText());
    			List<WebElement> eachStudentData = eachStudent.findElements(By.tagName("td"));
    			int tdIndex = 0;

    			System.out.println("lendo campos: ");
    			System.out.println("formNota:notasDatatable:"+ studentsIndex +":AB1Input");
    			System.out.println("formNota:notasDatatable:"+ studentsIndex +":AB2Input");
    			System.out.println("formNota:notasDatatable:"+ studentsIndex +":RAInput");
    			System.out.println("formNota:notasDatatable:"+ studentsIndex +":PFInput");
    			
    			for(WebElement data : eachStudentData) {
    				if (tdIndex == 1)
    					student.setCode(data.getText());
    				else if(tdIndex == 2)
    					student.setName(data.getText());
    				else if(tdIndex == 3) {
    					System.out.println("Pegando ABI");
    					WebElement x = data.findElement(By.id("formNota:notasDatatable:"+ studentsIndex +":AB1Input"));
    					System.out.println(x);
    					student.setScore1(data.findElement(By.id("formNota:notasDatatable:"+ studentsIndex +":AB1Input")).getAttribute("value"));
    				} else if(tdIndex == 4)
    					student.setScore2(data.findElement(By.id("formNota:notasDatatable:"+ studentsIndex +":AB2Input")).getAttribute("value"));
    				else if(tdIndex == 5)
    					student.setScoreReposition(data.findElement(By.id("formNota:notasDatatable:"+ studentsIndex +":RAInput")).getAttribute("value"));
    				else if(tdIndex == 6)
    					student.setScoreFinal(data.findElement(By.id("formNota:notasDatatable:"+ studentsIndex +":PFInput")).getAttribute("value"));

    				tdIndex++;
    			}
    			
    			System.out.println("ALUNO LIDO DA PAGINA: ");
				System.out.println(student);

				studentsIndex++;
    			students.add(student);
    		}
    	}
    	
    	return students;
    }
 
    
    public ArrayList<Turma> getGroupsByYearSemester(String year, String semester) throws NoSuchElementException {
    	System.out.println("obtendo semestre para turmas por ano e semestre");
    	Semestre s = this.getTurmasData(year, semester);
		
    	System.out.println("SEMESTRE LIDO: " + s);
    	
    	System.out.println("obtendo turmas do semestre");
		ArrayList<Turma> turmas = s.getTurmas();
		
		for(Turma t : turmas) {
			System.out.println("=========== Listando Turma ============");
			System.out.println(t);
			Hashtable<Turma.TURMAS_DATA, String> links = t.getLinksHashTable();
			for(String linkElement : links.values()) {
				System.out.println(linkElement);
			}
			
			System.out.println("ESTUDANTES DA TURMA");
			ArrayList<Student> students = this.createLimitedGroupList(t);
			for(Student std : students) {
				System.out.println(std);
			}
			
			t.setStudents(students);

		}
		
		return turmas;
    }
    
    private void pressButtonSave(String turmaName) {
    	WebElement saveGradesElement = driver.findElement(By.id("formNota:btnSalvar"));
		if (saveGradesElement != null) {
			saveGradesElement.click();
			saveGradesElement = driver.findElement(By.className("rich-messages-label"));
			if (saveGradesElement != null && saveGradesElement.getText().equals("Notas registradas com sucesso."))
				System.out.println("Notas de " + turmaName + " salvas com sucesso...");
				//TODO Adicionar status de sucesso
		}
    }
    
    public boolean saveScores2(Semestre semester) {
    	System.out.println("salvando notas... " + semester.getPeriod() + " - " + semester.getYear());
    	
    	Semestre semesterOnline = this.getTurmasData(semester.getYear(), semester.getPeriod());
    	ArrayList<Turma> turmasOnline = semesterOnline.getTurmas();
    	ArrayList<Turma> turmasToSave = semester.getTurmas();
    	
    	for(Turma eachTurmaToSave : turmasToSave) {
    		System.out.println("TURMAS ONLINE: " + turmasOnline.size());
    		
    		for(Turma eachTurmaOnline : turmasOnline) {
    			System.out.println("COMPARANDO TURMAS");
    			System.out.println(eachTurmaOnline);
    			System.out.println(eachTurmaToSave);
    			if (eachTurmaToSave.equals(eachTurmaOnline)) {
    				eachTurmaToSave.setLinksHashTable(eachTurmaOnline.getLinksHashTable());
    				break;
    			}
    		}
    		
    		WebElement notaElement = null;
    		if (turmasOnline.size() > 0) {
    			this.goToSpecificGroupPage(eachTurmaToSave, Turma.TURMAS_DATA.NOTA);
        		System.out.println("obtendo lista de alunos...");
        		this.getAllStudentesFromGroup2(eachTurmaToSave, 0);
        		
        		System.out.println("Salvando ABS...");
        		for(Student s : eachTurmaToSave.getStudentsHash().values()) {
        			StudentWebElements elements = s.getStudentWebElements();
        			
        			notaElement = elements.getAb1();
        			if (!notaElement.getAttribute("value").equals(""))
        				notaElement.clear();
        			
        			notaElement.sendKeys(s.getScore1());
        			
        			notaElement = elements.getAb2();
        			if (!notaElement.getAttribute("value").equals(""))
        				notaElement.clear();
        			
        			notaElement.sendKeys(s.getScore2());
        		}
        		
        		pressButtonSave(eachTurmaToSave.getFullName());
        		this.getAllStudentesFromGroup2(eachTurmaToSave, 1);
        		//TODO melhorar!
        		System.out.println("Salvando reps e fins...se necessario!");
        		for(Student s : eachTurmaToSave.getStudentsHash().values()) {
        			StudentWebElements elements = s.getStudentWebElements();
        			notaElement = elements.getReposicao();
        			System.out.println("reposicao: " + notaElement.isEnabled());
        			if (notaElement.isEnabled()) {
        				System.out.println("value: " + notaElement.getAttribute("value"));
        				if (!notaElement.getAttribute("value").equals("")) {
        					notaElement.clear();
        				}
        				
        				notaElement.sendKeys(s.getScoreReposition());
        			}
        			
        			notaElement = elements.getFinalp();
        			System.out.println("final: " + notaElement.isEnabled());
        			if (notaElement.isEnabled()) {
        				if (!notaElement.getAttribute("value").equals("")) {
        					notaElement.clear();
        				}
        				
        				notaElement.sendKeys(s.getScoreFinal()); 
        			}
        			
        			
        		}
        		
        		pressButtonSave(eachTurmaToSave.getFullName());
    		} else
    			return false;
    		
    	} 
    	
    	return true;
    	
    }
    
    public boolean saveScores(Semestre semester) {
    	System.out.println("salvando notas... " + semester.getPeriod() + " - " + semester.getYear());
    	
    	Semestre semesterOnline = this.getTurmasData(semester.getYear(), semester.getPeriod());
    	ArrayList<Turma> turmasOnline = semesterOnline.getTurmas();
    	ArrayList<Turma> turmasToSave = semester.getTurmas();
    	
    	for(Turma eachTurmaToSave : turmasToSave) {
    		System.out.println("TURMAS ONLINE: " + turmasOnline.size());
    		
    		for(Turma eachTurmaOnline : turmasOnline) {
    			System.out.println("COMPARANDO TURMAS");
    			System.out.println(eachTurmaOnline);
    			System.out.println(eachTurmaToSave);
    			if (eachTurmaToSave.equals(eachTurmaOnline)) {
    				eachTurmaToSave.setLinksHashTable(eachTurmaOnline.getLinksHashTable());
    				break;
    			}
    		}
    		
    		if (turmasOnline.size() > 0) {
    			this.goToSpecificGroupPage(eachTurmaToSave, Turma.TURMAS_DATA.NOTA);
        		System.out.println("obtendo lista de alunos...");
        		List<WebElement> allStudents = this.getAllStudentesFromGroup();
        		System.out.println("alunos recuperados " + allStudents);
        		
        		if (allStudents != null) {
        			int maxLoop = maxRepeat.get(eachTurmaToSave.getMaxData());
        			for (int i=0; i < maxLoop; i++ ) {
        				System.out.println("COMEÇANDO... CONTINUANDO.... para " + eachTurmaToSave.getFullName());
        				for(WebElement eachStudent : allStudents) {
            				System.out.println("Pegando dados de cada aluno...");
            				List<WebElement> eachStudentData = eachStudent.findElements(By.tagName("td"));
            				int tdIndex = 0;

            				String code = "";
            				String name = "";
            				WebElement ab1 = null;
            				WebElement ab2 = null;
            				WebElement rep = null;//verificar se o campo esta editavel
            				WebElement provaFinal = null;//verificar se o campo esta editavel
            				for(WebElement data : eachStudentData) {
            					if (tdIndex == 1)
            						code = data.getText();
            					else if(tdIndex == 2)
            						name = data.getText();
            					else if(tdIndex == 3)
            						ab1 = data.findElement(By.tagName("input"));
            					else if(tdIndex == 4)
            						ab2 = data.findElement(By.tagName("input"));
            					else if(tdIndex == 5)
            						rep = data.findElement(By.tagName("input"));
            					else if(tdIndex == 6)
            						provaFinal = data.findElement(By.tagName("input"));
            					
            					tdIndex++;
            				}
            				
            				ArrayList<Student> students = eachTurmaToSave.getStudents();
            				String currentABScore;
            				for(Student studentObj : students) {
            					if (studentObj.getCode().equals(code)) {
            						System.out.println("Setando notas de " + studentObj.getName());
            						
            						currentABScore = studentObj.getScore1();
            						if(i == 0 && ab1.isEnabled() && currentABScore.length() > 0)
            							ab1.sendKeys(currentABScore);
            						
            						currentABScore = studentObj.getScore2();
            						if(i == 0 && ab2.isEnabled() && currentABScore.length() > 0)
            							ab2.sendKeys(currentABScore);
            						
            						System.out.println("antes da reposicao i = " + i);
            						String repositionOrFinal = "";
            						if (i == 1) {
            							System.out.println("ENTROU PARA REPOSICAO");
            							repositionOrFinal = studentObj.getScoreReposition();
                						if (repositionOrFinal.length() > 0) {
                							if (rep.isEnabled()) {
                								rep.sendKeys(repositionOrFinal);
                							} else {
                								System.out.println("nao esta enabled!");
                							}
                						} else {
                							System.out.println("repositionOrFinal.length() " + repositionOrFinal.length());
                						}
            						}
            						
            						if (i == 2) {
            							System.out.println("ENTROU PARA FINAL");
            							repositionOrFinal = studentObj.getScoreFinal();
                						if (repositionOrFinal.length() > 0) {
                							if (provaFinal.isEnabled()) {
                								provaFinal.sendKeys(repositionOrFinal);
                							}
                						}	
            						}
            						
            						students.remove(studentObj);
            						break;
            					}
            				}	
            			}
            			
            			WebElement saveGradesElement = driver.findElement(By.id("formNota:btnSalvar"));
            			if (saveGradesElement != null) {
            				saveGradesElement.click();
            				saveGradesElement = driver.findElement(By.className("rich-messages-label"));
            				if (saveGradesElement != null && saveGradesElement.getText().equals("Notas registradas com sucesso.") && i+1 == maxLoop)
            					System.out.println("Notas de " + eachTurmaToSave.getFullName() + " salvas com sucesso...");
            					//TODO Adicionar status de sucesso
            			}
            			
            			if (i+1 < maxLoop) {
        					System.out.println("PEGANDO NOVAS REFERENCIAS WEBELEMENT ESTA TURMA " + eachTurmaToSave.getFullName() + ", UMA VEZ QUE AINDA HA NOTAS...");
        					allStudents = this.getAllStudentesFromGroup();
        				}
        			}
        			
        		} else {
        			System.out.println("lista de alunos nula...");
        		}
    		} else
    			return false;
    		
    	} 
    	
    	return true;
    	
    }
    
    public Semestre downloadCPFromSIE(Semestre semestre) {
    	ArrayList<Turma> turmas = semestre.getTurmas();
    	
    	for(Turma eachTurma : turmas) {
    		System.out.println("Fazendo download do Plano de Aulas da turma " + eachTurma.getFullName());
    		goToSpecificGroupPage(eachTurma, Turma.TURMAS_DATA.PLANO_DE_CURSO);
    		
    		WebElement element = driver.findElement(By.id("cadastroEmenta:objetivos"));
			eachTurma.setGoals(element.getAttribute("value"));
    		
    		element = driver.findElement(By.id("cadastroEmenta:metodologia"));
    		eachTurma.setMethods(element.getAttribute("value"));
    		
    		element = driver.findElement(By.id("cadastroEmenta:avaliacao"));
    		eachTurma.setEvaluation(element.getAttribute("value"));
    		
    		element = driver.findElement(By.id("cadastroEmenta:referencias"));
    		String references = element.getAttribute("value");
    		eachTurma.addReference(references);
    		
    		System.out.println("PLANO DE AULA LIDO: ");
    		System.out.println("OBJETIVOS: " + eachTurma.getGoals());
    		System.out.println("MEOTODOLOGIA: " + eachTurma.getMethods());
    		System.out.println("AVALIACAO: " + eachTurma.getEvaluation());
    		System.out.println("REFERENCIAS: " + eachTurma.getReferences().get(0));
    	}
    	
    	return semestre;
    	
    }
    
    public boolean logout(int attempts) {
    	WebElement element = driver.findElement(By.linkText("Sair"));
    	
    	if(element != null) {
    		element.click();
    		element = driver.findElement(By.id("loginForm:username"));
    		if (element != null) {
    			isLoggedIn = false;
    			return true;
    		}
    	}
    	if (attempts <= 3)
    		logout(++attempts);
    	
    	return false;
    }

    public static void main(String[] args) {
    	SIESelenium s = new SIESelenium();
    	try {
			s.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
//    	System.out.println(Calendar.getInstance().get(Calendar.YEAR));
//    	String plan = "PLANO-CPTA054 - PROGRAMAÇÃO 2 - 60h-CIÊNCIA DA COMPUTAÇÃO-A.xlsx";
//    	String components[] = plan.split("-");
//    	String discipline = components[1].trim() + " - " + components[2].trim() + " - " + components[3].trim() + "-" + components[4] + "-" + components[5].split("\\.")[0];
//    	System.out.println(discipline);
//    	
//    	
//    	String plan2 = "PLANO-2016-1º - CPTA054 - PROGRAMAÇÃO 2 - 60h-CIÊNCIA DA COMPUTAÇÃO-T.xlsx";
//    	String components2[] = plan2.split("-");
//    	String discipline2 = components2[3].trim() + " - " + components2[4].trim() + " - " + components2[5].trim() + "-" + components2[6].trim() + "-" + components2[7].split("\\.")[0];
//    	System.out.println(discipline2);
//   		SIESelenium c = new SIESelenium();
//   		try {
//			c.start();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//   		ArrayList<Turma> turmas = c.getGroupsByYearSemester("2016", "1");
//		
//		FilesManager ex = new FilesManager();
//		ex.createExcelForGrades(turmas, "path...");
//	
//		Turma turmaTeste = new Turma();
//		turmaTeste.setYear("2016");
//		turmaTeste.setSemester("1");
//		turmaTeste.setClassName("CPTA054 - PROGRAMAÇÃO 2 - 60h");
//		turmaTeste.setCourse("CIÊNCIA DA COMPUTAÇÃO");
//		
//		try {
//			ArrayList<Student> students = ex.readTurma(turmaTeste);
//			for(Student s : students) {
//				System.out.println(s);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for(Turma turma : turmas) {
//			try {
//				ex.readTurma(turma);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		
//		c.logout();
		
		

	}
	
}
