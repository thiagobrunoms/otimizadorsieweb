package aracomp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Testes {
	public static void main(String[] args) {
		
		try {
			FileInputStream fis = new FileInputStream("/Users/thiagodesales/Documents/workspace-sts-3.8.3.RELEASE/SWO/participantes.xlsx");
			Workbook workgroup = new XSSFWorkbook(fis);
			
			Sheet sheet = workgroup.getSheetAt(0);
			
			Iterator<Row> rowIter = sheet.iterator();
			rowIter.next();//title
			String cpf = "";
			while(rowIter.hasNext()) {
				Row currentRow = rowIter.next();
				currentRow.getCell(1).setCellType(CellType.STRING);
				cpf = currentRow.getCell(1).getStringCellValue();
				System.out.println(cpf);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
