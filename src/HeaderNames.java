
public enum HeaderNames {

	MATRICULA ("Matrícula"),
	NAME ("Nome"),
	PROVA_AB1 ("AB1"),
	PROVA_AB2 ("AB2"),
	PROVA_REPOSICAO ("Reposição"),
	PROVA_FINAL ("Prova Final"),
	
	GOALS ("Objetivos"),
	METHODS ("Metodologia"),
	EVALUATION ("Avaliação"),
	REFERENCES ("Referências");
	
	private final String header;
	
	
	private HeaderNames(String headerName) {
		this.header = headerName;
	}
	
	public String getHeaderName() {
		return this.header;
	}
	
	public static void main(String[] args) {
		for(HeaderNames k : HeaderNames.values()) {
			System.out.println(k);
		}
	}
	
}

