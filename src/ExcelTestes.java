import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTestes {
	public static void main(String[] args) {
		
		Workbook workgroup = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("/Users/thiagodesales/Downloads/participantesAracomp.xlsx");
			workgroup = new XSSFWorkbook(fis);
			Sheet sheet = workgroup.getSheetAt(0);
			Iterator<Row> rowIter = sheet.rowIterator();
			
			int quantity = 0;
			rowIter.next();
			String emails = "";
			while(rowIter.hasNext()) {
				Row r = rowIter.next();
				Cell cell = r.getCell(2);
				if (cell != null) {
					String email = cell.getStringCellValue();
					emails = emails + ", " + email;
//					System.out.println(email);
					quantity++;
				}
			}
			
			System.out.println("quantidade: " + quantity);
			System.out.println(emails);
			
			workgroup.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
