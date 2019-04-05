package julia;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;



public class Main {
	private WebDriver driver;
	private final String sigaURL = "https://www.sympla.com.br/devfest-maceio-2017__158502?token=e8698c6343e27e926da0551c90816b56";
	
    public Main() throws Exception {
    	start();
    }
    
    public void start() throws Exception {
    	driver = initChrome();
    }

    private WebDriver initChrome() throws Exception {
    	System.setProperty("webdriver.chrome.driver", "chromedriver");
        return new ChromeDriver();
    }
    
    public void openURL() {
    	driver.get(sigaURL);
    }
	
    public void addNew(List<Person> persons) {
    	for(Person p : persons) {
    		this.openURL();
    		
        	WebElement element = driver.findElement(By.className("icon-plus-circle"));
        	List<WebElement> elements = driver.findElements(By.className("icon-plus-circle"));
        	elements.get(1).click();
        	
        	element = driver.findElement(By.id("btnContinue"));
        	element.click();
        	
        	element = driver.findElement(By.id("customFormField_firstName_0"));
        	element.sendKeys(p.getName());
        	
        	element = driver.findElement(By.id("customFormField_lastName_0"));
        	element.sendKeys(p.getSurname());
        	
        	element = driver.findElement(By.id("customFormField_Email_0"));
        	element.sendKeys(p.getEmail());
        	
        	element = driver.findElement(By.id("customFormField_203234_1_0"));
        	element.sendKeys(p.getDdd());
        	
        	element = driver.findElement(By.id("customFormField_203234_2_0"));
        	element.sendKeys(p.getPhone());

        	element = driver.findElement(By.id("customFormField_203235_0"));
        	element.sendKeys(p.getEmployee());
        	
        	element = driver.findElement(By.id("customFormField_237623_0"));
        	element.sendKeys(p.getCode());
        	
        	element = driver.findElement(By.id("customFormField_203236_0"));
        	element.sendKeys(p.getCity());
        	
        	//Copy
        	element = driver.findElement(By.id("FreeOrder_FIRST_NAME"));
        	element.sendKeys(p.getName());
        	
        	element = driver.findElement(By.id("FreeOrder_LAST_NAME"));
        	element.sendKeys(p.getSurname());
        	
        	element = driver.findElement(By.id("FreeOrder_EMAIL"));
        	element.sendKeys(p.getEmail());
        	
        	element = driver.findElement(By.id("FreeOrder_confirmEmail"));
        	element.sendKeys(p.getEmail());
        	
        	element = driver.findElement(By.id("buttonFree"));
        	if (element != null)
        		System.out.println("Agora eh so confirmar");
        	else
        		System.out.println("confirmacao nao encontrada...");
        	
    	}
    	
    }
    	
    public static void main(String[] args) {
		Main m;
		try {
			m = new Main();
			
			ArrayList<Person> persons = new ArrayList<>();
			persons.add(new Person("José Junio", "Calú", "junio.calu@gmail.com", "82", "996018672", "Estudante - UFAL - Campus Arapiraca.", "123123", "Arapiraca/AL"));
			m.addNew(persons);
//			m.makeLogin();
//			m.goToProjectsPage();
//			
//			FileInputStream fis = new FileInputStream("/Users/thiagodesales/Documents/workspace-sts-3.8.3.RELEASE/SWO/participantes2.xlsx");
//			Workbook workgroup = new XSSFWorkbook(fis);
//			
//			Sheet sheet = workgroup.getSheetAt(0);
//			
//			Iterator<Row> rowIter = sheet.iterator();
//			rowIter.next();//title
//			String cpf = "";
//			while(rowIter.hasNext()) {
//				Row currentRow = rowIter.next();
//				currentRow.getCell(1).setCellType(CellType.STRING);
//				cpf = currentRow.getCell(1).getStringCellValue();
//				System.out.println("Cadastrando participante " + cpf);
//				m.addParticipant(cpf);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
}

