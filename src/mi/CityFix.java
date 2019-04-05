package mi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CityFix {

	private HashMap<String, Integer> cityHash;
	
	public CityFix() {
		cityHash = new HashMap<String, Integer>();
		cityHash.put("Arapiraca", 0);
		cityHash.put("Girau", 0);
		cityHash.put("Maceió", 0);
		cityHash.put("Olho dagua do Casado", 0);
	}
	
	public void read() throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream("pesquisami.xlsx");
		Workbook workgroup = new XSSFWorkbook(fis);
		Sheet sheet = workgroup.getSheetAt(0);
		
		Iterator<Row> cellIterRows = sheet.rowIterator();
		cellIterRows.next();
		int a = 0;
		while(cellIterRows.hasNext()) {
			Row r = cellIterRows.next();
			Cell city = r.getCell(3);
			String cityName = city.getStringCellValue();
			
			if (cityName.toLowerCase().contains("arapiraca")) {
				cityHash.put("Arapiraca", cityHash.get("Arapiraca") + 1);
			} else if (cityName.toLowerCase().contains("girau")) {
				cityHash.put("Girau", cityHash.get("Girau") + 1);
			} else if (cityName.toLowerCase().contains("maceio")) {
				cityHash.put("Maceió", cityHash.get("Maceió") + 1);
			} else if (cityName.toLowerCase().contains("casado")) {
				cityHash.put("Olho dagua do Casado", cityHash.get("Olho dagua do Casado") + 1);
			}
		}
		
		for (String city : cityHash.keySet()) {
			System.out.println(city + " = " + cityHash.get(city));
		}
	}
	
	public static void main(String[] args) {
		try {
			new CityFix().read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
