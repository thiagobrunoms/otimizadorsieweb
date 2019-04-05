package utils;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Calendar;

import exceotions.DataInputException;

public class DataValidation {

	private static boolean validateCPF(String strCPF)  {
		int Soma;
	    float Resto;
	    Soma = 0;
	    
		if (strCPF == "00000000000" || strCPF.length() < 11) return false;
		
		if (strCPF.contains(".") || strCPF.contains("-")) {
			strCPF = strCPF.replace(".", "");
			strCPF = strCPF.replace("-", "");
		}
		
		for (int i=1; i<=9; i++) Soma = Soma + new Integer(strCPF.substring(i-1, i)).intValue() * (11 - i);
		Resto = (Soma * 10) % 11;
		
	    if ((Resto == 10) || (Resto == 11))  Resto = 0;
	    if (Resto != new Integer(strCPF.substring(9, 10)).intValue() ) return false;
		
		Soma = 0;
	    for (int i = 1; i <= 10; i++) Soma = Soma + new Integer(strCPF.substring(i-1, i)).intValue() * (12 - i);
	    Resto = (Soma * 10) % 11;
		
	    if ((Resto == 10) || (Resto == 11))  Resto = 0;
	    if (Resto != new Integer(strCPF.substring(10, 11)).intValue() ) return false;
	    
	    return true;
	}
	
	public static void checkBasicInputData(String strCPF, String password, String year) throws DataInputException {
		if (!validateCPF(strCPF))
			throw new DataInputException("CPF Inválido!");
		
		if (password.length() == 0 || password.length() > 25)
			throw new DataInputException("Senha inválida!");
		
		try {
			int yearInt = new Integer(year).intValue();
			if(year.length() != 4 || yearInt < 2014 || yearInt > Calendar.getInstance().get(Calendar.YEAR)+2)
				throw new DataInputException("Ano inválido!");
		} catch (NumberFormatException e) {
			throw new DataInputException("Ano inválido!");
		}
		
	}
	
	public static void checkDataPathInputData(String path) throws DataInputException {
		if (path.length() == 0 || Files.notExists(Paths.get(path), LinkOption.NOFOLLOW_LINKS))
			throw new DataInputException("Caminho inválido para a pasta!");
	}
	
	public static void main(String[] args) {
		
	}
	
}
