import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FilesManager {
	
	public String createFolder(String path, String year, String period) throws IOException {
		System.out.println("createFolder " + path + " - year " + year + " - period " + period);
//		new File("../potentially/long/pathname/without/all/dirs")).mkdirs();
		StringBuffer s = new StringBuffer(path);
		char isSlash = s.charAt(path.length()-1);
		boolean yearFolder = false;
		boolean periodFolder = false;
		String newPath= "";
		if (isSlash == '/') 
			newPath = path + year + "/";
		else 
			newPath = path + "/" + year + "/";
		
		System.out.println("path para folder: " + newPath );
		if (Files.notExists(Paths.get(newPath), LinkOption.NOFOLLOW_LINKS)) {
			System.out.println("criando pois ainda não existe...");
			yearFolder = new File(newPath).mkdir();
		} else
			yearFolder = true;
		
		newPath = newPath + period + "/";
		System.out.println("path para period: " + newPath );
		if (Files.notExists(Paths.get(newPath), LinkOption.NOFOLLOW_LINKS)) {
			System.out.println("criando pois ainda não existe...");
			periodFolder = new File(newPath).mkdir();
		} else
			periodFolder = true;
		
		if (periodFolder && yearFolder) {
			System.out.println("pastas criadas com sucesso!");
			return newPath;
		} else  {
			System.out.println("não foi possível criar pastas: " + "periodFolder: " + periodFolder + " yearFolder: " + yearFolder);
			throw new IOException("Não foi possível criar diretórios para a pasta selecionada!");
		}
			
	}
	
	private void createFile(Workbook workbook, String name, String path) {
		System.out.println("Criando arquivo excel para arquivo " + path + name);
		try {
			FileOutputStream fos = new FileOutputStream(path + name + ".xlsx");
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createClassPlanRowTitle(XSSFSheet sheet) {
		int rowIndex = 0;
		int cellIndex = 0;
		
        Row titleRow = sheet.createRow(rowIndex);
        Cell codeCell = titleRow.createCell(cellIndex);
        codeCell.setCellValue(HeaderNames.GOALS.getHeaderName());
        
        Cell nameCell = titleRow.createCell(++cellIndex);
        nameCell.setCellValue(HeaderNames.METHODS.getHeaderName());
        
        Cell ab1Cell = titleRow.createCell(++cellIndex);
        ab1Cell.setCellValue(HeaderNames.EVALUATION.getHeaderName());
        
        Cell ab2Cell = titleRow.createCell(++cellIndex);
        ab2Cell.setCellValue(HeaderNames.REFERENCES.getHeaderName());
	}
	
	private void createGradesRowTitle(XSSFSheet sheet) {
		int rowIndex = 0;
		int cellIndex = 0;
		
        Row titleRow = sheet.createRow(rowIndex);
        Cell codeCell = titleRow.createCell(cellIndex);
        codeCell.setCellValue(HeaderNames.MATRICULA.getHeaderName());
        
        Cell nameCell = titleRow.createCell(++cellIndex);
        nameCell.setCellValue(HeaderNames.NAME.getHeaderName());
        
        Cell ab1Cell = titleRow.createCell(++cellIndex);
        ab1Cell.setCellValue(HeaderNames.PROVA_AB1.getHeaderName());
        
        Cell ab2Cell = titleRow.createCell(++cellIndex);
        ab2Cell.setCellValue(HeaderNames.PROVA_AB2.getHeaderName());
        
        Cell reposCell = titleRow.createCell(++cellIndex);
        reposCell.setCellValue(HeaderNames.PROVA_REPOSICAO.getHeaderName());
        
        Cell pfCell = titleRow.createCell(++cellIndex);
        pfCell.setCellValue(HeaderNames.PROVA_FINAL.getHeaderName());
	}
	
//	public Turma getClassPlanFromExcel(Turma turma) throws IOException {
//		FileInputStream fis = new FileInputStream(turma.getFile());
//		Workbook workgroup = new XSSFWorkbook(fis);
//		Sheet sheet = workgroup.getSheetAt(0);
//		HashMap<HeaderNames, Integer> headers = new HashMap<HeaderNames, Integer>();
//		
//		Row row = sheet.getRow(0);
//		Iterator<Cell> cellIterator = row.cellIterator();
//		while(cellIterator.hasNext()) {
//			Cell cellTemp = cellIterator.next();
//			
//			if (cellTemp.getStringCellValue().equals(HeaderNames.GOALS.getHeaderName()))
//				headers.put(HeaderNames.GOALS, new Integer(cellTemp.getColumnIndex()));
//			
//			if (cellTemp.getStringCellValue().equals(HeaderNames.METHODS.getHeaderName()))
//				headers.put(HeaderNames.METHODS, new Integer(cellTemp.getColumnIndex()));
//			
//			if (cellTemp.getStringCellValue().equals(HeaderNames.EVALUATION.getHeaderName()))
//				headers.put(HeaderNames.EVALUATION, new Integer(cellTemp.getColumnIndex()));
//			
//			if (cellTemp.getStringCellValue().equals(HeaderNames.REFERENCES.getHeaderName()))
//				headers.put(HeaderNames.REFERENCES, new Integer(cellTemp.getColumnIndex()));	
//		}
//		
//		Iterator<Row> rows = sheet.rowIterator();
//		while(rows.hasNext()) {
//			Row currentRow = rows.next();
//			int currentRowIndex = currentRow.getRowNum();
//			
//			if (currentRowIndex == 1) {
//				turma.setGoals(currentRow.getCell(headers.get(HeaderNames.GOALS)).getStringCellValue());
//				turma.setMethods(currentRow.getCell(headers.get(HeaderNames.METHODS)).getStringCellValue());
//				turma.setEvaluation(currentRow.getCell(headers.get(HeaderNames.EVALUATION)).getStringCellValue());
//				turma.addReference(currentRow.getCell(headers.get(HeaderNames.REFERENCES)).getStringCellValue());
//			} else if (currentRowIndex > 1)
//				turma.addReference(currentRow.getCell(headers.get(HeaderNames.REFERENCES)).getStringCellValue());
//		}
//		
//		return turma;
//	}
	
	public boolean createExcelForGrades(ArrayList<Turma> turmas, String path) {
		System.out.println("Criando arquivos excel para turmas para o caminho " + path);
		if (turmas.size() > 0) {
			System.out.println("Turmas encontradas...");
			for(Turma turma : turmas) {
				XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet = workbook.createSheet("Notas");
		        this.createGradesRowTitle(sheet);
		        
		        int cellIndex = 0;
		        int rowIndex = 1;
		        ArrayList<Student> students = turma.getStudents();
		        Row rowStudent = null;
		        for(Student student : students) {
		        	rowStudent = sheet.createRow(rowIndex++);
		        	Cell codeCell = rowStudent.createCell(cellIndex);
		        	codeCell.setCellValue(student.getCode());
		        	
		        	Cell nameCell = rowStudent.createCell(++cellIndex);
		        	nameCell.setCellValue(student.getName());
		        	
		        	if (!student.getScore1().equals(""))
		        		rowStudent.createCell(++cellIndex).setCellValue(student.getScore1());
		        	
		        	if (!student.getScore2().equals(""))
		        		rowStudent.createCell(++cellIndex).setCellValue(student.getScore2());
		        	
		        	if (!student.getScoreReposition().equals(""))
		        		rowStudent.createCell(++cellIndex).setCellValue(student.getScoreReposition());
		        	
		        	if (!student.getScoreFinal().equals(""))
		        		rowStudent.createCell(++cellIndex).setCellValue(student.getScoreFinal());
		        	
		        	cellIndex = 0;
		        }
		        
		        sheet.autoSizeColumn(15); //not working
		        this.createFile(workbook, turma.getFullName(), path);
			}
			
			return true;
		} else {
			System.out.println("Turmas não encontradas...");
			return false;
			
		}

	}
	
	public void createExcelForCP(Semestre semester, String path) {
		System.out.println("Criando arquivos excel de planos de aula para turmas abaixo e pasta para o caminho " + path);
		ArrayList<Turma> turmas = semester.getTurmas();
		
		for(Turma eachTurma : turmas) {
			XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Plano de Aula");
	        this.createClassPlanRowTitle(sheet);
	        
	        int cellIndex = 0;
	        int rowIndex = 1;
	        
	        Row data = sheet.createRow(rowIndex++);
	        Cell goalCell = data.createCell(cellIndex);
	        goalCell.setCellValue(eachTurma.getGoals());
	        	
	        Cell methodsCell = data.createCell(++cellIndex);
	        methodsCell.setCellValue(eachTurma.getMethods());
	        
	        Cell evaluationCell = data.createCell(++cellIndex);
	        evaluationCell.setCellValue(eachTurma.getEvaluation());
	        
	        Cell referenceCell = data.createCell(++cellIndex);
	        referenceCell.setCellValue(eachTurma.getReferences().get(0));
	        
	        sheet.autoSizeColumn(15); //not working
	        this.createFile(workbook, "PLANO-"+eachTurma.getFullName(), path);
		}  

	}
	
	public ArrayList<Student> readTurma(Turma turma) throws IOException {//improve to return GroupFileNotFoundException
		//new File(turma.getFullName()+".xlsx")
		FileInputStream fis = new FileInputStream(turma.getFile());
		Workbook workgroup = new XSSFWorkbook(fis);
		Sheet sheet = workgroup.getSheetAt(0);
		HashMap<HeaderNames, Integer> headers = new HashMap<HeaderNames, Integer>();
		
		Row row = sheet.getRow(0);
		Iterator<Cell> cellIterator = row.cellIterator();
		
		while(cellIterator.hasNext()) {
			Cell currentCell = cellIterator.next();
			String text = currentCell.getStringCellValue();
			
			System.out.println("TEXTO NESSE CELL " + text + " na coluna " + currentCell.getColumnIndex());
			
			if(text.equalsIgnoreCase(HeaderNames.MATRICULA.getHeaderName())) 
				headers.put(HeaderNames.MATRICULA, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.NAME.getHeaderName()))
				headers.put(HeaderNames.NAME, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_AB1.getHeaderName()))
				headers.put(HeaderNames.PROVA_AB1, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_AB2.getHeaderName()))
				headers.put(HeaderNames.PROVA_AB2, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_REPOSICAO.getHeaderName()))
				headers.put(HeaderNames.PROVA_REPOSICAO, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_FINAL.getHeaderName()))
				headers.put(HeaderNames.PROVA_FINAL, new Integer(currentCell.getColumnIndex()));
			
		}
		
		//Preciso saber qual a quantidade de tipos de notas disponíveis. Quanto mais "longo" estiver o período, 
		//o algoritmo de salvar notas se comportará "um pouco" diferente.
		for(HeaderNames headerName : HeaderNames.values()) {
			if (headers.get(headerName) != null)
				turma.setMaxData(headerName);
		}
		
		System.out.println(headers);
		ArrayList<Student> students = new ArrayList<Student>();
		Iterator<Row> rowIterator = sheet.rowIterator();
		rowIterator.next();//jumps first row.
		
		while(rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			String studentCode = "";
			Integer indexRef = null;
			try {
				indexRef = headers.get(HeaderNames.MATRICULA);
				if (indexRef != null)
					studentCode = currentRow.getCell(indexRef.intValue()).getStringCellValue();
			} catch (IllegalStateException e) {
				//Sometimes, student's codes are read as numbers, not strings.
				System.out.println("Matrícula como número!");
				if (indexRef != null) {
//					studentCode = new Double(currentRow.getCell(indexRef.intValue()).getNumericCellValue()).toString();
					studentCode = Double.toString(currentRow.getCell(indexRef.intValue()).getNumericCellValue());
					System.out.println("MATRICULA REUPERADA: " + studentCode);
					
					DecimalFormat decimalFormatter = new DecimalFormat("############");
					studentCode = decimalFormatter.format(Double.parseDouble(studentCode));
					
					System.out.println("MATRICULA MODIFICADA: " + studentCode);
				}
			}
			
			String studentName = "";
			indexRef = headers.get(HeaderNames.NAME);
			if (indexRef != null)
				studentName = currentRow.getCell(indexRef.intValue()).getStringCellValue();
			//else //throws exception
				
			Cell cellTemp = null;
			String studentAB1 = "";
			indexRef = headers.get(HeaderNames.PROVA_AB1);
			if (indexRef != null) {
				System.out.println("entrou 1");
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					System.out.println("entrou 2");
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) { //TODO
						System.out.println("entrou 3");
						studentAB1 = new Double(cellTemp.getNumericCellValue()).toString();
						System.out.println("Leu nota: " + studentAB1);
					} else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						System.out.println("entrou 4");
						String datum = "";
						try {
							datum = cellTemp.getStringCellValue();
							System.out.println("Leu nota em str: " + datum);
							studentAB1 = new Double(datum).toString();
							System.out.println("Leu nota em str2: " + studentAB1);
						} catch (NumberFormatException e) {
							if (datum != null && (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL")) )
								studentAB1 = "FAL";
							System.out.println("Estudante não tem nota da AB1.");
						} catch (IllegalStateException e) {
							System.out.println(e.getMessage());
							if (e.getMessage().equals("Cannot get a STRING value from a NUMERIC formula cell")) {
								double datumInDouble = cellTemp.getNumericCellValue();
								System.out.println("Leu nota em double: " + datumInDouble);
								studentAB1 = new Double(datumInDouble).toString();
								System.out.println("Leu nota em str2: " + studentAB1);
							}
						}
					} else {
						System.out.println("entrou 5");
					}
					System.out.println("passou 6");
				} else
					System.out.println("Estudante não tem nota da AB1.");
			} else System.out.println("indexRef null");
			System.out.println("AB1 = " + studentAB1);
			
			String studentAB2 = "";
			indexRef = headers.get(HeaderNames.PROVA_AB2);
			if (indexRef != null) {
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) { //TODO 
						studentAB2 = new Double(cellTemp.getNumericCellValue()).toString();
					} else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						String datum = "";
						try {
							datum = cellTemp.getStringCellValue();
							studentAB2 = new Double(datum).toString();
						} catch (NumberFormatException e) {
							if (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL"))
								studentAB2 = "FAL";
							System.out.println("Estudante não tem nota da AB2.");
						} catch (IllegalStateException e) {
							studentAB2 = new Double(cellTemp.getNumericCellValue()).toString();
						}
					}
				} else
					System.out.println("Estudante não tem nota da AB2.");
			}
			System.out.println("AB2 = " + studentAB2);	
			
			String studentRep = "";
			indexRef = headers.get(HeaderNames.PROVA_REPOSICAO);
			if (indexRef != null) {
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) //TODO 
						studentRep = new Double(cellTemp.getNumericCellValue()).toString();
					else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						String datum = cellTemp.getStringCellValue();
						try {
							studentRep = new Double(datum).toString();
						} catch (NumberFormatException e) {
							if (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL"))
								studentRep = "FAL";
							System.out.println("Estudante não tem nota da reposição.");
						}
					}
				} else
					System.out.println("Estudante não tem nota da reposição.");
			}
			System.out.println("REP = " + studentAB2);
			
			String studentFinal = "";
			indexRef = headers.get(HeaderNames.PROVA_FINAL);
			if (indexRef != null) {
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) //TODO 
						studentFinal = new Double(cellTemp.getNumericCellValue()).toString();
					else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						String datum = cellTemp.getStringCellValue();
						try {
							studentFinal = new Double(datum).toString();
						} catch (NumberFormatException e) {
							if (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL"))
								studentFinal = "FAL";
							System.out.println("Estudante não tem nota para prova final.");
						}
					}
				} else 
					System.out.println("Estudante não tem nota para prova final.");
			}
			
			System.out.println("FINAL = " + studentAB2);
			Student currentStudent = new Student(studentName, studentCode, studentAB1, studentAB2, studentRep, studentFinal);
			System.out.println("NOVO ALUNO CRIADO PARA SALVAR NOTAS ");
			System.out.println(currentStudent);
			students.add(currentStudent);
		}
		
		return students;
	}
	
	public HashMap<String, Student> readTurma2(Turma turma) throws IOException {//improve to return GroupFileNotFoundException
		FileInputStream fis = new FileInputStream(turma.getFile());
		Workbook workgroup = new XSSFWorkbook(fis);
		Sheet sheet = workgroup.getSheetAt(0);
		HashMap<HeaderNames, Integer> headers = new HashMap<HeaderNames, Integer>();
		
		Row row = sheet.getRow(0);
		Iterator<Cell> cellIterator = row.cellIterator();
		
		while(cellIterator.hasNext()) {
			Cell currentCell = cellIterator.next();
			String text = currentCell.getStringCellValue();
			
			System.out.println("TEXTO NESSE CELL " + text + " na coluna " + currentCell.getColumnIndex());
			
			if(text.equalsIgnoreCase(HeaderNames.MATRICULA.getHeaderName())) 
				headers.put(HeaderNames.MATRICULA, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.NAME.getHeaderName()))
				headers.put(HeaderNames.NAME, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_AB1.getHeaderName()))
				headers.put(HeaderNames.PROVA_AB1, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_AB2.getHeaderName()))
				headers.put(HeaderNames.PROVA_AB2, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_REPOSICAO.getHeaderName()))
				headers.put(HeaderNames.PROVA_REPOSICAO, new Integer(currentCell.getColumnIndex()));
			
			else if(text.equalsIgnoreCase(HeaderNames.PROVA_FINAL.getHeaderName()))
				headers.put(HeaderNames.PROVA_FINAL, new Integer(currentCell.getColumnIndex()));
			
		}
		
		//Preciso saber qual a quantidade de tipos de notas disponíveis. Quanto mais "longo" estiver o período, 
		//o algoritmo de salvar notas se comportará "um pouco" diferente.
		for(HeaderNames headerName : HeaderNames.values()) {
			if (headers.get(headerName) != null)
				turma.setMaxData(headerName);
		}
		
		System.out.println(headers);
		HashMap<String, Student> students = new HashMap<>();
		Iterator<Row> rowIterator = sheet.rowIterator();
		rowIterator.next();//jumps first row.
		
		while(rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			String studentCode = "";
			Integer indexRef = null;
			try {
				indexRef = headers.get(HeaderNames.MATRICULA);
				if (indexRef != null)
					studentCode = currentRow.getCell(indexRef.intValue()).getStringCellValue();
			} catch (IllegalStateException e) {
				//Sometimes, student's codes are read as numbers, not strings.
				System.out.println("Matrícula como número!");
				if (indexRef != null) {
//					studentCode = new Double(currentRow.getCell(indexRef.intValue()).getNumericCellValue()).toString();
					studentCode = Double.toString(currentRow.getCell(indexRef.intValue()).getNumericCellValue());
					System.out.println("MATRICULA REUPERADA: " + studentCode);
					
					DecimalFormat decimalFormatter = new DecimalFormat("############");
					studentCode = decimalFormatter.format(Double.parseDouble(studentCode));
					
					System.out.println("MATRICULA MODIFICADA: " + studentCode);
				}
			}
			
			String studentName = "";
			indexRef = headers.get(HeaderNames.NAME);
			if (indexRef != null)
				studentName = currentRow.getCell(indexRef.intValue()).getStringCellValue();
			//else //throws exception
				
			Cell cellTemp = null;
			String studentAB1 = "";
			indexRef = headers.get(HeaderNames.PROVA_AB1);
			if (indexRef != null) {
				System.out.println("entrou 1");
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					System.out.println("entrou 2");
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) { //TODO
						System.out.println("entrou 3");
						studentAB1 = new Double(cellTemp.getNumericCellValue()).toString();
						System.out.println("Leu nota: " + studentAB1);
					} else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						System.out.println("entrou 4");
						String datum = "";
						try {
							datum = cellTemp.getStringCellValue();
							System.out.println("Leu nota em str: " + datum);
							studentAB1 = new Double(datum).toString();
							System.out.println("Leu nota em str2: " + studentAB1);
						} catch (NumberFormatException e) {
							if (datum != null && (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL")) )
								studentAB1 = "FAL";
							System.out.println("Estudante não tem nota da AB1.");
						} catch (IllegalStateException e) {
							System.out.println(e.getMessage());
							if (e.getMessage().equals("Cannot get a STRING value from a NUMERIC formula cell")) {
								double datumInDouble = cellTemp.getNumericCellValue();
								System.out.println("Leu nota em double: " + datumInDouble);
								studentAB1 = new Double(datumInDouble).toString();
								System.out.println("Leu nota em str2: " + studentAB1);
							}
						}
					} else {
						System.out.println("entrou 5");
					}
					System.out.println("passou 6");
				} else
					System.out.println("Estudante não tem nota da AB1.");
			} else System.out.println("indexRef null");
			System.out.println("AB1 = " + studentAB1);
			
			String studentAB2 = "";
			indexRef = headers.get(HeaderNames.PROVA_AB2);
			if (indexRef != null) {
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) { //TODO 
						studentAB2 = new Double(cellTemp.getNumericCellValue()).toString();
					} else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						String datum = "";
						try {
							datum = cellTemp.getStringCellValue();
							studentAB2 = new Double(datum).toString();
						} catch (NumberFormatException e) {
							if (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL"))
								studentAB2 = "FAL";
							System.out.println("Estudante não tem nota da AB2.");
						} catch (IllegalStateException e) {
							studentAB2 = new Double(cellTemp.getNumericCellValue()).toString();
						}
					}
				} else
					System.out.println("Estudante não tem nota da AB2.");
			}
			System.out.println("AB2 = " + studentAB2);	
			
			String studentRep = "";
			indexRef = headers.get(HeaderNames.PROVA_REPOSICAO);
			if (indexRef != null) {
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) //TODO 
						studentRep = new Double(cellTemp.getNumericCellValue()).toString();
					else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						String datum = cellTemp.getStringCellValue();
						try {
							studentRep = new Double(datum).toString();
						} catch (NumberFormatException e) {
							if (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL"))
								studentRep = "FAL";
							System.out.println("Estudante não tem nota da reposição.");
						}
					}
				} else
					System.out.println("Estudante não tem nota da reposição.");
			}
			System.out.println("REP = " + studentAB2);
			
			String studentFinal = "";
			indexRef = headers.get(HeaderNames.PROVA_FINAL);
			if (indexRef != null) {
				cellTemp = currentRow.getCell(indexRef.intValue());
				if (cellTemp != null) {//getCellTypeEnum
					if(cellTemp.getCellTypeEnum() == CellType.NUMERIC) //TODO 
						studentFinal = new Double(cellTemp.getNumericCellValue()).toString();
					else if(cellTemp.getCellTypeEnum() == CellType.STRING || cellTemp.getCellTypeEnum() == CellType.FORMULA) {//TODO
						String datum = cellTemp.getStringCellValue();
						try {
							studentFinal = new Double(datum).toString();
						} catch (NumberFormatException e) {
							if (datum.equalsIgnoreCase("F") || datum.equalsIgnoreCase("FAL"))
								studentFinal = "FAL";
							System.out.println("Estudante não tem nota para prova final.");
						}
					}
				} else 
					System.out.println("Estudante não tem nota para prova final.");
			}
			
			System.out.println("FINAL = " + studentAB2);
			Student currentStudent = new Student(studentName, studentCode, studentAB1, studentAB2, studentRep, studentFinal);
			System.out.println("NOVO ALUNO CRIADO PARA SALVAR NOTAS ");
			System.out.println(currentStudent);
			students.put(studentCode, currentStudent);
		}
		
		return students;
	}
	
	public static void main(String[] args) {
//		FilesManager fm = new FilesManager();
//		try {
//			Turma t = fm.getClassPlanFromExcel("PLANO-CPTA054 - PROGRAMAÇÃO 2 - 60h-CIÊNCIA DA COMPUTAÇÃO.xlsx");
//			System.out.println(t);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String path = "/Users/thiagobrunoms/teste";
//		String year = "2016";
//		String period = "1";
//		
//		StringBuffer s = new StringBuffer(path);
//		char isSlash = s.charAt(path.length()-1);
//		boolean yearFolder;
//		String newPath= "";
//		if (isSlash == '/') 
//			newPath = path + year + "/";
//		else 
//			newPath = path + "/" + year + "/";
//		
//		System.out.println(newPath);
//		
//		System.out.println(newPath + period);
////		FilesManager ex = new FilesManager();
////		ex.READ
	}
	
}
