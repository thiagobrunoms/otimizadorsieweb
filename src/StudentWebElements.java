import org.openqa.selenium.WebElement;

public class StudentWebElements {

	private WebElement ab1;
	private WebElement ab2;
	private WebElement reposicao;
	private WebElement finalp;
	
	public StudentWebElements(WebElement ab1, WebElement ab2, WebElement reposicao, WebElement finalp) {
		super();
		this.ab1 = ab1;
		this.ab2 = ab2;
		this.reposicao = reposicao;
		this.finalp = finalp;
	}
	public WebElement getAb1() {
		return ab1;
	}
	public void setAb1(WebElement ab1) {
		this.ab1 = ab1;
	}
	public WebElement getAb2() {
		return ab2;
	}
	public void setAb2(WebElement ab2) {
		this.ab2 = ab2;
	}
	public WebElement getReposicao() {
		return reposicao;
	}
	public void setReposicao(WebElement reposicao) {
		this.reposicao = reposicao;
	}
	public WebElement getFinalp() {
		return finalp;
	}
	public void setFinalp(WebElement finalp) {
		this.finalp = finalp;
	}
	
	
	
	
}
