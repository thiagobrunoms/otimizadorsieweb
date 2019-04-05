package aracomp;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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

import com.graphbuilder.curve.Point;


public class Main {
	private WebDriver driver;
	private final String sigaURL = "http://sigaa.sig.ufal.br/sigaa/verTelaLogin.do";
	
    public Main() throws Exception {
    	start();
    }
    
    public String doRemoteRequest(String httpMethod, String urlToDownload, long timeout, String codeToInjectBefore, Map<String, String> formData, String contentType, int formDataFormat) {
        if (urlToDownload.equals("")) return "";
        if (driver instanceof JavascriptExecutor) {
        	driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
            String response = "";
            try {
                String formDataCode = "";
                String formDataVar = "";
                if (formData != null) {
                    // Don't use 0 as data format, it is reserved for overloaded methods
                    if (formDataFormat == 1) {
                        formDataVar = "formDataXYZ";
                        formDataCode = "var " + formDataVar + " = new FormData(); \n";
                        String appendCode = "";
                        for (Map.Entry<String, String> entry : formData.entrySet()) {
                            appendCode = appendCode + formDataVar + ".append('" + entry.getKey() + "', " + entry.getValue() + "); \n";
                        }
                        formDataCode = formDataCode + appendCode + "\n";
                    } else if (formDataFormat == 2) {
                        String appendCode = "";
                        for (Map.Entry<String, String> entry : formData.entrySet()) {
                            appendCode = appendCode + entry.getKey() + "=" + entry.getValue() + "&";
                        }
                        formDataVar = "'" + appendCode.substring(0, appendCode.length()-1) + "'";
                    }
                }

                // TODO: make doRemoteRequest support sending request with a specific charset, part of this is already implement
                //				String charset = "charset=ISO-8859-1";
                String charset = "";
                if (!charset.equals("")) {
                    charset = "; " + charset;
                }
                // Example: xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded; charset=ISO-8859-1')
                if (!contentType.equals("")) {
                    contentType = "xhr.setRequestHeader('Content-Type', '" + contentType + charset + "');";
                } else {
                    //					contentType = "xhr.setRequestHeader('Content-Type', '" + charset + "');";
                }

                //				System.out.println(contentType);

                String completeCode = codeToInjectBefore +
                        "var callback = arguments[arguments.length - 1];" +
                        "var xhr = new XMLHttpRequest();" +
                        "xhr.open('" + httpMethod + "', " + "'"+urlToDownload + "'" + ", true);" +
                        contentType +
                        "xhr.onreadystatechange = function() {" +
                        "  if ((xhr.readyState == 4) && (xhr.status==200)) {" +
                        //					       "     console.log(xhr.responseText);" +
                        //						   "     console.log(xhr.getResponseHeader('Content-Type'));" +
                        "     callback(xhr.responseText);" +
                        "  }; " +
                        "}; " +
                        formDataCode +
                        "xhr.send(" + formDataVar + ");";
                			
                System.out.println("Complete code:");
                System.out.println(completeCode);
                response = (String)((JavascriptExecutor) driver).executeAsyncScript(completeCode);
            } catch (org.openqa.selenium.TimeoutException e) {
                // TODO: better handling when a timeout occurs while executing a doRemoteDownload
                // Maybe return null?
                System.out.println("Timeout of " + timeout + " reached while attempting to get " + urlToDownload);
                //				return "ERROR DOWNLOADING CONTENT";
                return "";
            }
            //		    try {
            //		    	Charset iso88591charset = Charset.forName("ISO-8859-1");
            //		    	ByteBuffer inputBuffer = ByteBuffer.wrap(new byte[]{(byte)0xC3, (byte)0xA2});
            //		    	ByteBuffer outputBuffer = iso88591charset.encode((String)response);
            //		    	String responseStr = new String(outputBuffer.array());
            //				System.out.println(new String(((String)response).getBytes(Charset.forName("UTF-8"))));
            return new String(((String)response).getBytes(Charset.forName("ISO-8859-1")));
            //				System.out.println(responseStr);
            //				return responseStr;
            //			} catch (UnsupportedEncodingException e) {
            //				e.printStackTrace();
            //			}
        }
        return "";
    }
    
    public void start() throws Exception {
    	driver = initChrome();
    }

    private WebDriver initChrome() throws Exception {
    	System.setProperty("webdriver.chrome.driver", "chromedriver");
        return new ChromeDriver();
    }
    
    public void openSIGAAURL() {
    	driver.get(sigaURL);
    }
    
//    Map<String, String> formData = new HashMap<String, String>();
//	formData.put("menu:j_id_jsp_2030544319_3", "menu:j_id_jsp_2030544319_3" );
//    formData.put("jscook_action", "menu_j_id_jsp_2030544319_3_j_id_jsp_2030544319_4_menu:A]#{listaAtividadesParticipantesExtensaoMBean.listarAtividadesComParticipantesCoordenador}");
//    formData.put("javax.faces.ViewState", "j_id2");
//	
//	this.doRemoteRequest("POST", "http://sigaa.sig.ufal.br/sigaa/portais/docente/docente.jsf", 1000, "", formData, "application/x-www-form-urlencoded", 2);
	
    public void goToProjectsPage() {
    	WebElement element = driver.findElement(By.linkText("Menu Docente"));
    	System.out.println("1");
    	if (element != null) 
    		element.click();
    	
    	System.out.println("2");
    	

    	List<WebElement> elementy = driver.findElements(By.tagName("span"));
    	if (elementy.size() == 0) return;
    	
    	WebElement elementx = null;
    	for(WebElement e : elementy) {
    		if(e.getText().equals("Extensão")) {
    			System.out.println("3");
    			Actions action = new Actions(driver);
    			action.moveToElement(e, e.getLocation().x, e.getLocation().y).build().perform();
    			
    			Robot r;
				try {
					r = new Robot();
					r.mouseMove(255, 180);
					r.mousePress(InputEvent.BUTTON1_MASK);
//					r.mouseRelease(InputEvent.BUTTON1_MASK);
					
					r.mouseMove(255, 195);
					Thread.sleep(1000);
					r.mousePress(InputEvent.BUTTON1_MASK);
					r.mouseRelease(InputEvent.BUTTON1_MASK);
					
					Thread.sleep(1000);
					r.mouseMove(255, 220);
					r.mousePress(InputEvent.BUTTON1_MASK);
					r.mouseRelease(InputEvent.BUTTON1_MASK);
					
					Thread.sleep(1000);
					r.mouseMove(600, 260);
					r.mousePress(InputEvent.BUTTON1_MASK);
					r.mouseRelease(InputEvent.BUTTON1_MASK);
					
					Thread.sleep(1000);
					r.mouseMove(785, 300);
					r.mousePress(InputEvent.BUTTON1_MASK);
					r.mouseRelease(InputEvent.BUTTON1_MASK);
					
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
		    	element = driver.findElement(By.id("formListaCursosEventoParaGerenciarAparticipantes:cmdLinkInscritosAtividade"));
		    	element.click();
    	        
    			return;
    		}
    	}
    }
    
    public void addParticipant(String cpf) {
    	
    	WebElement element;
    	element = driver.findElement(By.linkText("Adicionar Novo Participante"));
    	element.click();
        
    	element = driver.findElement(By.id("formBuscaPadraoParticipantesExtensao:cpf"));
    	if (element != null)
    		element.sendKeys(cpf);
    	
    	element = driver.findElement(By.id("formBuscaPadraoParticipantesExtensao:cmdBucarParticipante"));
    	if (element != null)
    		element.click();
    	
    	try {
    		element = driver.findElement(By.id("painel-erros"));
        	if (element != null) {
        		element = element.findElement(By.tagName("ul")).findElement(By.tagName("li"));
        		if (element.getText().equals("Nenhum cadastro foi encontrado com essas informações.")) {
        			System.out.println("-----> Problema com " + cpf + ": " + element.getText());
        			driver.navigate().back();
        			driver.navigate().back();
//        			element = driver.findElement(By.id("formBuscaPadraoParticipantesExtensao:cmdCancelar"));
//        			element.click();
        			return;
        		} else System.out.println("Elemento de erro nao encontrado!");
        	}
    	} catch (NoSuchElementException e) {}
    	
    	element = driver.findElement(By.id("formBuscaPadraoParticipantesExtensao:selecionarParticipante"));
    	if (element != null)
    		element.click();
    	else
    		System.out.println("nao encontrou botao dinamico!");
    	
    	Select select = new Select(driver.findElement(By.id("formIncluirParticipante:tipoParticipacao")));
    	select.selectByIndex(2);
    	
    	element = driver.findElement(By.id("formIncluirParticipante:frequencia"));
    	element.clear();
    	element.sendKeys("100");
    	
    	element = driver.findElement(By.id("formIncluirParticipante:certificado:0"));
    	element.click();
    	
    	element = driver.findElement(By.id("formIncluirParticipante:btCadastrar"));
    	element.click();
    	
    	try {
    		element = driver.findElement(By.id("painel-erros"));
        	if (element != null) {
        		element = element.findElement(By.tagName("ul")).findElement(By.tagName("li"));
        		if (element.getText().contains("O participante")) {
        			System.out.println("Problema com " + cpf + ": " + element.getText());
        			driver.navigate().back();
        			driver.navigate().back();
        			driver.navigate().back();
        			driver.navigate().back();
//        			element = driver.findElement(By.id("formBuscaPadraoParticipantesExtensao:cmdCancelar"));
//        			element.click();
        			return;
        		} else System.out.println("Elemento de erro nao encontrado!");
        	} else System.out.println("Usuario " + cpf + " cadastrado com sucesso!");
    	} catch (NoSuchElementException e) {}
    	
    }
    
    public void makeLogin() {
    	WebElement element = driver.findElement(By.name("user.login"));
    	if (element != null) {
    		element.sendKeys("06411272433");
    	}
    	
    	element = driver.findElement(By.name("user.senha"));
    	if (element != null)
    		element.sendKeys("tel2j05!");
    	
    	element = driver.findElement(By.cssSelector("input[value='Entrar']"));
    	if (element != null)
    		element.click();
    }
    
    public static void main(String[] args) {
		Main m;
		try {
			m = new Main();
			m.openSIGAAURL();
			m.makeLogin();
			m.goToProjectsPage();
			
			FileInputStream fis = new FileInputStream("/Users/thiagodesales/Documents/workspace-sts-3.8.3.RELEASE/SWO/participantes2.xlsx");
			Workbook workgroup = new XSSFWorkbook(fis);
			
			Sheet sheet = workgroup.getSheetAt(0);
			
			Iterator<Row> rowIter = sheet.iterator();
			rowIter.next();//title
			String cpf = "";
			while(rowIter.hasNext()) {
				Row currentRow = rowIter.next();
				currentRow.getCell(1).setCellType(CellType.STRING);
				cpf = currentRow.getCell(1).getStringCellValue();
				System.out.println("Cadastrando participante " + cpf);
				m.addParticipant(cpf);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
}
